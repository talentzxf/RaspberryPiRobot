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

    public Skeleton2D(double l1, double l2) {
        Bone bone1 = new Bone(new Point2D(0, 0), new Vector2D(1, 1), l1);
        boneList.add(bone1);
        Bone bone2 = new Bone(bone1.getEndPoint(), new Vector2D(1, -1), l2);
        boneList.add(bone2);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        paint.setStrokeWidth((float) 1.0);

        int scrWidth = canvas.getWidth();
        int scrHeight = canvas.getHeight();



        for(Bone bone: boneList){
            canvas.drawLine((float)MathHelper.getScreenX(scrWidth, bone.getStartPoint().getX()),
                    (float)MathHelper.getScreenX(scrHeight, bone.getStartPoint().getY()),
                    (float)MathHelper.getScreenX(scrWidth, bone.getEndPoint().getX()),
                    (float)MathHelper.getScreenX(scrHeight, bone.getEndPoint().getY()), paint);
            paint.setStrokeWidth(5.0f);
        }
    }
}
