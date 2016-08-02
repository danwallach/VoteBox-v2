"""
Diverter control module for vote box, calibrated for HD 6001HB servo.
Example usage:
    
import diverter
diverter.up() # moves diverter up
diverter.down() # moves diverter down

Note that RPi.GPIO.cleanup() needs to be executed by the caller before
program termination.

Author: Jerry Lue
"""

import diverter_config as config
import RPi.GPIO as GPIO
import rpi_servodriver as sdriver
import time

SERVO_PIN = 18 # hardware PWM pin

servmap = {
    "6001HB": {"up": 0.4, "down": 1.0}, \
    "S3003": {"up": 0, "down": 0.7}}

"""
Moves diverter up.
"""
def up():
    GPIO.setmode(GPIO.BCM)
    servo = sdriver.init(SERVO_PIN)
    sdriver.start(servo, servmap[config.SERVO_MODEL]["up"])
    time.sleep(1)
    sdriver.stop(servo)
    return

"""
Moves diverter down.
"""
def down():
    GPIO.setmode(GPIO.BCM)
    servo = sdriver.init(SERVO_PIN)
    sdriver.start(servo, servmap[config.SERVO_MODEL]["down"])
    time.sleep(1)
    sdriver.stop(servo)
    return
