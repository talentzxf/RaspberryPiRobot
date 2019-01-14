package com.vincentzhang.robotcontrol.view;

public class IKSolver2D {
    private final float degAngleDiff = 50;

    /**
     *
     *
     *
     *        /------- theta_2
     *       /
     *      /\
     *     /  \  deg_servo_theta_2
     *    /    \-----
     *   /  deg_servo_theta_1(deg_theta_1)
     *   ---------------
     *
     */

    float deg_servo_theta_1;  // The angle of main servo
    float deg_servo_theta_2;  // The angle of secondary servo

    float get_deg_theta_1(){
        return deg_servo_theta_1; // servo_theta_1 is the same as theta_1
    }

    float get_theta_2(){
        return 180 - get_deg_theta_1() + deg_servo_theta_2 - degAngleDiff;
    }


}
