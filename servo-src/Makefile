diverter: diverter.o servolib.o
	gcc diverter.o servolib.o -o diverter -lwiringPi

servotest: servotest.o servolib.o
	gcc servolib.o servotest.o -o servotest -lwiringPi

servolib: servolib.c
	gcc -c servolib.c -o servolib.o -lwiringPi
	ar rcs servolib.a servolib.o

clean:
	rm -f servolib servotest servolib.o servolib.a servotest.o diverter diverter.o
