#https://www.bogotobogo.com/python/OpenCV_Python/python_opencv3_Image_Global_Thresholding_Adaptive_Thresholding_Otsus_Binarization_Segmentations.php
#https://www.pyimagesearch.com/2015/11/02/watershed-opencv/

import cv2
import numpy as np
from matplotlib import pyplot as plt

image = cv2.imread('scene10_capture.png')
cv2.imshow("Image", image)

gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

img = cv2.medianBlur(gray,5)

#Convierte a una imagen binaria para poder segmentar
#De 0 a 29 es el fondo

ret,th1 = cv2.threshold(img,29,255,cv2.THRESH_BINARY)

# find contours in the thresholded image
cnts = cv2.findContours(th1.copy(), cv2.RETR_EXTERNAL,
	cv2.CHAIN_APPROX_SIMPLE)[-2]

# loop over the contours
for (i, c) in enumerate(cnts):
	# draw the contour
	#((x, y), _) = cv2.minEnclosingCircle(c)
    (x, y, w, h) = cv2.boundingRect(c)
    cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)

    cv2.putText(image, "#{}".format(i + 1), (int(x) - 10, int(y)), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 255), 2)
    #cv2.drawContours(image, [c], -1, (0, 255, 0), 2)
    cv2.drawContours(image,[c],0,(0,0,255),2)

cv2.imshow("Image", image)


#El threshold no es tan chido porque al cambiar las condiciones de luz un valor fijo no hace paro
#Para esos casos esta el threshold adaptativo que calcula el umbral para pequeñas regiones de la imagen, asi
#Se pueden tener diferentes umbrales para diferentes regiones de la misma imagen

th2 = cv2.adaptiveThreshold(img,255,cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY,11,2)
th3 = cv2.adaptiveThreshold(img,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,11,2)

titles = ['Original Image', 'Global Thresholding (v = 127)',
            'Adaptive Mean Thresholding', 'Adaptive Gaussian Thresholding']
images = [img, th1, th2, th3]

for i in range(4):
    plt.subplot(2,2,i+1),plt.imshow(images[i],'gray')
    plt.title(titles[i])
    plt.xticks([]),plt.yticks([])
plt.show()