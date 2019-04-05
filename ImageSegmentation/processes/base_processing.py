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
       #self._node = NodeConnection(address, port)
        #self._node.connect()

    '''
    Helper
    '''

    # Resize with ratio

    def image_resize(self, image, width=None, height=None, inter=cv2.INTER_AREA):
        dim = None
        (h, w) = image.shape[:2]

        if width is None and height is None:
            return image

        if width is None:
            r = height / float(h)
            dim = (int(w * r), height)
        else:
            r = width / float(w)
            dim = (width, int(h * r))
        resized = cv2.resize(image, dim, interpolation=inter)

        return resized

    def execute(self, image_path):

        grids_columns = 1
        grids_rows = 1

        image_matrix = [[0 for x in range(grids_columns)] for y in range(grids_rows)]

        image = cv2.imread(image_path)

        imgHeight, imgWidth, channels = image.shape

        scene_image = image.copy()
        resized_image = None

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
            print(str(w)+","+str(h))
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

                image_matrix[index_row][index_column] = (i + 1)
                print(image_matrix)

                single_image = scene_image[y:y + h, x:x + w]

                if h >= w:
                    resized_image = self.image_resize(single_image, height=128)
                else:
                    resized_image = self.image_resize(single_image, width=128)

                self.perform_action(image, index_column, index_row, i)

        cv2.imshow(self._title, image)
        cv2.waitKey(0)
        cv2.imshow(self._title, resized_image)
        cv2.waitKey(0)

    def perform_action(self, image, cx, cy, id):

        print(str(cx) + "," + str(cy) + ":" + str(id))

        #Send to the middleware

        #spike = Spike("Loc_Spk", "Spatial", image_matrix, 0, 0)

        #self._node.send_json(spike.to_json())

