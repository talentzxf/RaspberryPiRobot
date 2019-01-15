package com.vincentzhang.robotcontrol.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VincentZhang on 1/14/2019.
 */

public class MathHelper {
    static public final double EPS = 0.001;

    static public class Point2D {
        protected double x;
        protected double y;

        public Point2D() {
        }

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double distance(Point2D p) {
            return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
        }
    }

    static public class Vector2D extends Point2D {
        public Vector2D(double x, double y) {
            super(x, y);
        }

        public Vector2D(Point2D point) {
            super(point.x, point.y);
        }

        public Vector2D add(Vector2D p) {
            return new Vector2D(p.x + this.x, p.y + this.y);
        }

        public Vector2D negative() {
            return new Vector2D(-this.x, -this.y);
        }

        public Vector2D minus(Vector2D p) {
            return this.add(p.negative());
        }

        public double length() {
            return Math.sqrt(this.x * this.x + this.y * this.y);
        }

        public Vector2D normalize() {
            double vLength = length();
            return new Vector2D(this.x / vLength, this.y / vLength);
        }

        public double dotProduct(Vector2D p) {
            return this.x * p.x + this.y * p.y;
        }

        public double degAngle(Vector2D v) {
            double dot = dotProduct(v);
            double radCos = dot / (length() * v.length());

            return Math.toDegrees(Math.acos(radCos));
        }
    }

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
}
