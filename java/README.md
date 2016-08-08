# Ballot box driver, Java edition 1.0

Fully event-driven implementation of the ballot box driver software in Java.
The web client for the screen display and sound is not included; it is
available at [VoteBox-Display](https://github.com/luejerry/VoteBox-Display).

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
Note that this program must be run as root because of direct hardware access.

## Documentation
Generate documentation by running

`./gradlew javadoc`

The resulting JavaDoc is placed under `build/docs`. Source code for this
project is formatted with Markdown and converted to JavaDoc HTML with the
[pegdown-doclet](https://github.com/Abnaxos/pegdown-doclet) plugin, which is
included in the provided Gradle configuration.

## Source
This software was developed in IntelliJ IDEA and built with Gradle. Opening
this directory (`/java`) as project root in IntelliJ should import everything
in a working state. You may need to manually refresh Gradle for it to download
the necessary dependencies.

The Gradle task `application/run` can be used to run the program as configured
in Gradle. To run it through IntelliJ or manually, the main entry point is via
`Controller.main()`.

## Todo
- Debugging
- Replace Python-based servomotor control with something more palatable
- Organize stuff into actual subpackages

## Authors
- luejerry