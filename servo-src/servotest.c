
#include "servolib.h"

int main(void) {
    wiringPiSetupGpio();
    servo_init();
    servo_interactive();
    return 0;
}
