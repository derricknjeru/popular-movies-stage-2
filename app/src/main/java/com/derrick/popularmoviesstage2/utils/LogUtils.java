package com.derrick.popularmoviesstage2.utils;

import android.util.Log;

public class LogUtils {


    /**
     * Set to true to enable logging
     */
    public static final boolean IS_LOG_ENABLED = true;

    /**
     * To display debug message with tag when isDEBUG = true
     *
     * @param tag     LogTag
     * @param message Log message
     */
    public static void showDebugLog(String tag, String message) {
        if (IS_LOG_ENABLED)
            Log.d(tag, message + "");
    }

    /**
     * To display debug message with tag when isDEBUG = true
     *
     * @param tag     LogTag
     * @param message Log message
     */
    public static void showInformationLog(String tag, String message) {
        if (IS_LOG_ENABLED)
            Log.i(tag, message + "");
    }

    /**
     * To display debug message with tag when isDEBUG = true
     *
     * @param tag     LogTag
     * @param message Log message
     */
    public static void showErrorLog(String tag, String message) {
        if (IS_LOG_ENABLED)
            Log.e(tag, message + "");
    }

    /**
     * To display Exception message with "EXCEPTION" tag when isDEBUG = true
     *
     * @param exception Error message
     */
    public static void showException(Exception exception) {
        if (IS_LOG_ENABLED) {
            Log.e("EXCEPTION: ", exception.getMessage() + "");
            exception.printStackTrace();
        }
    }

    /**
     * To display debug message with tag when isDEBUG = true
     *
     * @param tag     LogTag
     * @param message Log message
     */
    public static void showLog(String tag, String message) {
        if (IS_LOG_ENABLED)
            Log.d(tag, message + "");
    }

    /**
     * Log a long log that would otherwise be truncated due to device allocated buffer-size or otherwise cause out of memory Exceptions(OOM)
     *
     * @param tag
     * @param message
     */
    public static void logLongLog(String tag, String message) {
        int maxLogSize = 800;
        for (int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            Log.v(tag, " longLog:: " + message.substring(start, end));
        }
    }
}
