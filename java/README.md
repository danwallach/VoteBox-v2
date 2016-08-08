# Ballot box driver, Java edition

Implementation of the ballot box driver software in Java. The web client for
the screen display and sound is not included; it is available at
[VoteBox-Display](https://github.com/luejerry/VoteBox-Display).

## Requirements
- Raspberry Pi 3
- Java 1.8
- Gradle
- WiringPi
- Pi-blaster

## Run
Run `./gradlew run` as root.

## Deploy
Running directly through Gradle is convenient, but slow to start. To package
the application in a self-contained archive package:

`./gradlew distTar`

The resulting tar package is placed in `build/distributions`. Unpack it to the
desired directory and run the included startup script to start the program.