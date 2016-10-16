package com.filatti.effects.adjusts;

public enum Tone {
    SHADOWS(0),
    MIDTONES(1),
    HIGHLIGHTS(2);

    private final int mValue;

    Tone(int value) {
        mValue = value;
    }

    public int toInt() {
        return mValue;
    }
}
