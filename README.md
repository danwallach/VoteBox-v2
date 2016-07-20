# VoteBox version 2

An improved version of the original [Vote Box](https://github.com/danwallach/VoteBox-v1) created for the STAR-Vote project.

## Directories
*   `box-schematics` CAD schematics of mechanical components
*   `circuitdiagram` Electronic circuit diagrams
*   `scan-src` Source files for barcode scanner module
*   `servo-src` Source files for hardware PWM servo module (experimental)

## Files
### Core Programs
*   `votebox.py` Main routine
*   `status_server.py` Ballot box status server for use with web frontend
*   `take_in.py` Paper intake and sorting routine
*   `diverter.py` Diverter control library
*   `rpi_servodriver.py` Servomotor control library
*   `scan` Barcode scanner program

### Configuration Files
*   `config.py` Config options for main program
*   `diverter_config.py` Config options for diverter controller

### Testing Programs
*   `servotest.py` Servomotor library test program
*   `servotest_interactive.py` Interactive servo test program

### Documentation
*   `network-notes.md` Documentation of Epson DS-510 driver hacking
*   `scanner-codes.md` Documentation of Epson ESCI/2 scanner communication protocol

### Miscellaneous
*   `ping_slack` Script that sends device IP to Slack chat
