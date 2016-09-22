/**
 * Provides classes for interfacing with hardware devices used by the ballot box on the Raspberry Pi 3 platform.
 *
 * These include:
 *
 * - Pulse width modulation (PWM) control via [pi-blaster](https://github.com/sarfata/pi-blaster) or the RPi.GPIO
 *   Python library.
 * - Servomotor control.
 * - Barcode scanner control via a bundled C executable (`scan`).
 *
 * @author luejerry
 */
package edu.rice.starvote.ballotbox.drivers;