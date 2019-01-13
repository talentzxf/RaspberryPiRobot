from threading import Thread

import RPi.GPIO as GPIO
import time
import sys


class SuperSonic:
    def __init__(self):
        self.SensorRight = 16
        self.SensorLeft = 12

        self.PWMA = 18
        self.AIN1 = 22
        self.AIN2 = 27

        self.PWMB = 23
        self.BIN1 = 25
        self.BIN2 = 24

        self.BtnPin = 19
        self.Gpin = 5
        self.Rpin = 6

        self.TRIG = 20
        self.ECHO = 21

        self.is_thread_running = False

    def setup(self):
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(self.TRIG, GPIO.OUT)
        GPIO.setup(self.ECHO, GPIO.IN)

        GPIO.setup(self.Gpin, GPIO.OUT)
        GPIO.setup(self.Rpin, GPIO.OUT)
        GPIO.setup(self.BtnPin, GPIO.IN, pull_up_down=GPIO.PUD_UP)

        GPIO.setup(self.AIN2, GPIO.OUT)
        GPIO.setup(self.AIN1, GPIO.OUT)
        GPIO.setup(self.PWMA, GPIO.OUT)

        GPIO.setup(self.BIN1, GPIO.OUT)
        GPIO.setup(self.BIN2, GPIO.OUT)
        GPIO.setup(self.PWMB, GPIO.OUT)

    def distance(self):
        GPIO.output(self.TRIG, 0)
        time.sleep(0.000002)

        GPIO.output(self.TRIG, 1)
        time.sleep(0.000001)

        GPIO.output(self.TRIG, 0)

        while GPIO.input(self.ECHO) == 0:
            a = 0
        time1 = time.time()
        while GPIO.input(self.ECHO) == 1:
            a = 1

        time2 = time.time()
        during = time2 - time1
        return during * 340 / 2 * 100

    def thread_func(self):
        self.is_thread_running = True

        self.setup()
        while self.is_thread_running:
            self.callback(self.distance())

        self.is_thread_running = True

    def start_thread(self, callback):
        if not self.is_thread_running:
            self.callback = callback
            self.thread = Thread(target=self.thread_func)
            self.thread.start()
        else:
            print "Super sonic is running, can't start again!"

    def stop_thread(self):
        self.is_thread_running = False
        self.thread.join()
        print "Super sonic stopped"