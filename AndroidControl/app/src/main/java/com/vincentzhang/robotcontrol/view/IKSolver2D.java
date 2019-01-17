package com.vincentzhang.robotcontrol.view;

import com.vincentzhang.robotcontrol.utils.MathHelper;
import com.vincentzhang.robotcontrol.utils.Point2D;
import com.vincentzhang.robotcontrol.utils.Vector2D;

import java.util.List;

public class IKSolver2D {
    private final double degAngleDiff = 50;

    private double degServoTheta1;  // The angle of main servo
    private double degServoTheta2;  // The angle of secondary servo

    public double getDegServoTheta1() {
        return degServoTheta1;
    }

    public double getDegServoTheta2() {
        return degServoTheta2;
    }

    public double getDegTheta1(){
        return 180 - degServoTheta1; // servo_theta_1 = 180 - theta_1
    }

    public double calculateServoTheta2(double theta_2){
        return 180 - getDegTheta1() + theta_2 - degAngleDiff;
    }

    // Different from ServoTheta2!!!!
    public double getDegTheta2(){
        return degServoTheta2 - 180 + getDegTheta1() + degAngleDiff;
    }

    public boolean solveIK(Point2D target, double l1, double l2){
        Point2D c1 = new Point2D(0,0);
        List<Point2D> intersectPoints = MathHelper.circleInterect(c1,l1, target, l2);
        if(intersectPoints == null || intersectPoints.isEmpty()){
            return false;
        }

        // TODO: Make the motion more smooth.
        Point2D point = intersectPoints.get(0);
        Vector2D v1 = new Vector2D(point);
        Vector2D v2 = v1.minus(new Vector2D(target));

        double degTheta2 = v1.degAngle(v2);

        this.degServoTheta1 = 180 - Math.toDegrees(Math.atan2(point.getY(), point.getX()));
        this.degServoTheta2 = this.calculateServoTheta2(degTheta2);

        return true;
    }
}
