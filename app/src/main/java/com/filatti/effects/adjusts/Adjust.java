package com.filatti.effects.adjusts;

import com.filatti.effects.Effect;
import com.filatti.utils.Copyable;

public abstract class Adjust implements Effect, Copyable<Adjust> {
    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("filatti");
    }
}
