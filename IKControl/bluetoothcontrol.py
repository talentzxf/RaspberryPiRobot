#!/usr/bin/env python
# -*- coding: utf-8 -*-
from Adafruit_PWM_Servo_Driver import PWM
import RPi.GPIO as GPIO
import time
import sys
import serial

ser = serial.Serial("/dev/ttyAMA0",9600)  #串口波特率设置

PWMA   = 18
AIN1   = 22
AIN2   = 27

PWMB   = 23
BIN1   = 25
BIN2   = 24

BtnPin  = 19
Gpin    = 5
Rpin    = 6

TRIG = 20
ECHO = 21
# Initialise the PWM device using the default address
# bmp = PWM(0x40, debug=True)
pwm = PWM(0x40,debug = False)

servoMin = 150  # Min pulse length out of 4096
servoMax = 600  # Max pulse length out of 4096
#SERVO
myservo1 = 0
myservo2 = 1
myservo3 = 2
myservo4 = 3

SERVOS = 4  #舵机数4个

#定义舵机的角度值初始化
#手爪
ms1MIN = 10
ms1MAX = 50
ms1INITANGLE = 28
ms1currentAngle = 0

#上臂电机
ms2MIN = 10
ms2MAX = 140
ms2INITANGLE = 90
ms2currentAngle = 0

#下臂电机
ms3MIN = 40
ms3MAX = 170
ms3INITANGLE = 90
ms3currentAngle = 0

#底座
ms4MIN = 0
ms4MAX = 170
ms4INITANGLE = 90
ms4currentAngle = 0

ServoDelayTime = 0.05 #舵机响应时间
delta = 5        #舵机转动幅度
delta_bottom = 2 #底座舵机转动幅度


def setServoPulse(channel, pulse):
  pulseLength = 1000000.0                   # 1,000,000 us per second
  pulseLength /= 50.0                       # 60 Hz
  print "%d us per period" % pulseLength
  pulseLength /= 4096.0                     # 12 bits of resolution
  print "%d us per bit" % pulseLength
  pulse *= 1000.0
  pulse /= (pulseLength*1.0)
# pwmV=int(pluse)
  print "pluse: %f  " % (pulse)
  pwm.setPWM(channel, 0, int(pulse))

#Angle to PWM
def write(servonum,x):
  y=x/90.0+0.5
  y=max(y,0.5)
  y=min(y,2.5)
  setServoPulse(servonum,y)
  
def t_up(speed,t_time):
        L_Motor.ChangeDutyCycle(speed)
        GPIO.output(AIN2,False)#AIN2
        GPIO.output(AIN1,True) #AIN1

        R_Motor.ChangeDutyCycle(speed)
        GPIO.output(BIN2,False)#BIN2
        GPIO.output(BIN1,True) #BIN1
        time.sleep(t_time)
        
def t_stop(t_time):
        L_Motor.ChangeDutyCycle(0)
        GPIO.output(AIN2,False)#AIN2
        GPIO.output(AIN1,False) #AIN1

        R_Motor.ChangeDutyCycle(0)
        GPIO.output(BIN2,False)#BIN2
        GPIO.output(BIN1,False) #BIN1
        time.sleep(t_time)
        
def t_down(speed,t_time):
        L_Motor.ChangeDutyCycle(speed)
        GPIO.output(AIN2,True)#AIN2
        GPIO.output(AIN1,False) #AIN1

        R_Motor.ChangeDutyCycle(speed)
        GPIO.output(BIN2,True)#BIN2
        GPIO.output(BIN1,False) #BIN1
        time.sleep(t_time)

def t_left(speed,t_time):
        L_Motor.ChangeDutyCycle(speed)
        GPIO.output(AIN2,True)#AIN2
        GPIO.output(AIN1,False) #AIN1

        R_Motor.ChangeDutyCycle(speed)
        GPIO.output(BIN2,False)#BIN2
        GPIO.output(BIN1,True) #BIN1
        time.sleep(t_time)

def t_right(speed,t_time):
        L_Motor.ChangeDutyCycle(speed)
        GPIO.output(AIN2,False)#AIN2
        GPIO.output(AIN1,True) #AIN1

        R_Motor.ChangeDutyCycle(speed)
        GPIO.output(BIN2,True)#BIN2
        GPIO.output(BIN1,False) #BIN1
        time.sleep(t_time)
        
def keysacn():
    val = GPIO.input(BtnPin)
    while GPIO.input(BtnPin) == False:
        val = GPIO.input(BtnPin)
    while GPIO.input(BtnPin) == True:
        time.sleep(0.01)
        val = GPIO.input(BtnPin)
        if val == True:
            GPIO.output(Rpin,1)
            while GPIO.input(BtnPin) == False:
                GPIO.output(Rpin,0)
        else:
            GPIO.output(Rpin,0)
            
def setup():
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)
        
        GPIO.setup(Gpin, GPIO.OUT)     # Set Green Led Pin mode to output
        GPIO.setup(Rpin, GPIO.OUT)     # Set Red Led Pin mode to output
        GPIO.setup(BtnPin, GPIO.IN, pull_up_down=GPIO.PUD_UP)    # Set BtnPin's mode is input, and pull up to high level(3.3V)

        GPIO.setup(AIN2,GPIO.OUT)
        GPIO.setup(AIN1,GPIO.OUT)
        GPIO.setup(PWMA,GPIO.OUT)
        
        GPIO.setup(BIN1,GPIO.OUT)
        GPIO.setup(BIN2,GPIO.OUT)
        GPIO.setup(PWMB,GPIO.OUT)
        pwm.setPWMFreq(50)                        # Set frequency to 60 Hz
        
def Re_Servo():
  global  ms1currentAngle
  global  ms2currentAngle
  global  ms3currentAngle
  global  ms4currentAngle  
  write(myservo1,ms1INITANGLE)   #手爪  
  write(myservo2,ms2INITANGLE)   #上臂  
  write(myservo3,ms3INITANGLE)   #下臂  
  write(myservo4,ms4INITANGLE)   #底座
  
  ms1currentAngle = ms1INITANGLE
  ms2currentAngle = ms2INITANGLE
  ms3currentAngle = ms3INITANGLE
  ms4currentAngle = ms4INITANGLE  
  time.sleep(1)
#-------------------机械臂运动函数定义----------------
def ClampOpen():  #手爪打开
  write(myservo1,ms1MAX)
  time.sleep(0.3)

def ClampClose(): #手臂闭合
  write(myservo1,ms1MIN)
  time.sleep(0.3)

def BottomLeft(): #底座左转
  global ms4currentAngle
  if(ms4currentAngle+delta_bottom) < ms4MAX:
    ms4currentAngle+=delta_bottom
  write(myservo4,ms4currentAngle)    

def BottomRight(): #底座右转
  global ms4currentAngle
  if(ms4currentAngle-delta_bottom) > ms4MIN:
    ms4currentAngle-=delta_bottom
  write(myservo4,ms4currentAngle)

def Arm_A_Up(): #上臂舵机向上
  global ms2currentAngle
  if(ms2currentAngle + delta) < ms2MAX:
    ms2currentAngle += delta
  write(myservo2,ms2currentAngle)

def Arm_A_Down():#上臂舵机向下
  global ms2currentAngle
  if(ms2currentAngle - delta) > ms2MIN:
    ms2currentAngle -= delta
  write(myservo2,ms2currentAngle)
  
def Arm_B_Up():#下臂舵机向上
  global ms3currentAngle
  if(ms3currentAngle - delta) > ms3MIN:
    ms3currentAngle -= delta
  write(myservo3,ms3currentAngle)

def Arm_B_Down(): #下臂舵机向下
  global ms3currentAngle
  if(ms3currentAngle + delta) < ms3MAX:
    ms3currentAngle += delta
  write(myservo3,ms3currentAngle) 

def Servo_stop(): #停止所有舵机
  write(myservo1,ms1currentAngle)
  write(myservo2,ms2currentAngle)
  write(myservo3,ms3currentAngle) 
  write(myservo4,ms4currentAngle) 
  
def loop():
        while True:
          command=ser.read()
          ser.write(command +'\n')#回显
          if command == 'A':
            t_up(50,0)    #前进
          elif command == 'B':
            t_down(50,0)  #后退
          elif command == 'D':
            t_right(50,0) #右转
          elif command == 'C':
            t_left(50,0)  #左转
          elif command == 'F':
            t_stop(0)     #停止
          elif command == '0':
            ser.write("Servo all stop\n")
            #Servo_stop()
            time.sleep(ServoDelayTime)
          elif command =='1':  #1
            ser.write("MeArm turn Left\n")
            BottomLeft()
            time.sleep(ServoDelayTime)
          elif command =='2':  #2
            ser.write("MeArm turn Right\n")
            BottomRight()
            time.sleep(ServoDelayTime)
          elif command =='4':  #4
            ser.write("Arm A Up\n")
            Arm_A_Up()
            time.sleep(ServoDelayTime)
          elif command =='J':  #上
            ser.write("Arm A Down\n")
            Arm_A_Down()
            time.sleep(ServoDelayTime)         
          elif command =='L':   #左
            ser.write("Arm B Up\n")
            Arm_B_Up()
            time.sleep(ServoDelayTime) 
          elif command =='K':    #下
            ser.write("Arm B Down\n")
            Arm_B_Down()
            time.sleep(ServoDelayTime)            
          elif command =='G':  #打开手爪 （加速）
            ser.write("Clamp Open\n")
            ClampOpen()
          elif command =='H':  #闭合手爪 （减速）
            ser.write("Clamp Close\n")
            ClampClose()
            
def destroy():
	GPIO.cleanup()

if __name__ == "__main__":
        setup()
        L_Motor= GPIO.PWM(PWMA,100)
        L_Motor.start(0)
        R_Motor = GPIO.PWM(PWMB,100)
        R_Motor.start(0)
        keysacn()
        Re_Servo()
        try:
                loop()
        except KeyboardInterrupt:
                destroy()

