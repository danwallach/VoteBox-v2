- Added "changemac" script in /etc/network/if-up.d to change mac address on interface online

# Compiling Epson scanner binaries for ARM

Epson does not provide precompiled ARM Linux driver binaries for the DS-510 scanner. It does, however, provide sources
available [here](http://support.epson.net/linux/src/scanner/imagescanv3/debian/imagescan_3.16.0.orig.tar.gz). We must
compile it ourselves to use the manufacturer drivers on the RPi. Note that according to SANE documentation, it does
have partial support for the DS-510 on its own, so that is a possible fallback in case we can't get these drivers
working.


## Compile epson imagescan (utsushi) drivers on the RPi

1. download and extract source tar:
    ```
    curl -LOC http://support.epson.net/linux/src/scanner/imagescanv3/debian/imagescan_3.16.0.orig.tar.gz
    tar -zxf imagescan_3.16.0.orig.tar.gz
    ```
    
2. install libboost-dev-all from aptitude
3. run configure script, specifying boost library location (`/usr/lib/arm-linux-gnueabihf`)

    `./configure --with-boost-libdir=/usr/lib/arm-linux-gnueabihf`

4. make

    `make`

This step failed with compiler error due to insufficient memory. Error persisted even after increasing swap space
to 8 GB. Cross compiling on a more powerful system appears to be necessary here.


## Cross-compiling utsushi drivers from x86_64 Ubuntu 16.04 machine, take one

On target machine (rpi):

1. download boost c++ libraries

    `sudo apt-get install libboost-all-dev`

2. archive the installed binaries and transfer to build machine

    `tar -zcf lib.tar.gz /usr/lib/arm-linux-gnueabihf`

On build machine (x86-ubuntu):

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

7. make

    `make`

8. archive the directory and transfer to target machine

    `tar -zcf arm-utsushi.tar.gz utsushi-0.16.0`


On target machine:

1. reconfigure to target

    `./configure --with-boost-libdir=/usr/lib/arm-linux-gnueabihf`

2. make check

    `make check`

   * This step failed with binaries in utsushi/connexions/.lib failing tests. Easy solution is to just recompile these
natively, since these are not a problem for the pi
    ```
    cd connexions
    make
    ```

   * Rerun make check
    ```
    cd ..
    make check
    ```

   * Now errors are generated due to missing dependencies used by the testsuite. Install them

    `apt-get install tesseract-ocr graphicsmagick doxygen graphviz`

   * The Makefile in `./filters/tests` needs to be updated to point to graphicsmagick. Do not rerun configure; it will
force a recompile. Change the `CONVERT=` to

    `CONVERT=gm convert`

   * Rerun make check

    `make check`


At this point, it became evident that make had used the built-in gcc rather than the cross-compiler, possibly due to
autodetection of x86_64 libraries. I decided to abandon the cross-compiling approach and go with full environment
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

On host machine (x86_64 ubuntu):

1. Get the scratchbox2/qemu RPi VM box

    `git clone https://github.com/luejerry/raspberry-devbox`

2. Set it up using the instructions in README
    ```
    vagrant up
    vagrant ssh
    ```

3. At this stage, a problem with mismatching locales was encountered ("could not set locale"). To fix, generate locales
for all en_US languages

    `sb2 -eR dpkg-reconfigure locales`

   * Select (spacebar) all locales starting with en_US
   * Choose "en_US" as default locale

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

2. Install the necessary dependencies in install-deps.sh

    `sudo ./install-deps.sh`

3. Run make check

    `make check`

4. Run make install

    `sudo make install`

   * This will install to the prefix folder specified when configure was ran, in this case ~/deploy. A driver file is
also installed to `/etc/udev/rules.d`.


# Useful tricks

- To see exactly which libraries are being loaded (and which ones are failing to load), set the environment variable LD_DEBUG:
    ```
    export LD_DEBUG=files	# displays library files
    export LD_DEBUG=bindings	# displays symbol bindings
    export LD_DEBUG=libs	# displays lib search paths
    export LD_DEBUG=help	# displays other possible options
    ```
