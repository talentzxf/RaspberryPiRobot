from Adafruit_PWM_Servo_Driver import PWM


class ServoController:
    def __init__(self, id):
        self.pwm = PWM(0x40, debug=False)
        self.servoid = id
        pass

    # Angle to PWM
    def set_degree(self, x):
        y = x / 90.0 + 0.5
        y = max(y, 0.5)
        y = min(y, 2.5)
        self.setServoPulse(self.servoid, y)

    def setServoPulse(self, channel, pulse):
        pulseLength = 1000000.0  # 1,000,000 us per second
        pulseLength /= 50.0  # 60 Hz
        print "%d us per period" % pulseLength
        pulseLength /= 4096.0  # 12 bits of resolution
        print "%d us per bit" % pulseLength
        pulse *= 1000.0
        pulse /= (pulseLength * 1.0)
        # pwmV=int(pluse)
        print "pluse: %f  " % (pulse)
        self.pwm.setPWM(channel, 0, int(pulse))
