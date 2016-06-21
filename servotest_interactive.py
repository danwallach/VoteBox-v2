"""
Sets servomotor angle from user input.

Example usage:
    python servotest_interactive.py

Author: Jerry Lue
"""

import RPi.GPIO as GPIO
import rpi_servodriver as sdriver
import sys

SERVO_PIN = 18


def main():
    GPIO.setmode(GPIO.BCM)
    servo = sdriver.init(SERVO_PIN)
    sdriver.start(servo, 0.5)
    while True:
        try:
            angle = input("Enter servo angle (0.0-1.0): ")
            sdriver.move(servo, angle)
        except KeyboardInterrupt:
            sdriver.stop(servo)
            GPIO.cleanup()
            sys.exit()
    return

if __name__ == "__main__":
    main()
