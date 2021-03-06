# python detect_driver_drowsiness.py --shape-predictor shape_predictor_68_face_landmarks.dat
# python detect_driver_drowsiness.py --shape-predictor shape_predictor_68_face_landmarks.dat --alarm alarm.wav

from scipy.spatial import distance as dist
from imutils.video import VideoStream
from imutils import face_utils
from threading import Thread
import numpy as np
import playsound
import argparse
import imutils
import time
import dlib
import cv2
import random
from firebase import firebase


COUNTER = 0
SND = 0
SND1 = 0
TOTAL = 0
TOTAL1 = 0
TOTAL2 = 0
SPEED = 0
i = 0
j = 0
j1 = 0
 
def sound_alarm(path):
	playsound.playsound(path)

def eye_aspect_ratio(eye):
	A = dist.euclidean(eye[1], eye[5])
	B = dist.euclidean(eye[2], eye[4])
	C = dist.euclidean(eye[0], eye[3])
	ear = (A + B) / (2.0 * C)
	return ear
 

ap = argparse.ArgumentParser()
ap.add_argument("-p", "--shape-predictor", required=True,
	help="path to facial landmark predictor")
ap.add_argument("-a", "--alarm", type=str, default="",
	help="path alarm .WAV file")
ap.add_argument("-w", "--webcam", type=int, default=0,
	help="index of webcam on system")
args = vars(ap.parse_args())
 

def f1():
	EYE_AR_THRESH = 0.3
	EYE_AR_CONSEC_FRAMES = 48

	global COUNTER,SND,SND1,TOTAL,TOTAL1,TOTAL2,SPEED,i,j,j1,x
	ALARM_ON = False
	fr = firebase.FirebaseApplication('https://a124-270e9.firebaseio.com/')


	print("[INFO] loading facial landmark predictor...")
	detector = dlib.get_frontal_face_detector()
	predictor = dlib.shape_predictor(args['shape_predictor'])


	(lStart, lEnd) = face_utils.FACIAL_LANDMARKS_IDXS["left_eye"]
	(rStart, rEnd) = face_utils.FACIAL_LANDMARKS_IDXS["right_eye"]

	# start the video stream thread
	print("[INFO] starting video stream thread...")
	vs = VideoStream(src=0).start()
	fileStream = False
	time.sleep(1.0)

	# loop over frames from the video stream
	while True:
		frame = vs.read()
		frame = imutils.resize(frame, width=450)
		gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

		# detect faces in the grayscale frame
		rects = detector(gray, 0)

		# loop over the face detections
		for rect in rects:
			shape = predictor(gray, rect)
			shape = face_utils.shape_to_np(shape)
			leftEye = shape[lStart:lEnd]
			rightEye = shape[rStart:rEnd]
			leftEAR = eye_aspect_ratio(leftEye)
			rightEAR = eye_aspect_ratio(rightEye)
			ear = (leftEAR + rightEAR) / 2.0
			leftEyeHull = cv2.convexHull(leftEye)
			rightEyeHull = cv2.convexHull(rightEye)
			cv2.drawContours(frame, [leftEyeHull], -1, (0, 255, 0), 1)
			cv2.drawContours(frame, [rightEyeHull], -1, (0, 255, 0), 1)

			if ear < EYE_AR_THRESH:
				COUNTER += 1

				if COUNTER >= EYE_AR_CONSEC_FRAMES:
					# if the alarm is not on, turn it on
					fr.put('/','Blinkrate',str("ALERT!!"))
					TOTAL += 1
					if not ALARM_ON:
						ALARM_ON = True
						if args["alarm"] != "":
							t = Thread(target=sound_alarm,
								args=(args["alarm"],))
							t.deamon = True
							t.start()

					# draw an alarm on the frame
					cv2.putText(frame, "DROWSINESS ALERT!", (10, 30),
						cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)
			else:
				COUNTER = 0
				ALARM_ON = False
				fr.put('/','Blinkrate',str("0"))

		
			cv2.putText(frame, "EAR: {:.2f}".format(ear), (300, 30),
				cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)
	


		cv2.imshow("Frame", frame)
		key = cv2.waitKey(1) & 0xFF

		if key == ord("q"):
			break

	# do a bit of cleanup
	cv2.destroyAllWindows()
	vs.stop()




if __name__ == '__main__':
    Thread(target = f1).start()
    #Thread(target = f2).start()
    #Thread(target = f3).start()