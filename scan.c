/*
 * Note: to compile, include argument -lwiringPi, e.g.
 *  gcc scan.c -o scan -lwiringPi
 *
 * Due to requirements of wiringPi, this program must be run as root.
 *
 * Scans a code using the barcode scanner on pin 25. Continues
 * until code is successfully read or an error occurs. Valid code
 * is printed to standard output.
 */
#include <stdio.h>
#include <poll.h>
#include <unistd.h>
#include <errno.h>
#include <signal.h>
#include <wiringPi.h>

int main(void) {
    struct pollfd mypoll = { STDIN_FILENO, POLLIN|POLLPRI };
    char code[32];
    int err = 0;
    
    wiringPiSetupGpio(); // BCM pin numbering

    pinMode(25, OUTPUT);
    // Loop until code read or error occurs
    while (!err) {
        // Turn on scanner
        digitalWrite(25, HIGH);
        // Wait for scan for 2.5s before restarting scanner
        if(err = poll(&mypoll, 1, 2500)) {
            scanf("%31s", code);
            printf("%s", code);
        } else if (err < 0) {
            fprintf(stderr, "Error occurred reading: %s", strerror(errno));
        } 
        //else {
        //    printf("No code scanned\n");
        //}

        // Restart scanner. Wait 100ms to allow it to reset
        digitalWrite(25, LOW);
        usleep(100000);
    }
    return 0;
}
