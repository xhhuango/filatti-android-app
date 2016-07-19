package com.filatti.effects.adjusts;

import com.filatti.effects.Effect;

import org.opencv.android.OpenCVLoader;

public abstract class Adjust implements Effect {
    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("filatti");
    }
}
