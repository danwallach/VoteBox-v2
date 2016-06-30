
#include <stdio.h>
#include <stdlib.h>
#include <wiringPi.h>

#define PWM_PIN 18

void servo_init();
void servo_move(int pos);
void hd6001_move(int pos);
void s3003_move(int pos);
void servo_interactive();
