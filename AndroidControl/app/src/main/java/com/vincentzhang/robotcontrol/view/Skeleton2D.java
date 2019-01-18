package com.vincentzhang.robotcontrol.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.vincentzhang.robotcontrol.utils.MathHelper;
import com.vincentzhang.robotcontrol.utils.Point2D;
import com.vincentzhang.robotcontrol.utils.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by VincentZhang on 1/15/2019.
 */

public class Skeleton2D extends Observable {
    private List<Bone> boneList = new ArrayList();
    private Point2D clampPosition = null;
    private int clampSize = 40;
    private boolean isClampMoving = false;
    private IKSolver2D ikSolver2D = new IKSolver2D();
    private double bone1Length = 0;
    private double bone2Length = 0;

    public boolean isClampMoving() {
        return isClampMoving;
    }

    public void setClampMoving(boolean clampMoving) {
        isClampMoving = clampMoving;
    }

    public Skeleton2D(double l1, double l2) {
        Bone bone1 = new Bone(new Point2D(0, 0), new Vector2D(1, 1), l1);
        boneList.add(bone1);
        Bone bone2 = new Bone(bone1.getEndPoint(), new Vector2D(1, -1), l2);
        boneList.add(bone2);

        setTheta(50, 60);

        bone1Length = l1;
        bone2Length = l2;
    }

    public void setTheta(double degTheta1, double degTheta2) {
        Bone bone1 = boneList.get(0);
        bone1.setBoneDir(new Vector2D(1, 0).rotateCCW(degTheta1));
        Bone bone2 = boneList.get(1);
        bone2.dragBone(bone1.getEndPoint());
        bone2.setBoneDir(bone1.getBoneDir().rotateCW(180 - degTheta2));

        setChanged();
        notifyObservers(ikSolver2D);
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
            clampPosition = bone.getEndPoint();
        }

        if (clampPosition != null) {
            float scrLastPointX = (float) MathHelper.getScreenX(scrWidth, clampPosition.getX());
            float scrLastPointY = (float) MathHelper.getScreenY(scrHeight, clampPosition.getY());
            if (this.isClampMoving) {
                paint.setColor(Color.BLUE);
            } else {
                paint.setColor(Color.BLACK);
            }
            canvas.drawCircle(scrLastPointX, scrLastPointY, clampSize, paint);
        }
    }

    public boolean insideObject(double x, double y) {
        return new Point2D(x, y).distance(clampPosition) <= clampSize;
    }

    public void setClampPosition(double x, double y) {
        if (ikSolver2D.solveIK(new Point2D(x, y), bone1Length, bone2Length)) {
            setTheta(ikSolver2D.getDegTheta1(), ikSolver2D.getDegTheta2());
        }
    }
}
