import traceback


class CommandParser:
    def __init__(self):
        self.command_mapping = {
            'clamp_point': self.clamp_point,
            "reset": self.reset
        }

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


    def clamp_point(self, point_x, point_y, point_z):
        float_point_x = float(point_x)
        float_point_y = float(point_y)
        float_point_z = float(point_z)
        return "Point coords:%f,%f,%f" % (float_point_x, float_point_y, float_point_z)

    def reset(self):
        return "System reseted"
