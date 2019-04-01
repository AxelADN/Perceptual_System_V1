# -*- coding: utf-8 -*-


import socket
import threading
import time

from processes.base_processing import SegmentationProcess


class ImageServer(object):
    def __init__(self, host, port, command):
        self.host = host
        self.port = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.sock.bind((self.host, self.port))
        self.command = command

    def listen(self):
        self.sock.listen(5)
        while True:
            print("Esperando conexiones...")
            client, address = self.sock.accept()
            threading.Thread(target=self.listen_to_client, args=(client, address)).start()

    def listen_to_client(self, client, address):
        size = 1024
        total = 0
        ts = int(round(time.time() * 1000))
        image_path = "received/received"+str(ts)+".png"
        recv_image = open(image_path, "wb")

        while True:
            try:

                data = client.recv(size)
                total += len(data)
                recv_image.write(data)

                if len(data) < size:
                    break

            except Exception as e:
                print(e)
                client.close()
                return False

        print("Bytes:"+str(total))
        recv_image.flush()
        recv_image.close()

        self.command.execute(image_path)


if __name__ == "__main__":

    hostname = socket.gethostbyname(socket.gethostname())
    portNum = 10000
    print(hostname)

    command = SegmentationProcess("Posterior Parietal Cortex", hostname, 20000)
    ImageServer(hostname, portNum, command).listen()

