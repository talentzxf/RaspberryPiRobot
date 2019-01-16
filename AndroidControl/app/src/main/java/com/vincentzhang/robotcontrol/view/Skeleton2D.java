package com.vincentzhang.robotcontrol.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.vincentzhang.robotcontrol.utils.MathHelper;
import com.vincentzhang.robotcontrol.utils.Point2D;
import com.vincentzhang.robotcontrol.utils.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VincentZhang on 1/15/2019.
 */

public class Skeleton2D {
    private List<Bone> boneList = new ArrayList();
    private Point2D lastPoint = null;

    public Skeleton2D(double l1, double l2) {
        Bone bone1 = new Bone(new Point2D(0, 0), new Vector2D(1, 1), l1);
        boneList.add(bone1);
        Bone bone2 = new Bone(bone1.getEndPoint(), new Vector2D(1, -1), l2);
        boneList.add(bone2);

        setTheta(50, 60);
    }

    public void setTheta(double degTheta1, double degTheta2) {
        Bone bone1 = boneList.get(0);
        bone1.setBoneDir(new Vector2D(1, 0).rotateCCW(degTheta1));
        Bone bone2 = boneList.get(0);
        bone2.dragBone(bone1.getEndPoint());
        bone2.setBoneDir(bone1.getBoneDir().rotateCW(180 - degTheta2));
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        paint.setStrokeWidth((float) 5.0);

        int scrWidth = canvas.getWidth();
        int scrHeight = canvas.getHeight();

        for (Bone bone : boneList) {
            float scrPoint1X = (float) MathHelper.getScreenX(scrWidth, bone.getStartPoint().getX());
            float scrPoint1Y = (float) MathHelper.getScreenY(scrHeight, bone.getStartPoint().getY());
            float scrPoint2X = (float) MathHelper.getScreenX(scrWidth, bone.getEndPoint().getX());
            float scrPoint2Y = (float) MathHelper.getScreenY(scrHeight, bone.getEndPoint().getY());
            canvas.drawLine(scrPoint1X, scrPoint1Y, scrPoint2X, scrPoint2Y, paint);
            lastPoint = bone.getEndPoint();
        }

        if (lastPoint != null) {
            float scrLastPointX = (float) MathHelper.getScreenX(scrWidth, lastPoint.getX());
            float scrLastPointY = (float) MathHelper.getScreenY(scrHeight, lastPoint.getY());
            canvas.drawCircle(scrLastPointX, scrLastPointY, 20, paint);
        }
    }
}
