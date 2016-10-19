# Ballot box driver, Java edition 1.9

Driver application for the STAR-Vote ballot box, written in Java for the Raspberry Pi.

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

*Update 9/28/2016*: The cause of the unreliable PWM behavior may be due to dynamic
PWM clock scaling specific to certain conditions on the Pi3 (possibly HDMI). To
resolve, run pi-blaster in PCM mode:

`./pi-blaster --pcm`

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
them in the directory `build/install/ballotbox`. Three variants of the
application are installed:

* `bin/ballotbox`: Networked to the STAR-Vote protocol and displays status
messages via a web-based UI. The UI itself must be downloaded and hosted
separately (see Votebox-Display).
* `bin/ballotboxsw`: Non-networked and displays status messages via Swing.
* `bin/ballotboxfx`: **Experimental.** Displays status messages via JavaFX.
Note that JavaFX is not included in the ARM JDK distribution, so this cannot
actually be run on the RPi.

**Important**: from the command line, the application must be started with
the working directory at the installation root. This is currently necessary
due to design flaws in legacy STAR-Vote code. For example, to start
`ballotboxsw`, use

~~~
cd build/install/ballotbox
bin/ballotboxsw
~~~

### Running from remote session
If running the Swing-based variant over SSH, the X11 display must be set to
the host's physical display. This can be done by setting the `DISPLAY`
environment variable:

`export DISPLAY=:0.0`


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
