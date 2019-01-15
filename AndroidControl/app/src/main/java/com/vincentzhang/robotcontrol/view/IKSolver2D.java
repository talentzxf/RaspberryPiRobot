package com.vincentzhang.robotcontrol.view;

import com.vincentzhang.robotcontrol.utils.MathHelper;

import java.util.List;

public class IKSolver2D {
    private final double degAngleDiff = 50;

    /**
     *
     *
     *
     *        /------- theta_2
     *       /
     *      /\
     *     /  \  degServoTheta2
     *    /    \-----
     *   /  degServoTheta1(deg_theta_1)
     *   ---------------
     *
     */

    double degServoTheta1;  // The angle of main servo
    double degServoTheta2;  // The angle of secondary servo

    double getDegTheta1(){
        return degServoTheta1; // servo_theta_1 is the same as theta_1
    }

    double calculateServoTheta2(double theta_2){
        return 180 - getDegTheta1() + theta_2 - degAngleDiff;
    }

    double getTheta2FromServoTheta2(){
        return degServoTheta2 - 180 + getDegTheta1() + degAngleDiff;
    }

    boolean solveIK(MathHelper.Point2D target, double l1, double l2){
        MathHelper.Point2D c1 = new MathHelper.Point2D(0,0);
        List<MathHelper.Point2D> intersectPoints = MathHelper.circleInterect(c1,l1, target, l2);
        if(intersectPoints.isEmpty()){
            return false;
        }

        // TODO: Make the motion more smooth.
        MathHelper.Point2D point = intersectPoints.get(0);
        MathHelper.Vector2D v1 = new MathHelper.Vector2D(point);
        MathHelper.Vector2D v2 = v1.minus(new MathHelper.Vector2D(target));

        double degTheta2 = v1.degAngle(v2);
        double degTheta1 = 180 - Math.toDegrees(Math.atan2(point.getY(), point.getX()));
        
        this.degServoTheta1 = degTheta1;
        this.calculateServoTheta2(degTheta2);

        return true;
    }
}
