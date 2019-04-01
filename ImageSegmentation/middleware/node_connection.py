import socket
import threading


class NodeConnection(object):

    def __init__(self, address, port):
        self._address = address
        self._port = port
        self._socket = None

    def connect(self):
        self._socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._socket.connect((self._address, self._port))

    def send_json(self, json_object):
        threading.Thread(target=self.__send_json_th__, args=(json_object, )).start()

    def __send_json_th__(self, json_object):

        enc_json_object = json_object.encode('utf-8')
        chunk_size = 4096
        total_sent = 0

        while total_sent < len(enc_json_object):
            sent = self._socket.send(enc_json_object[total_sent:])
            if sent == 0:
                raise (RuntimeError, "Error sending to Java")
            total_sent = total_sent + sent

    def send_bytes(self, data_bytes):
        pass
