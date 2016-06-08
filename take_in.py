import RPi.GPIO as GPIO
import time
import logging

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

    return pwm

def take_in(pwm):
    """Take in all ballots."""
    tray_empty = False
    timeout = time.time() + 1   # One second from now.

    logging.info("Taking in a sheet...")

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
        main()
    except KeyboardInterrupt:
        logging.info('Keyboard interrupt.')
        pwm = setup()
        clean_up(pwm)
