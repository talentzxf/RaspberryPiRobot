package com.vincentzhang.robotcontrol.view;

import com.vincentzhang.robotcontrol.utils.Point2D;
import com.vincentzhang.robotcontrol.utils.Vector2D;

public class Bone {
    private Point2D startPoint;
    private Point2D endPoint;

    public Bone(Point2D startPoint, Vector2D direction, double length) {
        this.startPoint = startPoint;
        this.endPoint = new Vector2D(startPoint).add(direction.normalize()).multiply(length);
    }

    public Point2D getEndPoint(){
        return endPoint;
    }

    public Point2D getStartPoint() {
        return startPoint;
    }
}
