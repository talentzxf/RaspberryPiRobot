package com.vincentzhang.robotcontrol.utils;

public class Point2D {
    protected double x;
    protected double y;

    public Point2D() {
    }

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D p) {
        this.x = p.x;
        this.y = p.y;
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

    public double manhattanDistance(Point2D p){
        return Math.abs(x-p.x) + Math.abs(y-p.y);
    }
}
