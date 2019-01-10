from IKControl.SuperSonic import SuperSonic
from MotorController import MotorController

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
    
    def set_left_speed(self, speed):
        self.left_wheel.set_speed(speed)
    
    def set_right_speed(self, speed):
        self.right_wheel.set_speed(speed)

    def start_supersonic(self, callback):
        self.supersonic.start_thread(callback)

    def stop_supersonic(self):
        self.supersonic.stop_thread()
