package com.vincentzhang.robotcontrol.view;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by VincentZhang on 1/17/2019.
 */

public interface SkeletonChangeListener extends Observer{
    @Override
    void update(Observable o, Object arg);
}
