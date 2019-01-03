import RPi.GPIO as GPIO
import time

LEDPin = 29
GPIO.setmode(GPIO.BOARD)
GPIO.setup(LEDPin, GPIO.OUT)
GPIO.output(LEDPin, GPIO.HIGH)

try:
    while True:
        print '... led on'
        GPIO.output(LEDPin, GPIO.LOW)
        time.sleep(0.5)
        print '... led off'
        GPIO.output(LEDPin, GPIO.HIGH)
        time.sleep(0.5)
except KeyboardInterrupt:
    GPIO.output(LEDPin, GPIO.HIGH)
    GPIO.cleanup()
