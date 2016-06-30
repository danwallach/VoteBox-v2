/*
 * servolib
 *
 * Reimplementation of the servo library in C using wiringPi. This
 * approach uses the hardware PWM feature available on GPIO 18 of the
 * Raspberry Pi, reducing GPU usage and increasing timing accuracy over
 * the software based solution.
 *
 * Author:
 * Jerry Lue
 */
#include "servolib.h"

// #define PWM_PIN 18

void servo_init() {
	pinMode(PWM_PIN, PWM_OUTPUT);
	pwmSetMode(PWM_MODE_MS);
	pwmSetClock(375);
	pwmSetRange(1024);
}

void servo_move(int pos) {
	pwmWrite(PWM_PIN, pos);
}

/*
 * Move a HD 6001HB servo.
 *
 * For the 6001HB servo, raw input ranges from 40 (0.8 ms) to 120
 * (2.35 ms), which translates to a range of 0-80 for the input here.
 * Values outside this range may be used but response is not guaranteed.
 */
void hd6001_move(int pos) {
    servo_move(pos + 40);
}

void servo_interactive() {
    while (1) {
        char input[6];
        int value;
        printf("Enter servo value (0-1024): ");
        fgets(input, 6, stdin);
        value = atoi(input);
        servo_move(value);
    }
}
