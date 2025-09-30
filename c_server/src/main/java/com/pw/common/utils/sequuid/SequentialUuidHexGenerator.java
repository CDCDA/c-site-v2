package com.pw.common.utils.sequuid;

public class SequentialUuidHexGenerator extends AbstractUUIDGenerator {
    private static final String sep = "-";

    public static synchronized String generate() {
        return
                format(getJVM()) + sep
                        + format(getHiTime()) + sep
                        + format(getLoTime()) + sep
                        + format(getIP()) + sep
                        + format(getCount());
    }

    protected static synchronized String format(int intValue) {
        String formatted = Integer.toHexString(intValue);
        StringBuilder buf = new StringBuilder("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    protected static synchronized String format(short shortValue) {
        String formatted = Integer.toHexString(shortValue);
        StringBuilder buf = new StringBuilder("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }
}
