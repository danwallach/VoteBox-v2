import time

def start(pin, freq, dc):
    # print str(pin) + " " + str(freq) + " " + str(dc)
    print_args([pin, freq, dc])


def set_dc(pin, dc):
    print_args([pin, dc])


def set_freq(pin, freq):
    print_args([pin, freq])


def print_args(args):
    for e in args:
        print str(e)
