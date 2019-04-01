# import the necessary packages
from skimage.measure import compare_ssim as ssim
import matplotlib.pyplot as plt
import numpy as np
import cv2


def mse(imageA, imageB):
    # the 'Mean Squared Error' between the two images is the
    # sum of the squared difference between the two images;
    # NOTE: the two images must have the same dimension
    err = np.sum((imageA.astype("float") - imageB.astype("float")) ** 2)
    err /= float(imageA.shape[0] * imageA.shape[1])

    # return the MSE, the lower the error, the more "similar"
    # the two images are
    return err


def compare_images(imageA, imageB, title):

    m = mse(imageA, imageB)
    s = ssim(imageA, imageB)

    return s


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




detectedImages = ["detected/2.png","detected/3.png","detected/4.png","detected/5.png","detected/6.png",
                  "detected/7.png","detected/8.png","detected/9.png","detected/10.png","detected/11.png"]

for target in detectedImages:

    detect_class(target)

