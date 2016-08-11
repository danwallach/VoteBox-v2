import socket
import SocketServer
from BaseHTTPServer import BaseHTTPRequestHandler

# VoteBox devices state: (waiting, pending, accept, reject).
status = None

def changeStatus(new_status):
    global status 
    status = new_status

# HTTP Daemon
class StatusRequestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path == '/status':
            self.send_response(200)
            self.send_header('Access-Control-Allow-Origin', '*')
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(status)
        
class TCPServer_nowait(SocketServer.TCPServer):
    def server_bind(self):
        self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.socket.bind(self.server_address)

#httpd = SocketServer.TCPServer(('', 80), StatusRequestHandler)
httpd = TCPServer_nowait(('', 81), StatusRequestHandler)
