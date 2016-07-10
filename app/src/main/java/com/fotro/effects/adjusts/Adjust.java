package com.fotro.effects.adjusts;

import com.fotro.effects.Effect;

import org.opencv.android.OpenCVLoader;

public abstract class Adjust implements Effect {
    static {
        System.loadLibrary("opencv_java3");
        OpenCVLoader.initDebug();
    }
}
