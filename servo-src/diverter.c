
#include "servolib.h"

void s3003_up() {
    wiringPiSetupGpio();
    servo_init();
    s3003_move(0);
}

void s3003_down() {
    wiringPiSetupGpio();
    servo_init();
    s3003_move(70);
}

int main(int argc, char *argv[]) {
    const char* usagestr = "Usage: diverter up/down\n";
    switch (argc) {
        case 2:
            if (!strcmp(argv[1], "up")) {
                s3003_up();
            } else if (!strcmp(argv[1], "down")) {
                s3003_down();
            } else {
                printf(usagestr);
                return 1;
            }
            return 0;
        default:
            printf(usagestr);
            return 1;
    }
}
