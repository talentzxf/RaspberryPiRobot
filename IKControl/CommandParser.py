import traceback
from HWController import HWController
from IKControl.SuperSonic import SuperSonic


class CommandParser:
    def __init__(self):
        self.command_mapping = {
            'clamp_point': self.clamp_point,
            "reset": self.reset,
            'echo': self.echo,
            'setspeed': self.setspeed,
            'supersonic': self.supersonic,
            'setservodegree' : self.setservodegree
        }
        self.hw_controller = HWController()

    def supersonic_callback(self):
        pass

    def supersonic(self, is_start):
        if is_start == 'start':
            self.hw_controller.start_supersonic(self.supersonic_callback)
        elif is_start == "stop":
            self.hw_controller.stop_supersonic()
        pass

    def dispatch(self, command):
        try:
            command_params = command.split(" ")
            if command_params[0] in self.command_mapping:
                command_execution_method = self.command_mapping[command_params[0]]
                command_params.pop(0)
                return command_execution_method(*command_params)
            else:
                return "Unknown command:%s" % command
        except:
            return "Exception happened:", traceback.format_exc()

    def setspeed(self, side, speed):
        if side == "left":
            self.hw_controller.set_left_speed(speed)
        elif side == "right":
            self.hw_controller.set_right_speed(speed)
        return "echo setspeed:" + side + "," + speed

    def setservodegree(self, id, degree):
        self.hw_controller.set_servo_degree(id, degree)
        return "Servo:" + id + " set to degree:" + degree

    def echo(self, content):
        return "echo " + content

    def clamp_point(self, point_x, point_y, point_z):
        float_point_x = float(point_x)
        float_point_y = float(point_y)
        float_point_z = float(point_z)
        return "echo Point coords:%f,%f,%f" % (float_point_x, float_point_y, float_point_z)

    def reset(self):
        return "echo System reseted"
