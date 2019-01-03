#!/usr/bin/env python
# -*- coding: utf-8 -*-
from Adafruit_PWM_Servo_Driver import PWM
import RPi.GPIO as GPIO
import time
import sys
import serial
import math

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
  #time.sleep(0.3)

def ClampClose(): #手臂闭合
  write(myservo1,ms1MIN)
  #time.sleep(0.3)

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
  

def deg2rad(degAngle):
    return degAngle/180.0 * math.pi

def rad2deg(radAngle):
    return radAngle/math.pi * 180.0

def forwardX(l1, l2 , degTheta1, degTheta2):
    theta1 = deg2rad(degTheta1)
    theta2 = deg2rad(degTheta2)
    return l1 * math.cos(math.pi-theta1) + l2 * math.sin(-theta1 + theta2 + math.pi/2.0)
    

def forwardY(l1, l2, degTheta1, degTheta2):
     theta1 = deg2rad(degTheta1)
     theta2 = deg2rad(degTheta2)
     return l1 * math.sin(math.pi-theta1) - l2 * math.cos(-theta1 + theta2 + math.pi/2.0)

def getServo2DegAngleFromDegTheta2(degTheta1, degTheta2, degAngleDiff):
    return 180 - degTheta1 + degTheta2 - degAngleDiff


def getDegTheta2FromServo2Angle(degTheta1, servo2DegAngle, degAngleDiff):
    return servo2DegAngle - 180 + degTheta1 + degAngleDiff


def circleIntersect(l1,l2,Tx,Ty):
    A = (l1*l1-l2*l2+Tx*Tx+Ty*Ty)/(2.0*Tx)
    B = -Ty/Tx
    C = B*B+1
    D = 2*A*B
    E = A*A - l1*l1

    det = D*D - 4*C*E
    if det < 0 :
      raise Exception("Det < 0, circle didn't intersect!")
    
    y1 = (-D + math.sqrt(det))/(2.0*C)
    x1 = A+B*y1
    return (x1,y1)  

def vectorDegAngle(vec1, vec2):
  dot = vec1[0] * vec2[0] + vec1[1] * vec2[1]
  length1 = math.sqrt(vec1[0]*vec1[0] + vec1[1]*vec1[1])
  length2 = math.sqrt(vec2[0]*vec2[0] + vec2[1]*vec2[1])

  cosTheta = dot/(length1*length2)
  print "vec1X:%f, vec1Y:%f, vec2X:%f, vec2Y:%f"%(vec1[0],vec1[1],vec2[0],vec2[1])
  print "dot:%f, length1:%f, length2:%f, cosTheta:%f"%(dot,length1,length2,cosTheta)
  return rad2deg(math.acos(cosTheta))

def IKCalculate2D(l1,l2,Tx,Ty,degAngleDiff):
  intersectPoint = circleIntersect(l1,l2,Tx,Ty)
  print "Circle IntersectPoint:%f,%f"%(intersectPoint[0],intersectPoint[1])
  degTheta2 = vectorDegAngle(intersectPoint, (intersectPoint[0]-Tx,intersectPoint[1]-Ty) )

  degTheta1 = 180 - rad2deg(math.atan2(intersectPoint[1],intersectPoint[0]))

  print "Theta1:%f, Theta2:%f"%(degTheta1, degTheta2)
  servoTheta2 = getServo2DegAngleFromDegTheta2(degTheta1,degTheta2, degAngleDiff)

  return (degTheta1, servoTheta2)  

def IKCalculate3D(l1,l2,targetPoint,degAngleDiff):
  theta4 = math.atan2(targetPoint[0], targetPoint[2])
  degTheta4 = rad2deg(theta4)
  X2D = targetPoint[0] / math.sin(theta4)
  Y2D = targetPoint[1]
  
  (servo1DegAngle, servo2DegAngle) = IKCalculate2D(l1,l2,X2D, Y2D, degAngleDiff)
  return (degTheta4, servo1DegAngle, servo2DegAngle)

def lineMotion3D(startPoint, endPoint):
        global ms3currentAngle
        global ms2currentAngle
        idx = 0
        degAngleDiff = 50
        l1 = 8
        l2 = 8
        # currentDegTheta2 = getDegTheta2FromServo2Angle(ms2currentAngle, ms3currentAngle, degAngleDiff)
        # currentX = forwardX(l1,l2, ms2currentAngle, currentDegTheta2)
        # currentY = forwardY(l1,l2, ms2currentAngle, currentDegTheta2)

        # print "Init theta1:%f, theta2:%f, CurrentX:%f, CurrentY:%f" % (ms2currentAngle, currentDegTheta2, currentX, currentY)

        step = 0
        stepDelta = 0.2

        clampOpen = True
        while step <= 1.0:
          currentPoint = (startPoint[0] + step * (endPoint[0] - startPoint[0]),
                          startPoint[1] + step * (endPoint[1] - startPoint[1]),
                          startPoint[2] + step * (endPoint[2] - startPoint[2]))
          (ms4currentAngle, ms2currentAngle, ms3currentAngle) = IKCalculate3D(l1,l2,currentPoint,degAngleDiff)

          # print "ms2currentAngle:%f, ms3currentAngle:%f"%(ms2currentAngle, ms3currentAngle)
          write(2,ms3currentAngle)
          write(1,ms2currentAngle)
          write(3, ms4currentAngle)
          #currentDegTheta2 = getDegTheta2FromServo2Angle(ms2currentAngle, ms3currentAngle, degAngleDiff)
          # testX = forwardX(l1,l2, ms2currentAngle, currentDegTheta2)
          #testY = forwardY(l1,l2, ms2currentAngle, currentDegTheta2)
          #print "TestX: %f, TestY: %f" % (testX,testY)
          time.sleep(0.1)
          # if clampOpen:
          #  clampOpen = False
          #  ClampOpen()
          #else:
          #  clampOpen = True
          #  ClampClose()
          step = step + stepDelta
          print "Current Step:%f"%step
           
#        for j in range(1,5):
#           Arm_A_Up() 
#           ms3currentAngle = backwardGetDegTheta2( l1, l2, currentX, -1, ms2currentAngle)
#           print "Current ms2currentAngle: %f, ms3currentAngle:%f" % (ms2currentAngle, ms3currentAngle)
#           currentTheta2 = getDegTheta2FromServo2Angle(ms2currentAngle, ms3currentAngle, rad2deg(angleDiff))
#           currentX = forwardX(l1,l2, ms2currentAngle, currentTheta2) 
#           print "CurrentX:%f, currentTheta2:%f" % (currentX, currentTheta2)
#           write(2,ms3currentAngle)
#           

def loop():
  height = 3
  lineMotion3D((5,-height,2),(10,-height,2))
  lineMotion3D((10,-height,2),(5,-height,4))
  lineMotion3D((5,-height,4),(10,-height,4))

  lineMotion3D((10,-height,4), (10,height,6))

  lineMotion3D((10,-height,6), (7,-height,7))
  lineMotion3D((7,-height,7), (5,-height,7))
  lineMotion3D((5,-height,7), (7,-height,7))
  lineMotion3D((7,-height,7), (10,-height,8))


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
        keysacn()
        try:
                print("Looping")
                loop()
        except KeyboardInterrupt:
                destroy()

