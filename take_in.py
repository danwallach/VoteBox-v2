#!/usr/bin/env python

import RPi.GPIO as GPIO
import time
import logging
import subprocess

# To relaunch script as sudo.
# See https://gist.github.com/davejamesmiller/1965559
import os
import sys

import config
import diverter

"""
Takes in all ballots placed in tray. 

Driven by the paper feeder of the HP Deskjet 1000 J110.
"""
logging.basicConfig(level=logging.DEBUG)

# Output pins.
MOTOR_ENABLE = 17
MOTOR_FORWARD = 27
MOTOR_BACKWARD = 22

# Input pins.
HALFWAY_TRIGGER = 23

def setup():
    """Set up the GPIO pins as input and output."""
    logging.info("Running Ballot Diverter V2.")

    logging.info("Setting up pins...")

    GPIO.setmode(GPIO.BCM)

    GPIO.setup(MOTOR_ENABLE, GPIO.OUT)
    pwm = GPIO.PWM(MOTOR_ENABLE, 120)
    pwm.start(0)

    GPIO.setup(MOTOR_FORWARD, GPIO.OUT)
    GPIO.setup(MOTOR_BACKWARD, GPIO.OUT)

    GPIO.setup(HALFWAY_TRIGGER, GPIO.IN, pull_up_down=GPIO.PUD_UP)

    logging.info("Brocasting status - 'Waiting.'")
    config.status = "waiting"

    return pwm

def take_in(pwm):
    """Take in all ballots."""
    tray_empty = False
    diverter.up()
    timeout = time.time() + 1   # One second from now.

    logging.info("Taking in a sheet...")

    logging.info("Broacasting status - 'Pending.'")
    config.status = "pending"

    logging.debug("Rolling forward...")
    before_halfway = GPIO.input(HALFWAY_TRIGGER) # True if trigger depressed.
    while before_halfway:
        pwm.ChangeDutyCycle(100)
        GPIO.output(MOTOR_FORWARD, True)
        GPIO.output(MOTOR_BACKWARD, False)

        if time.time() > timeout:
            logging.info("Tray is empty.")
            tray_empty = True
            break

        before_halfway = GPIO.input(HALFWAY_TRIGGER) # True if trigger depressed.

    logging.debug("Rolling backward...")
    time.sleep(.1)
    if not tray_empty:
        slow_motor(pwm)
    while not before_halfway:
        pwm.ChangeDutyCycle(100)
        GPIO.output(MOTOR_FORWARD, False)
        GPIO.output(MOTOR_BACKWARD, True)

        before_halfway = GPIO.input(HALFWAY_TRIGGER) # True if trigger depressed.
    
    time.sleep(.1)
    if not tray_empty:
        take_in(pwm)

def slow_motor(pwm):
    """Slow down motor to make accept or reject decision."""
    logging.debug('Slowing motor.')
    pwm.ChangeDutyCycle(40)
    GPIO.output(MOTOR_FORWARD, False)
    GPIO.output(MOTOR_BACKWARD, True)
    time.sleep(3)

    logging.info('Firing scanner for three seconds.')
    # call(["./scan", "3"])
    # scan = Popen(["./scan", "3"], stdout=PIPE)
    # output, err = p.communicate()
    barcode = subprocess.check_output(["./scan", "3"])

    if barcode:
        logging.info('Barcode read. Drawbridge down.')
        diverter.down()

        logging.info("Brocasting status - 'Accept.'")
        config.status = "accept"
    else:
        logging.info("Broacasting status - 'Reject.'")
        config.status = "reject"


def clean_up(pwm):
    """Roll backward to open tray, then shut down pins."""
    logging.info('Cleaning up.')
    pwm.ChangeDutyCycle(100)
    GPIO.output(MOTOR_FORWARD, False)
    GPIO.output(MOTOR_BACKWARD, True)
    time.sleep(1)

    pwm.stop()
    GPIO.cleanup()

def main():
    pwm = setup()
    take_in(pwm)
    clean_up(pwm)

if __name__ == '__main__':
    try:
        if os.geteuid() != 0:
            os.execvp("sudo", ["sudo"] + sys.argv)
        main()
    except KeyboardInterrupt:
        logging.info('Keyboard interrupt.')
        pwm = setup()
        clean_up(pwm)
