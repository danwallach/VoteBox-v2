#!/usr/bin/env python

import SocketServer
from BaseHTTPServer import BaseHTTPRequestHandler

import config

class StatusRequestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path == '/status':
            self.send_header('Access-Control-Allow-Origin', '*')
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(config.status)
        
        self.send_response(200)

def main():
    try:
        httpd = SocketServer.TCPServer(("", 80), StatusRequestHandler)
        httpd.serve_forever()

    except KeyboardInterrupt:
        httpd.socket.close()

if __name__ == '__main__':
    main()
