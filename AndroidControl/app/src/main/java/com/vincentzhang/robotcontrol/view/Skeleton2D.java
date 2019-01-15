package com.vincentzhang.robotcontrol.view;

import com.vincentzhang.robotcontrol.utils.Point2D;
import com.vincentzhang.robotcontrol.utils.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VincentZhang on 1/15/2019.
 */

public class Skeleton2D {
    public static class Bone {
        private Point2D startPoint;
        private Point2D endPoint;

        public Bone(Point2D startPoint, Vector2D direction, double length) {
            endPoint = new Vector2D(startPoint).add(direction.normalize()).multiply(length);
        }

        public Point2D getEndPoint(){
            return endPoint;
        }
    }

    private List<Bone> boneList = new ArrayList();

    public Skeleton2D() {
        Bone bone1 = new Bone(new Point2D(0,0), new Vector2D(1,1), 10);
        boneList.add(bone1);
        Bone bone2 = new Bone(bone1.getEndPoint(), new Vector2D(1,-1), 10);
        boneList.add(bone2);
    }

}
