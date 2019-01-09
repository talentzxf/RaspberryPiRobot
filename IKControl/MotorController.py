from Adafruit_PWM_Servo_Driver import PWM
import RPi.GPIO as GPIO

class MotorController:
    def __init__(self, pwm_address, A1, A2):
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)

        GPIO.setup(A1,GPIO.OUT)
        GPIO.setup(A2,GPIO.OUT)
        GPIO.setup(pwm_address, GPIO.OUT)

        self.A1 = A1
        self.A2 = A2
        self.motor= GPIO.PWM(pwm_address,100)
        self.motor.start(0)

    def set_speed(self, speedstr):

        speed = int(speedstr)
        
        if speed > 0:
            GPIO.output(self.A1,True)
            GPIO.output(self.A2,False)
        elif speed == 0:
            GPIO.output(self.A1,False)
            GPIO.output(self.A2,False)
        else:
            speed = -speed
            GPIO.output(self.A1,False)
            GPIO.output(self.A2,True)
        print "Setting motor cycle:%s"%speed
        self.motor.ChangeDutyCycle(float(speed))