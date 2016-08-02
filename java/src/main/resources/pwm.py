import RPi.GPIO as GPIO
import time


def start(pin, freq, dc):
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(pin, GPIO.OUT)
    servo = GPIO.PWM(pin, freq)
    servo.start(dc)
    # time.sleep(1)
    # servo.stop()


def set_dc(pin, dc):
    print_args([pin, dc])


def set_freq(pin, freq):
    print_args([pin, freq])


def print_args(args):
    for e in args:
        print str(e)
