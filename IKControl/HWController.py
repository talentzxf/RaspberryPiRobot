from IKControl.SuperSonic import SuperSonic
from MotorController import MotorController
from ServoController import ServoController


class HWController:
    def __init__(self):
        PWMA = 18
        AIN1 = 22
        AIN2 = 27

        PWMB = 23
        BIN1 = 25
        BIN2 = 24
        self.left_wheel = MotorController(PWMA, AIN1, AIN2)
        self.right_wheel = MotorController(PWMB, BIN1, BIN2)
        self.supersonic = SuperSonic()
        
        self.servos = {}
        self.servos[0] = ServoController(0)  # Clamp
        self.servos[1] = ServoController(1)  # Main arm
        self.servos[2] = ServoController(2)  # Second arm
        self.servos[3] = ServoController(3)  # Main platform
        self.set_servo_degree(1, 90)
        self.set_servo_degree(2,90)

    def set_left_speed(self, speed):
        self.left_wheel.set_speed(speed)

    def set_right_speed(self, speed):
        self.right_wheel.set_speed(speed)

    def start_supersonic(self, callback):
        self.supersonic.start_thread(callback)

    def stop_supersonic(self):
        self.supersonic.stop_thread()

    def set_servo_degree(self, id, degree):
        self.servos[int(id)].set_degree(float(degree))
