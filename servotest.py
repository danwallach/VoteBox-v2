import RPi.GPIO as GPIO
#import take_in
import rpi_servodriver as sdriver
import time

SERVO_PIN = 18

def test():
    GPIO.setmode(GPIO.BCM)

    servo = sdriver.init(SERVO_PIN)
    sdriver.start(servo, 0.5)
    time.sleep(1)
    sdriver.move(servo, 0.2)
    time.sleep(1)
    sdriver.move(servo, 1.0)
    time.sleep(1)
    sdriver.move(servo, 0.5)
    time.sleep(1)
    sdriver.stop(servo)
    GPIO.cleanup()
    return

test() 
