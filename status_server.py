#!/usr/bin/env python

# import SocketServer
# from BaseHTTPServer import BaseHTTPRequestHandler
import threading
import os
import sys

import config

def serve():
    if os.geteuid() != 0:
        os.execvp("sudo", ["sudo"] + sys.argv)

    # httpd = SocketServer.TCPServer(('', 80), StatusRequestHandler)
    try:
        config.httpd.serve_forever()
    except KeyboardInterrupt:
        config.httpd.socket.close()

def test():
    try:
        broadcast_status = threading.Thread(target=serve)
        broadcast_status.daemon = True
        broadcast_status.start()

        while True:
            config.status = raw_input('Status to broacast: ')

    except KeyboardInterrupt:
        config.httpd.socket.close()


def main():
    try:
        broadcast_status = threading.Thread(target=serve)
        broadcast_status.daemon = True
        broadcast_status.start()

        take_in.main()


    except KeyboardInterrupt:
        config.httpd.socket.close()
    

# def main():
    # try:
        # httpd = SocketServer.TCPServer(("", 80), StatusRequestHandler)
        # httpd.serve_forever()

    # except KeyboardInterrupt:
        # httpd.socket.close()

if __name__ == '__main__':
    main()
