from nodes.image_socket import ImageServer
from processes.base_processing import SegmentationProcess


class DorsalVisualCortex(object):

    def __init__(self, hostname, port):
        self._hostname = hostname
        self._port = port

    def init(self):
        command = SegmentationProcess("Dorsal Visual Cortex", "", 20000)
        ImageServer(self._hostname, self._port, command).listen()
