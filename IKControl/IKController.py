import math
import time


class IKController:
    def __init__(self, solver, hw_controller):
        self.IKSolver = solver
        self.hw_controller = hw_controller
        #SERVO
        self.myservo1 = 0  # clamp
        self.myservo2 = 1  # base
        self.myservo3 = 2  # main lever
        self.myservo4 = 3  # sub lever

    def point_3d(self, target_point):
        (ms4currentAngle, ms2currentAngle, ms3currentAngle) = self.IKSolver.solve3d(target_point)

        self.hw_controller.write(self.myservo2, ms2currentAngle)
        self.hw_controller.write(self.myservo3, ms3currentAngle)
        self.hw_controller.write(self.myservo4, ms4currentAngle)

    def line_motion_3d(self, start_point, end_point, total_time_sec=2.0, steps=10):
        step = 0

        stepDelta = 1.0 / steps
        stepSleepTime = total_time_sec / steps

        while step <= 1.0:
            current_point = (start_point[0] + step * (end_point[0] - start_point[0]),
                             start_point[1] + step * (end_point[1] - start_point[1]),
                             start_point[2] + step * (end_point[2] - start_point[2]))
            self.point_3d(current_point)
            time.sleep(stepSleepTime)
            step = step + stepDelta
            print "Current Step:%f" % step
