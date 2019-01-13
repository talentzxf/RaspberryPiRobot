package com.vincentzhang.robotcontrol.view;

public class IKSolver2D {
    /**
     *
     *
     *
     *        /------- theta_2
     *       /
     *      /\
     *     /  \  servo_theta_2
     *    /    \-----
     *   /  servo_theta_1(theta_1)
     *   ---------------
     *
     */

    float servo_theta_1;  // The angle of main servo
    float servo_theta_2;  // The angle of secondary servo

    float get_theta_1(){
        return servo_theta_1; // servo_theta_1 is the same as theta_1
    }

    float get_theta_2(){
        S
    }

}
