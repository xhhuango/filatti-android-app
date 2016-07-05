package com.fotro.effects.adjustments;

public class IllegalAdjustmentParameter extends Exception {
    public IllegalAdjustmentParameter(String adjustment,
                                      String parameter,
                                      Object rightValue,
                                      Object wrongValue) {
        super(adjustment + "." + parameter + " is " + wrongValue + " but should be " + rightValue);
    }

    public IllegalAdjustmentParameter(String adjustment,
                                      String parameter,
                                      Class rightType,
                                      Class wrongType) {
        super(adjustment + "." + parameter + " is type of " + wrongType.getSimpleName()
                      + " but should be type of " + rightType.getSimpleName());
    }
}
