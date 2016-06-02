import RPi.GPIO as GPIO

# These constants are calibrated for the Futaba S3003 servo.
PWM_FREQ = 40.0 # pulse trigger frequency
MIN_PULSE = 0.4 # pulse ms fully CW
MAX_PULSE = 2.2 # pulse ms fully CCW

# Initialize servo PWM instance.
# Parameters:
#	pin: GPIO pin (BCM) connected to servo control
#	freq: PWM frequency
# Returns:
#	Servo PWM instance 
def init(pin):
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(pin, GPIO.OUT)
	return GPIO.PWM(pin, PWM_FREQ)
	
# Start servo.
# Parameters:
#	servo: servo PWM instance
#	angle: servo rotation. 0 = full CW, 1 = full CCW
def start(servo, angle):
	pulse_width = (MAX_PULSE - MIN_PULSE) * angle + MIN_PULSE
	dc = pulse_width / (1000.0 / PWM_FREQ)
	servo.start(dc)
	return
	
# Change servo rotation.
# Parameters:
#	servo: running servo PWM instance
#	angle: servo rotation. 0 = full CW, 1 = full CCW
def move(servo, angle):
	pulse_width = (MAX_PULSE - MIN_PULSE) * angle + MIN_PULSE
	dc = pulse_width / (1000.0 / PWM_FREQ)
	servo.ChangeDutyCycle(dc)
	return
	
# Stop servo.
# Parameters:
#	servo: running servo PWM instance
def stop(servo):
	servo.stop()
	return