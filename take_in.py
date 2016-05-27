import RPIO
from RPIO import PWM

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
HALFWAY_TRIGGER = 14

def setup():
    """Set up the GPIO pins as input and output."""
    print "Running Ballot Diverter V2."

    print "Setting up pins..."

    RPIO.setmode(RPIO.BCM)

    RPIO.setup(MOTOR_ENABLE, RPIO.OUT)
    RPIO.setup(MOTOR_FORWARD, RPIO.OUT)
    RPIO.setup(MOTOR_BACKWARD, RPIO.OUT)

    RPIO.setup(HALFWAY_TRIGGER, RPIO.IN)

def take_in():
    """Take in all ballots."""
    timeout = time.time() + 3   # Three seconds from now.

    print "Taking in a sheet..."

    print "Rolling forward..."
    after_halfway = RPIO.input(HALFWAY_TRIGGER) # True if trigger depressed.
    while not after_halfway:
        RPIO.output(MOTOR_ENABLE, True)
        RPIO.output(MOTOR_FORWARD, True)
        RPIO.output(MOTOR_BACKWARD, False)

        if time.time() > timeout:
            print "Tray is empty."
            break

# Main execution.
setup()
take_in()

RPIO.cleanup()
