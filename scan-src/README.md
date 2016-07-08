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

## Notes
Only printable ASCII characters and the space character are supported. Should
be good for Code 128 and below.
