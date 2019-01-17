package com.vincentzhang.robotcontrol.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VincentZhang on 1/14/2019.
 */

public class MathHelper {
    static public final double EPS = 0.001;

    static public class QuadraticEquationSolver {
        private double A = 0.0f;
        private double B = 0.0f;
        private double C = 0.0f;

        // Ax^2+Bx+C=0
        public QuadraticEquationSolver(double A, double B, double C) {
            this.A = A;
            this.B = B;
            this.C = C;
        }

        public double det() {
            return B * B - 4 * A * C;
        }

        public List<Double> solve() {
            List<Double> retValues = new ArrayList();

            double detNow = det();
            if (detNow < 0) {
                return null;
            } else if (Math.abs(detNow) <= EPS) {
                retValues.add(-B / (2 * A));
            } else {
                retValues.add((-B + Math.sqrt(det())) / (2 * A));
                retValues.add((-B - Math.sqrt(det())) / (2 * A));
            }
            return retValues;
        }

        public boolean verifySolution(double solution) {
            return Math.abs(A * solution * solution + B * solution + C) <= EPS;
        }
    }

    public static List<Point2D> circleInterect(Point2D c1, double r1, Point2D c2, double r2) {
        double D = c1.x + c2.x;
        double E = c2.x - c1.x;
        double F = c1.y + c2.y;
        double G = c2.y - c1.y;
        double H = r1 * r1 - r2 * r2;

        double I = -G / E;
        double J = H / (2 * E) + F * G / (2 * E) + D / 2;
        double K = J - c1.x;

        double A = I * I + 1;
        double B = 2 * K * I - 2 * c1.y;
        double C = K * K + c1.y * c1.y - r1 * r1;

        QuadraticEquationSolver qes = new QuadraticEquationSolver(A, B, C);

        List<Point2D> retPoints = new ArrayList<>();
        List<Double> solutions = qes.solve();

        if (solutions == null) {
            return null;
        }
        if (solutions.size() == 1) {
            double y1 = solutions.get(0);
            double x1 = I * y1 + J;

            retPoints.add(new Point2D(x1, y1));
        } else {
            double y1 = solutions.get(0);
            double x1 = I * y1 + J;

            double y2 = solutions.get(1);
            double x2 = I * y2 + J;
            retPoints.add(new Point2D(x1, y1));
            retPoints.add(new Point2D(x2, y2));
        }

        return retPoints;
    }

    public static boolean pointOnCircle(Point2D circleCenter, double r, Point2D targetPoint) {
        double distance = circleCenter.distance(targetPoint);
        return Math.abs(distance - r) < EPS;
    }

    public static double getScreenX(double screenWidth, double worldX) {
        return worldX + screenWidth / 6;
    }

    public static double getScreenY(double screenHeight, double worldY) {
        return screenHeight * 5 / 6 - worldY;
    }

    public static double getWorldX(double screenWidth, double scrX) {
        return scrX - screenWidth / 6;
    }

    public static double getWorldY(double screenHeight, double scrY) {
        return screenHeight * 5 / 6 - scrY;
    }
}
