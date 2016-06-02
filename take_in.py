import RPi.GPIO as GPIO

import time

"""
Takes in all ballots placed in tray. 

Driven by the paper feeder of the HP Deskjet 1000 J110.
"""

# Output pins.
MOTOR_ENABLE = 17
MOTOR_FORWARD = 27
MOTOR_BACKWARD = 22

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
    timeout = time.time() + 1   # One seconds from now.

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
    if not tray_empty:
        slow_motor()
    while not before_halfway:
        GPIO.output(MOTOR_FORWARD, False)
        GPIO.output(MOTOR_BACKWARD, True)

        before_halfway = GPIO.input(HALFWAY_TRIGGER) # True if trigger depressed.
    
    time.sleep(.1)
    if not tray_empty:
        take_in()

def slow_motor():
    """Slow down motor to make accept or reject decision."""
    GPIO.output(MOTOR_FORWARD, False)
    p = GPIO.PWM(MOTOR_BACKWARD, 120)
    p.start(40)
    time.sleep(3)
    p.stop()



def clean_up():
    """Roll backward to open tray, then shut down pins."""
    GPIO.output(MOTOR_FORWARD, False)
    GPIO.output(MOTOR_BACKWARD, True)
    time.sleep(1)

    GPIO.cleanup()

# Main execution.
setup()
take_in()
clean_up()

