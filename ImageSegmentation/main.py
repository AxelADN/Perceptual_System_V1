import socket

from nodes.dorsal_visual_cortex import DorsalVisualCortex

hostname = socket.gethostbyname(socket.gethostname())
portNum = 10000
print(hostname)

DorsalVisualCortex(hostname, portNum).init()

