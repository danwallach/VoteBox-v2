
# Compiling Epson scanner binaries for ARM

Epson does not provide precompiled ARM Linux driver binaries for the DS-510 scanner. It does, however, provide sources
available [here](http://support.epson.net/linux/src/scanner/imagescanv3/debian/imagescan_3.16.0.orig.tar.gz). We must
compile it ourselves to use the manufacturer drivers on the RPi.

## Compile epson imagescan (utsushi) drivers on the RPi

1. download and extract source tar:
    ```
    curl -LOC http://support.epson.net/linux/src/scanner/imagescanv3/debian/imagescan_3.16.0.orig.tar.gz
    tar -zxf imagescan_3.16.0.orig.tar.gz
    ```
    
2. install `libboost-dev-all`
3. run configure script, specifying boost library location (`/usr/lib/arm-linux-gnueabihf`)

    `./configure --with-boost-libdir=/usr/lib/arm-linux-gnueabihf`

4. `make`

This step failed with compiler error due to insufficient memory. Error persisted even after increasing swap space
to 8 GB. Cross compiling on a more powerful system appears to be necessary here.


## Cross-compiling utsushi drivers from amd64 Ubuntu 16.04 machine, take one

On target machine (rpi):

1. download boost c++ libraries

    `sudo apt-get install libboost-all-dev`

2. archive the installed binaries and transfer to build machine

    `tar -zcf lib.tar.gz /usr/lib/arm-linux-gnueabihf`

On build machine (amd64-ubuntu):

1. download and extract driver source as above
2. download c++ boost libraries

    `sudo apt-get install libboost-all-dev`

3. extract target boost binaries

    `tar -zxf lib.tar.gz`

4. download RPi cross compiling toolchain off github

    `git clone https://github.com/raspberrypi/tools`

5. add toolchain binaries for build system to PATH in ~/.bashrc

    `export PATH=$PATH:~/tools/arm-bcm2708/gcc-linaro-arm-linux-gnueabihf-raspbian-x64/bin`

6. run configure script, specifying target boost library location and cross-compile host

    `./configure --with-boost-libdir=./lib/arm-linux-gnueabihf --build=x86_64-linux-gnu --host=arm-linux-gnueabihf`

7. `make`

8. archive the directory and transfer to target machine


On target machine:

1. reconfigure to target

    `./configure --with-boost-libdir=/usr/lib/arm-linux-gnueabihf`

2. `make check`

   * This step failed with binaries in utsushi/connexions/.lib failing tests. Easy solution is to just recompile these
natively, since these are not a problem for the pi
    ```
    cd connexions
    make
    ```

   * Rerun `make check` in project root 

   * Now errors are generated due to missing dependencies used by the testsuite. Install them


    `apt-get install tesseract-ocr graphicsmagick doxygen graphviz`

   * The Makefile in `./filters/tests` needs to be updated to point to graphicsmagick. Do not rerun configure; it will
force a recompile. Change the `CONVERT=` to

    `CONVERT=gm convert`

   * Rerun `make check`


At this point, it became evident that make had used the built-in gcc rather than the cross-compiler, possibly due to
autodetection of x86\_64 libraries. I decided to abandon the cross-compiling approach and go with full environment
emulation instead, since the road of cross-compiling the entire dependency tree leads only to horrors beyond mortal
comprehension.


## Compiling utsushi drivers with ScratchBox2 and qEmu

Fortunately, another brave soul had set up a VM for emulated compilation with ScratchBox2 and qEmu and posted it to
[Github](https://github.com/nickhutchinson/raspberry-devbox). Unfortunately, it was quite outdated, using the previous
distribution of Raspbian (Wheezy) and an old cross compilation toolchain. Trying to compile the drivers in emulation
mode was met with many errors and warnings of deprecated assembly output (?!). After an afternoon of modifications to
the VM provisioning scripts, I was able to generate a VM using the current Raspbian Jessie distribution and cross
compilation toolchain, which I have placed in [this Github repository](https://github.com/luejerry/raspberry-devbox).

The emulated compilation approach uses ScratchBox2 and qEmu to emulate a Raspbian chroot, itself inside an Ubuntu 12
VM running in Vagrant/Virtualbox on the host system. Fully emulated compilation is extremely slow, a small price to
pay for one's sanity.

On host machine (amd64 ubuntu):

1. Get the scratchbox2/qemu RPi VM box

    `git clone https://github.com/luejerry/raspberry-devbox`

2. Set it up using the instructions in README
    ```
    vagrant up
    vagrant ssh
    ```

3. At this stage, a problem with mismatching locales was encountered ("could not set locale"). To fix, generate locales
for all en\_US languages

    `sb2 -eR dpkg-reconfigure locales`

   * Select (spacebar) all locales starting with `en_US`
   * Choose `en_US` as default locale

4. Download and extract driver source as above
5. Download boost libraries to the chroot

    `sb2 -eR apt-get install libboost-all-dev`

6. Run configure in emulation mode, specifying a staging directory

    `sb2 -e ./configure --with-boost-libdir=/usr/lib/arm-linux-gnueabihf --prefix=$HOME/deploy`

7. Run make in emulation mode (takes a long time)

    `sb2 -e make`

8. Run make check in emulation mode

    `sb2 -e make check`

   * Could not get past this point on the host machine due to test programs segfaulting when running under qEmu. Since
the hard part has been built, we can copy the build products to the Pi and run the test there, being careful not to
trigger a recompile

9. Archive the utsushi folder and send to Pi

On target machine (arm rpi):

1. Extract the utsushi folder to the same absolute path as on host, or symlink to it

    `sudo ln -s /home/pi /home/vagrant`

2. Install the necessary dependencies in `install-deps.sh` (we installed by hand to avoid installing unnecessary automake
tools)

    `sudo ./install-deps.sh`

3. `make check`

4. `sudo make install`

   * This will install to the prefix folder specified when configure was ran, in this case `~/deploy`. A driver file is
also installed to `/etc/udev/rules.d`.


# Compiling SANE binaries for ARM

[SANE](www.sane-project.org) introduced partial support for the DS-510 in version 1.0.25 with the introduction of the
`epsonds` module. Although the SANE frontend can be used with Utsushi, the backend driver is completely independent and
gives us an alternative option for using the scanner on the Pi.

## Precompiled binaries

The latest precompiled SANE packages on the Raspbian Jessie repository are version 1.0.24, which is outdated and does not
support the DS-510. We tried installing the latest stable release (1.0.25) from the experimental Debian Stretch repository,
but encountered a known bug in which the scanner would only accept one command successfully before failing with an IO error.
Others had reported this bug fixed in the latest unstable snapshots, which are only available as source and thus have to be
compiled. Fortunately, the RPi 3 is more than capable of compiling the necessary modules natively without the need for any
cross compilation insanity.

## Compiling on RPi

### Compiling backend driver

The backend driver module `epsonds` must be compiled and installed before the frontend is installed.

1. Download the SANE development sources from Github.

    `git clone git://git.debian.org/sane/sane-backends.git`

   - We received `sane-backends` on commit `e7100e275d58d67b903e883e7a02e45d18f81ef7`.

2. Make sure `libusb-dev` is installed.

3. Run configure in `sane-backends`, specifying that only to compile the driver module we need

    `./configure BACKENDS="epsonds"`

4. `make`

5. `sudo make install`


### Compiling frontend driver

1. Download the SANE development sources from Github.

    `git clone git://git.debian.org/sane/sane-frontends.git`

2. `./configure`

3. `make`

4. `make install`

5. Add `/usr/local/lib` to the library search path by adding it to `ld.so.conf` and running `ldconfig` as root. Alternatively,
set the environment variable `LD_LIBRARY_PATH=/usr/local/lib` in execution

    `LD_LIBRARY_PATH=/usr/local/lib scanimage -L`


# Useful tricks

- To see exactly which libraries are being loaded (and which ones are failing to load), set the environment variable `LD_DEBUG`:
    ```
    export LD_DEBUG=files	# displays library files
    export LD_DEBUG=bindings	# displays symbol bindings
    export LD_DEBUG=libs	# displays lib search paths
    export LD_DEBUG=help	# displays other possible options
    ```

- To write both stdout and stderr to a file while still printing to the console, use

    `<command> 2>&1 | tee <outfile>`



# Temporary notes

- _Make sure `pkg-config` is installed in the RPi chroot!_ It comes installed on the normal Raspbian distro but not when
using `debootstrap`. Epson driver `configure` will silently fail to detect `libudev` and `libusb` if this is not installed!
