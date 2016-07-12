# VoteBox devices state: (waiting, pending, accept, reject).
status = None

def changeStatus(new_status):
    global status 
    status = new_status
