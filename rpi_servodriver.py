import RPi.GPIO as GPIO

# These constants are calibrated for the Futaba S3003 servo.
PWM_FREQ = 50.0 # pulse trigger frequency
MIN_PULSE = 0.4 # pulse ms fully CW
MAX_PULSE = 2.2 # pulse ms fully CCW

def init(pin):
    """
    Initialize servo PWM instance.
    Parameters:
        pin: GPIO pin (BCM) connected to servo control
        freq: PWM frequency
    Returns:
        Servo PWM instance 
    """
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(pin, GPIO.OUT)
    return GPIO.PWM(pin, PWM_FREQ)
	
def start(servo, angle):
    """
    Start servo.
    Parameters:
        servo: servo PWM instance
        angle: servo rotation. 0 = full CW, 1 = full CCW
    """
    pulse_width = (MAX_PULSE - MIN_PULSE) * angle + MIN_PULSE
    dc = pulse_width / (10.0 / PWM_FREQ)
    servo.start(dc)
    return
	
def move(servo, angle):
    """
    Change servo rotation.
    Parameters:
        servo: running servo PWM instance
        angle: servo rotation. 0 = full CW, 1 = full CCW
    """
    pulse_width = (MAX_PULSE - MIN_PULSE) * angle + MIN_PULSE
    dc = pulse_width / (10.0 / PWM_FREQ)
    servo.ChangeDutyCycle(dc)
    return
	
def stop(servo):
    """
    Stop servo.
    Parameters:
        servo: running servo PWM instance
    """
    servo.stop()
    return
