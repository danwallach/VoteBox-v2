# scan: barcode scanner program

## Compiling
Run `make`. The WiringPi library must be installed.

## Usage
Before running, ensure the barcode scanner is activated by HIGH signal on BCM
pin 25. Run `scan` as root.

* `sudo ./scan` Scan until valid code is read.
* `sudo ./scan 5` Scan until code read or 5 seconds have elapsed.

On successful code read, the program prints the code followed by a newline to
`stdout` and exits. Nothing is printed if no code is read.

## Notes/Todo
As of 6/23/2016, `scan` can only process case-insensitive alphanumeric
characters. Capital letters read are output as lowercase and all symbols are
replaced with an underscore (`_`).