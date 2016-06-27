# VoteBox version 2

An improved version of the original [Vote Box](https://github.com/danwallach/VoteBox-v1) created for the STAR-Vote project.

## Directories
*   `box-schematics` CAD schematics of mechanical components
*   `circuitdiagram` Electronic circuit diagrams
*   `scan-src` Source files for barcode scanner module

## Files
### Core Programs
*   `take_in.py` Main paper intake and sorting routine
*   `diverter.py` Diverter control library
*   `rpi_servodriver.py` Servomotor control library
*   `scan` Barcode scanner program

### Testing Programs
*   `servotest.py` Servomotor library test program
*   `servotest_interactive.py` Interactive servo test program

### Documentation
*   `network-notes.md` Documentation of Epson DS-510 driver hacking

### Miscellaneous
*   `ping_slack` Script that sends device IP to Slack chat
