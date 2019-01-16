package com.vincentzhang.robotcontrol.utils;

public class Vector2D extends Point2D {
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

    public Vector2D multiply(double length) {
        return new Vector2D(x*length, y*length);
    }

    public Vector2D rotateCCW(double degDegree){
        double rad = Math.toRadians(degDegree);
        double sine = Math.sin(rad);
        double cosine = Math.cos(rad);

        double x_after = x * cosine - y * sine;
        double y_after = y * cosine + x * sine;

        return new Vector2D(x_after, y_after);
    }

    public Vector2D rotateCW(double degDegree) {
        return this.rotateCCW(-degDegree);
    }
}
