# Ballot box driver, Java edition 1.5

Fully event-driven implementation of the ballot box driver software in Java.
The web client for the screen display and sound is not included; it is
available at [VoteBox-Display](https://github.com/luejerry/VoteBox-Display).

## Requirements
- Raspberry Pi 3
- Java 1.8
- [WiringPi](http://wiringpi.com/)
- [Pi-blaster](https://github.com/sarfata/pi-blaster)

### Note on pi-blaster
Pi-blaster **must** be daemonized and configured with a pulse frequency of 50 Hz before
running this application (refer to its documentation to see how to do this). We have
also noted broken PWM behavior if the daemon is started immediately at boot. This was
resolved by delaying the start of pi-blaster by 30 seconds on boot.

### Root access
The application may be run with normal privileges if the machine is running Raspbian
with Linux kernel version 4.1.7 or newer. Otherwise, the application must be run as
root to allow hardware GPIO access.

## Run
Run `./gradlew run. This should only be used for development testing.

## Install
To build the application locally:

`./gradlew installApp`

This will package the application and all dependencies into JARs and place
them in the directory `build/install/ballotbox`. To run the installed
application, run:

`build/install/ballotbox/bin/ballotbox`

## Deploy
To package the application in a self-contained archive package:

`./gradlew distTar`

The resulting tar package is placed in `build/distributions`. This is the equivalent of
packaging the output of _Install_ in a tar archive.

## Documentation
Generate documentation by running

`./gradlew javadoc`

The resulting JavaDoc is placed under `build/docs`. Source code for this
project is formatted with Markdown and converted to JavaDoc HTML with the
[pegdown-doclet](https://github.com/Abnaxos/pegdown-doclet) plugin, which is
included in the provided Gradle configuration. The IntelliJ IDEA [Pegdown
Doclet plugin](https://plugins.jetbrains.com/plugin/7253?pr=idea) may be
optionally installed to properly render Markdown comments in the Quick
Documentation viewer.

## Source
This software was developed in IntelliJ IDEA and built with Gradle. Opening
this directory (`/java`) as project root in IntelliJ should import everything
in a working state. You may need to manually refresh Gradle for it to download
the necessary dependencies.

The Gradle task `application/run` can be used to run the program as configured
in Gradle. To run it through IntelliJ or manually, the main entry point is via
`edu.rice.starvote.Controller.main()`.

## Known Issues
* During operation, a `ConcurrentModificationException` is sometimes thrown.
This does not appear to interfere with any functionality. It is likely a bug
introduced in version 1.1 of the Pi4J library.

## Todo
- Debugging

## Authors
- luejerry
