import cv2
import numpy as np
from matplotlib import pyplot as plt
from skimage.measure import compare_ssim as ssim
from middleware.node_connection import NodeConnection
from middleware.spike import Spike


class BaseImageProcessing(object):

    def __init__(self, title):
        self._title = title

    def execute(self, image_path):
        print("Executing action "+image_path)


class SegmentationProcess(BaseImageProcessing):

    def __init__(self, title):
        self._title = title

    def __init__(self, title, address, port):
        self._title = title
        self._node = NodeConnection(address, port)
        self._node.connect()

    def execute(self, image_path):

        grids_columns = 4
        grids_rows = 5

        image_matrix = [[0 for x in range(grids_columns)] for y in range(grids_rows)]

        image = cv2.imread(image_path)

        imgHeight, imgWidth, channels = image.shape

        print("Size: "+str(imgHeight) + "," + str(imgWidth))

        # Convierte a una imagen binaria para poder segmentar
        # De 0 a 29 es el fondo

        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

        img = cv2.GaussianBlur(gray, (5, 5), 0)

        ret, th1 = cv2.threshold(img, 29, 255, cv2.THRESH_BINARY)

        # find contours in the thresholded image

        cnts = cv2.findContours(th1.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)[-2]

        for (i, c) in enumerate(cnts):
            (x, y, w, h) = cv2.boundingRect(c)

            if w >= 20 and h >= 20 and i < len(cnts):

                cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)
                cv2.putText(image, "#{}".format(i + 1), (int(x) - 10, int(y)), cv2.FONT_HERSHEY_SIMPLEX, 0.6,
                            (255, 255, 255), 2)

                cv2.drawContours(image, [c], 0, (0, 0, 255), 1)

                cv2.circle(image, (int(x + w / 2), int(y + h / 2)), 5, (80, 69, 255), 2)

                index_column = int(((x + w / 2) * grids_columns) / imgWidth);
                index_row = int(((y + h / 2) * grids_rows) / imgHeight);

                cv2.putText(image, str(index_column) + "," + str(index_row), (int(x + w / 2), int(y + h / 2)),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 255), 2)

                #Create the matrix used to get the 2D-String

                print(str(index_row)+","+str(index_column)+":"+str(i))

                image_matrix[index_row][index_column] = (i + 1)

        print(image_matrix)

        #Send to the middleware

        spike = Spike("Loc_Spk", "Spatial", image_matrix, 0, 0)

        self._node.send_json(spike.to_json())

        cv2.imshow(self._title, image)
        cv2.waitKey(0)
