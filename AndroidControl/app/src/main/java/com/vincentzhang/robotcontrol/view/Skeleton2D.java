package com.vincentzhang.robotcontrol.view;

import com.vincentzhang.robotcontrol.utils.MathHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VincentZhang on 1/15/2019.
 */
enum PointType {
    START,
    END
}

public class Skeleton2D {
    public static class Bone {
        private double length; // Once set, this should not change
        private Map<PointType, MathHelper.Point2D> endPointMapping = new HashMap();
        private Map<PointType, Boolean> pointIsFixed = new HashMap<>();

        public void setEndPoint(PointType tag, MathHelper.Point2D point) {
            endPointMapping.put(tag, point);
            pointIsFixed.put(tag, false); // By default, all points are flexible

            if (endPointMapping.get(PointType.START) != null &&
                    endPointMapping.get(PointType.END) != null) {
                MathHelper.Point2D start = endPointMapping.get(PointType.START);
                MathHelper.Point2D end = endPointMapping.get(PointType.END);
                length = end.distance(start);
            }
        }

        public void pinPoint(PointType tag) {
            pointIsFixed.put(tag, true);
        }

        public MathHelper.Point2D getEndPoint(PointType tag) {
            return endPointMapping.get(tag);
        }

        // Once connected, my points will be flexible.
        public void connect(Bone bone2, PointType tag, PointType myTag) {
            endPointMapping.put(myTag, bone2.getEndPoint(tag));

            for (PointType type : pointIsFixed.keySet()) {
                pointIsFixed.put(type, false);
            }
        }
    }

    Bone bone1 = new Bone();
    Bone bone2 = new Bone();

    public Skeleton2D() {
        bone1.setEndPoint(PointType.START, new MathHelper.Point2D(0, 0));
        bone1.setEndPoint(PointType.START, new MathHelper.Point2D(0, 0));

    }

}
