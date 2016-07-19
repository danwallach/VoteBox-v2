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
            self.send_header('Access-Control-Allow-Origin', '*')
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(status)
        
        self.send_response(200)

httpd = SocketServer.TCPServer(('', 80), StatusRequestHandler)
