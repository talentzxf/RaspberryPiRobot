import math

DEGANGELDIFF = 50


class IKSolver:
    def __init__(self):
        self.l1 = 8
        self.l2 = 8
        pass

    @staticmethod
    def circle_intersect(l1, l2, t_x, t_y):
        A = (l1 * l1 - l2 * l2 + t_x * t_x + t_y * t_y) / (2.0 * t_x)
        B = -t_y / t_x
        C = B * B + 1
        D = 2 * A * B
        E = A * A - l1 * l1

        det = D * D - 4 * C * E
        if det < 0:
            raise Exception("Det < 0, circle didn't intersect!")

        y1 = (-D + math.sqrt(det)) / (2.0 * C)
        x1 = A + B * y1
        return x1, y1

    def vector_deg_angle(vec1, vec2):
        dot = vec1[0] * vec2[0] + vec1[1] * vec2[1]
        length1 = math.sqrt(vec1[0] * vec1[0] + vec1[1] * vec1[1])
        length2 = math.sqrt(vec2[0] * vec2[0] + vec2[1] * vec2[1])

        cosTheta = dot / (length1 * length2)
        print "vec1X:%f, vec1Y:%f, vec2X:%f, vec2Y:%f" % (vec1[0], vec1[1], vec2[0], vec2[1])
        print "dot:%f, length1:%f, length2:%f, cosTheta:%f" % (dot, length1, length2, cosTheta)
        return math.degrees(math.acos(cosTheta))

    @staticmethod
    def get_servo2_deg_angle_from_deg_theta2(deg_theta1, deg_theta2):
        return 180 - deg_theta1 + deg_theta2 - DEGANGELDIFF

    def solve2d(self, target_point):
        intersectPoint = self.circle_intersect(self.l1, self.l2, target_point[0], target_point[1])
        print "Circle IntersectPoint:%f,%f" % (intersectPoint[0], intersectPoint[1])
        degTheta2 = self.vector_deg_angle(intersectPoint,
                                          (intersectPoint[0] - target_point[0], intersectPoint[1] - target_point[1]))
        deg_theta1 = 180 - math.degrees(math.atan2(intersectPoint[1], intersectPoint[0]))

        print "Theta1:%f, Theta2:%f" % (deg_theta1, degTheta2)
        servo_theta2 = self.get_servo2_deg_angle_from_deg_theta2(deg_theta1, degTheta2)
        return deg_theta1, servo_theta2

    def solve3d(self, target_point):
        theta4 = math.atan2(target_point[0], target_point[2])
        deg_theta4 = math.degrees(theta4)

        target_point2d = (target_point[0] / math.sin(theta4), target_point[1])
        (servo1_deg_angle, servo2_deg_angle) = self.solve2d(target_point2d)
        return deg_theta4, servo1_deg_angle, servo2_deg_angle
