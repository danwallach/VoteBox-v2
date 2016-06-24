/*
 * Usage:
 *  scan [timeout in seconds]
 * 
 * Examples:
 *  Scan code, no timeout
 *      sudo ./scan
 *  Scan code, 5 second timeout
 *      sudo ./scan 5
 * 
 * Description:
 *  Scans a code using the barcode scanner on pin 25. Continues
 *  until code is successfully read or an error occurs. Valid code
 *  is printed to standard output.
 * 
 * Notes:
 *  To compile, include argument -lwiringPi, e.g.
 *      gcc scan.c -o scan -lwiringPi
 *
 *  Due to requirements of wiringPi, this program must be run as root.
 *
 * Author:
 *  Jerry Lue
 */
#include <stdio.h>
#include <poll.h>
#include <unistd.h>
#include <errno.h>
#include <signal.h>
#include <wiringPi.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <linux/input.h>
#include <limits.h>

#define MAXCODE 64 // scan buffer size

// key event device of scanner
char *device = "/dev/input/by-id/usb-WIT_Electron_Company_WIT_122-UFS_V2.03-event-kbd";

// maps linux keycodes to ascii chars
char keymap(int code) {
    switch (code) {
        case KEY_1: return '1';
        case KEY_2: return '2';
        case KEY_3: return '3';
        case KEY_4: return '4';
        case KEY_5: return '5';
        case KEY_6: return '6';
        case KEY_7: return '7';
        case KEY_8: return '8';
        case KEY_9: return '9';
        case KEY_0: return '0';
        case KEY_Q: return 'Q';
        case KEY_W: return 'W';
        case KEY_E: return 'E';
        case KEY_R: return 'R';
        case KEY_T: return 'T';
        case KEY_Y: return 'Y';
        case KEY_U: return 'U';
        case KEY_I: return 'I';
        case KEY_O: return 'O';
        case KEY_P: return 'P';
        case KEY_A: return 'A';
        case KEY_S: return 'S';
        case KEY_D: return 'D';
        case KEY_F: return 'F';
        case KEY_G: return 'G';
        case KEY_H: return 'H';
        case KEY_J: return 'J';
        case KEY_K: return 'K';
        case KEY_L: return 'L';
        case KEY_Z: return 'Z';
        case KEY_X: return 'X';
        case KEY_C: return 'C';
        case KEY_V: return 'V';
        case KEY_B: return 'B';
        case KEY_N: return 'N';
        case KEY_M: return 'M';
        default: return '_';
    }
}

int scan(const int tries) { 
    int scanfd = open(device, O_RDONLY); 
    FILE *scanst = fdopen(scanfd, "r");
    struct pollfd mypoll = { scanfd, POLLIN|POLLPRI };
    char code[MAXCODE];
    int err = 0, readstatus = 0, codecounter = 0, trycount = 0;
    struct input_event keyevent;

    ioctl(scanfd, EVIOCGRAB, (void *) 1); // get exclusive access to scanner
    wiringPiSetupGpio(); // BCM pin numbering
    pinMode(25, OUTPUT);
    // Loop until code read or error occurs
    while (trycount < tries && !err) {
        // Turn on scanner
        digitalWrite(25, HIGH);
        // Wait for scan for 2.5s before restarting scanner
        if(err = poll(&mypoll, 1, 800)) {
            while ((readstatus = read(scanfd, &keyevent, sizeof(keyevent))) >= 0) {
                #ifdef DEBUG
                printf("Read returned: %d; ", readstatus);
                printf("Event type: %u; ", keyevent.type);
                printf("Event code: %u; ", keyevent.code);
                printf("Event value: %u\n", keyevent.value);
                #endif
                if (keyevent.type == EV_KEY && keyevent.value == 1) { // key press
                    if (codecounter == MAXCODE - 1 || keyevent.code == KEY_ENTER) {
                        // enter key signals end of scan 
                        code[codecounter] = '\0';
                        break;
                    }
                    // convert keycode to ascii char
                    code[codecounter] = keymap(keyevent.code);
                    if (code[codecounter] != '_') { // strip unrecognized characters
                        codecounter++;
                    }
                }
            }
            if (readstatus < sizeof(keyevent)) {
                fprintf(stderr, "Error occurred scanning: %s", strerror(errno));
                digitalWrite(25, LOW);
                return -1;
            }
            printf("%s\n", code);
        } else if (err < 0) {
            fprintf(stderr, "Error occurred polling: %s", strerror(errno));
            digitalWrite(25, LOW);
            return -1;
        }
        //else {
        //    printf("No code scanned\n");
        //}

        // Restart scanner. Wait 50ms to allow it to reset
        digitalWrite(25, LOW);
        usleep(200000);
        trycount ++;
    }
    return 0;
}

int main(int argc, char *argv[]) {
    switch (argc) {
        case 1:
            return scan(INT_MAX);
        case 2:
            return scan(atoi(argv[1]));
        default:
            return scan(INT_MAX);
    }
}
