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

import RPi.GPIO as GPIO
import rpi_servodriver as sdriver
import time

SERVO_PIN = 18 # hardware PWM pin

"""
Moves diverter up.
"""
def up():
    GPIO.setmode(GPIO.BCM)
    servo = sdriver.init(SERVO_PIN)
    sdriver.start(servo, 0.5)
    time.sleep(1)
    sdriver.stop(servo)
    return

"""
Moves diverter down.
"""
def down():
    GPIO.setmode(GPIO.BCM)
    servo = sdriver.init(SERVO_PIN)
    sdriver.start(servo, 1.0)
    time.sleep(1)
    sdriver.stop(servo)
    return
