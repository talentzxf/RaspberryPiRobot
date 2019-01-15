package com.vincentzhang.robotcontrol.view;

import com.vincentzhang.robotcontrol.utils.Point2D;
import com.vincentzhang.robotcontrol.utils.Vector2D;

public class Bone {
    private Point2D startPoint;
    private Point2D endPoint;

    public Bone(Point2D startPoint, Vector2D direction, double length) {
        this.startPoint = startPoint;
        this.endPoint = new Vector2D(startPoint).add(direction.normalize().multiply(length));
    }

    public Point2D getEndPoint(){
        return endPoint;
    }

    public Point2D getStartPoint() {
        return startPoint;
    }

    public void setDegTheta(double degTheta) {
        double length = startPoint.distance(endPoint);
        double rad = Math.toRadians(degTheta);

        endPoint = new Point2D(startPoint.getX() * Math.cos(rad) * length,
                startPoint.getY() * Math.sin(rad)*length);
    }
}
