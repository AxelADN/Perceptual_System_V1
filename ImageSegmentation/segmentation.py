#https://www.bogotobogo.com/python/OpenCV_Python/python_opencv3_Image_Global_Thresholding_Adaptive_Thresholding_Otsus_Binarization_Segmentations.php
#https://www.pyimagesearch.com/2015/11/02/watershed-opencv/

import cv2
import numpy as np
from matplotlib import pyplot as plt
from skimage.measure import compare_ssim as ssim

# Resize with ratio

def image_resize(image, width = None, height = None, inter = cv2.INTER_AREA):
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
    resized = cv2.resize(image, dim, interpolation = inter)

    return resized

#Calculates the similarity

def compare_images(imageA, imageB, title):

    s = ssim(imageA, imageB)

    return s

#Compares the target image with the other samples

def detect_class(target):

    original = cv2.imread(target)
    original = cv2.cvtColor(original, cv2.COLOR_BGR2GRAY)

    similarValues = []

    for i in range(0,100):

        contrast = cv2.imread("original/obj"+str(i+1)+"__0.png")

        # convert the images to grayscale
        contrast = cv2.cvtColor(contrast, cv2.COLOR_BGR2GRAY)

        sim = compare_images(original, contrast, "Original vs "+str(i+1))

        #print(target+" vs "+str(i+1)+": "+str(sim))

        similarValues.append(sim)

    maxValue = np.amax(similarValues)
    max = np.argmax(similarValues)

    # setup the figure
    fig = plt.figure(target+" Max: "+str(max+1))
    plt.suptitle("Similitud: %.2f" % (maxValue))

    # show first image
    detected = cv2.imread(target)
    ax = fig.add_subplot(1, 2, 1)
    plt.imshow(detected)
    plt.axis("off")

    # show the second image
    matched = cv2.imread("original/obj" + str(max + 1) + "__0.png")
    ax = fig.add_subplot(1, 2, 2)
    plt.imshow(matched)
    plt.axis("off")

    # show the images
    plt.show()

    print(target+" Max: "+str(max+1))

#################


##########################################################################################
#
# Main Method
#
#########################################################################################

#gridsColumns = 4
#gridsRows = 5

gridsColumns = 100
gridsRows = 60

image = cv2.imread('scene8_capture.png')

imgHeight, imgWidth, channels = image.shape

print(str(imgHeight)+","+str(imgWidth))

sceneImage = image.copy()

#Convierte a una imagen binaria para poder segmentar
#De 0 a 29 es el fondo

gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

img = cv2.GaussianBlur(gray,(5,5),0)

ret,th1 = cv2.threshold(img,29,255,cv2.THRESH_BINARY)

# find contours in the thresholded image

cnts = cv2.findContours(th1.copy(), cv2.RETR_EXTERNAL,cv2.CHAIN_APPROX_SIMPLE)[-2]

# loop over the contours

detectedImages = []

for (i, c) in enumerate(cnts):
    (x, y, w, h) = cv2.boundingRect(c)

    if w >= 20 and h >= 20 and i < len(cnts):

        cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)
        cv2.putText(image, "#{}".format(i + 1), (int(x) - 10, int(y)), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 255), 2)
        cv2.drawContours(image,[c],0,(0,0,255),1)

        cv2.circle(image,(int (x + w/2), int (y + h/2)),5,(80,69,255),2)

        indexColumn = int (((x + w/2) * gridsColumns)/imgWidth);
        indexRow = int(((y + h/2) * gridsRows)/imgHeight);

        cv2.putText(image, str(indexColumn)+","+str(indexRow), (int(x + w/2), int(y + h/2)), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 255), 2)

        singleImage = sceneImage[y:y + h, x:x + w]

        if h >= w:
            resized_image = image_resize(singleImage, height = 128)
        else:
            resized_image = image_resize(singleImage, width = 128)

        nh, nw = resized_image.shape[:2]

        blank_image = np.zeros((128, 128, 3), np.uint8)
        blank_image[::] = (29, 29, 29)
        blank_image[int((128-nh)/2):int((128-nh)/2) + resized_image.shape[0], int((128-nw)/2):int((128-nw)/2) + resized_image.shape[1]] = resized_image

        imageName = "detected/"+str(i+1) + '.png'

        cv2.imwrite(imageName, blank_image)

        detectedImages.append(imageName)

cv2.imshow("Image", image)
cv2.waitKey(0)
for target in detectedImages:

    detect_class(target)


