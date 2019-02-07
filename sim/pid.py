class PIDController:

    def __init__(self, kp, ki, kd, goal):
        self.kp = kp
        self.ki = ki
        self.kd = kd

        self.goal = goal
        self.error = 0

        self.lX = 0
        self.dError = 0
        self.iError = 0

    def correction(self):
        return (self.error * self.kp + self.iError*self.ki - (self.dError*self.kd))

    def updateError(self, currentPosition):
        self.error = self.goal - currentPosition
        self.iError = self.iError + self.error
        self.dError = currentPosition - self.lX
        self.lX = currentPosition
