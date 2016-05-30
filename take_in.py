import RPi.GPIO as GPIO

import time

"""
Takes in all ballots placed in tray. 

Driven by the paper feeder of the HP Deskjet 1000 J110.
"""

# Output pins.
MOTOR_ENABLE = 2
MOTOR_FORWARD = 3
MOTOR_BACKWARD = 4

# Input pins.
HALFWAY_TRIGGER = 23

def setup():
    """Set up the GPIO pins as input and output."""
    print "Running Ballot Diverter V2."

    print "Setting up pins..."

    GPIO.setmode(GPIO.BCM)

    GPIO.setup(MOTOR_ENABLE, GPIO.OUT)
    GPIO.setup(MOTOR_FORWARD, GPIO.OUT)
    GPIO.setup(MOTOR_BACKWARD, GPIO.OUT)

    GPIO.setup(HALFWAY_TRIGGER, GPIO.IN, pull_up_down=GPIO.PUD_UP)

def take_in():
    """Take in all ballots."""
    tray_empty = False
    timeout = time.time() + 1   # Three seconds from now.

    print "Taking in a sheet..."

    print "Rolling forward..."
    before_halfway = GPIO.input(HALFWAY_TRIGGER) # True if trigger depressed.
    while before_halfway:
        GPIO.output(MOTOR_ENABLE, True)
        GPIO.output(MOTOR_FORWARD, True)
        GPIO.output(MOTOR_BACKWARD, False)

        if time.time() > timeout:
            print "Tray is empty."
            tray_empty = True
            break

        before_halfway = GPIO.input(HALFWAY_TRIGGER) # True if trigger depressed.

    print "Rolling backward..."
    time.sleep(.1)
    while not before_halfway:
        GPIO.output(MOTOR_FORWARD, False)
        GPIO.output(MOTOR_BACKWARD, True)

        before_halfway = GPIO.input(HALFWAY_TRIGGER) # True if trigger depressed.
    
    if not tray_empty:
        take_in()

# Main execution.
setup()
take_in()

GPIO.cleanup()
