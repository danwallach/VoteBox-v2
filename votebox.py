#!/usr/bin/env python

import threading
import time

import config
import status_server
import take_in


def main():
    broadcast_status = threading.Thread(target=status_server.serve)
    broadcast_status.daemon = True
    broadcast_status.start()

    take_in.main()
    config.httpd.socket.close()

if __name__ == '__main__':
    main()
