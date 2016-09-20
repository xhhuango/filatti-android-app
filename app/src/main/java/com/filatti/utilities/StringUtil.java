package com.filatti.utilities;

import java.text.DecimalFormat;

public final class StringUtil {
    public static String valueToString(double value) {
        return new DecimalFormat("#.##").format(value);
    }
}
