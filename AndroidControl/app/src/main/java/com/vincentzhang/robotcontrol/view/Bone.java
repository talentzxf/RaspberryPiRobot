package com.vincentzhang.robotcontrol.view;

import com.vincentzhang.robotcontrol.utils.Point2D;
import com.vincentzhang.robotcontrol.utils.Vector2D;

public class Bone {
    private Point2D startPoint;
    private Point2D endPoint;

    public Bone(Point2D startPoint, Vector2D direction, double length) {
        this.startPoint = new Point2D(startPoint);
        this.endPoint = new Vector2D(startPoint).add(direction.normalize().multiply(length));
    }

    public Point2D getEndPoint(){
        return endPoint;
    }

    public Point2D getStartPoint() {
        return startPoint;
    }

    public double getBoneLength(){
        return startPoint.distance(endPoint);
    }

    public Vector2D getBoneDir(){
        return new Vector2D(endPoint).minus(new Vector2D(startPoint)).normalize();
    }

    public void setBoneDir(Vector2D newDir){
        double boneLength = getBoneLength();

        endPoint = (new Vector2D(startPoint)).add(newDir.normalize().multiply(boneLength));
    }

    /**
     * Drag the start point of the bone to the targetPoint. Keep length and dir
     * @param targetPoint
     */
    public void dragBone(Point2D targetPoint) {
        double boneLength = getBoneLength();
        Vector2D boneDir = getBoneDir();

        this.startPoint = targetPoint;
        this.endPoint = new Vector2D(this.startPoint).add(boneDir.multiply(boneLength));
    }
}
