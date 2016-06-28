#include <stdio.h>
#include <stdlib.h>

#include <wiringPi.h>

#define PWM_PIN 18

int servo_init() {
	pinMode(PWM_PIN, PWM_OUTPUT);
	pwmSetMode(PWM_MODE_MS);
	pwmSetClock(1920);
	pwmSetRange(200);
	return 0;
}

void servo_move(int pos) {
	pwmWrite(PWM_PIN, pos);
}

void servo_interactive() {
	char input[6];
	int value;
	fgets(input, 6, stdin);
	value = atoi(input);
	servo_move(value);
}

int main() {
	wiringPiSetupGpio();
	servo_init();
	servo_interactive();
}