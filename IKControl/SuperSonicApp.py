import RPi.GPIO as GPIO
import time
import sys

SensorRight = 16
SensorLeft = 12

PWMA = 18
AIN1 = 22
AIN2 = 27

PWMB = 23
BIN1 = 25
BIN2 = 24

BtnPin = 19
Gpin = 5
Rpin = 6

TRIG = 20
ECHO = 21

def setup():
    GPIO.setwarnings(False)
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(TRIG, GPIO.OUT)
    GPIO.setup(ECHO, GPIO.IN)

    GPIO.setup(Gpin, GPIO.OUT)
    GPIO.setup(Rpin, GPIO.OUT)
    GPIO.setup(BtnPin, GPIO.IN, pull_up_down=GPIO.PUD_UP)

    GPIO.setup(AIN2, GPIO.OUT)
    GPIO.setup(AIN1, GPIO.OUT)
    GPIO.setup(PWMA, GPIO.OUT)

    GPIO.setup(BIN1, GPIO.OUT)
    GPIO.setup(BIN2, GPIO.OUT)
    GPIO.setup(PWMB, GPIO.OUT)


def distance():
    GPIO.output(TRIG, 0)
    time.sleep(0.000002)

    GPIO.output(TRIG, 1)
    time.sleep(0.000001)

    GPIO.output(TRIG, 0)

    while GPIO.input(ECHO) == 0:
        a = 0
    time1 = time.time()
    while GPIO.input(ECHO) == 1:
        a = 1

    time2 = time.time()
    during = time2 - time1
    return during * 340/2*100

def loop():
    while True:
        dis = distance()
        print dis,'cm'
        print ''

def destroy():
    GPIO.cleanup()


if __name__ == '__main__':
    setup()
    print "Begin loop"
    loop()
    print "End loop"
