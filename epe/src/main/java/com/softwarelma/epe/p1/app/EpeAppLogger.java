package com.softwarelma.epe.p1.app;

import java.io.File;

import com.softwarelma.epe.p2.encodings.EpeEncodings;

public abstract class EpeAppLogger {

    static {
        if (EpeAppConstants.LOG_FILE_NAME != null && !EpeAppConstants.LOG_FILE_APPEND) {
            File fileLog = new File(EpeAppConstants.LOG_FILE_NAME);
            if (fileLog.isFile())
                fileLog.delete();
        }
    }

    /**
     * default is INFO, see also .properties
     */
    public enum LEVEL {
        TRACE, DEBUG, INFO, WARN, ERROR, FATAL
    }

    public static void logSystemOutPrintln(String message) {
        // the only valid println
        System.out.println(message);
    }

    public static void logSystemOutPrint(String message) {
        System.out.print(message);
    }

    public static void log(String message) {
        log(message, null, null);
    }

    public static void log(Throwable t) {
        log(t.getMessage(), t, null);
    }

    public static void log(String message, Throwable t) {
        log(message, t, null);
    }

    public static void log(String message, LEVEL level) {
        log(message, null, level);
    }

    public static void log(String message, Throwable t, LEVEL level) {
        log(message, t, level, true, false);
    }

    public static void log(String message, Throwable t, LEVEL level, boolean console, boolean messageAsIs) {
        if (!EpeAppConstants.SHOW_EXCEPTIONS && t != null)
            return;
        String timestamp = "???";
        level = level == null ? EpeAppConstants.LOG_LEVEL : level;

        try {
            timestamp = EpeAppUtils.retrieveTimestamp(EpeAppConstants.TIMESTAMP_DEFAULT_FORMAT);
        } catch (EpeAppException e) {
            e.printStackTrace();
        }

        if (!messageAsIs) {
            message = message == null || message.isEmpty() ? "Message not found" : message;
            message = retrieveMessageWithThreadExceptionSuffix(message, t);
            message = timestamp + " " + level + ": " + message;
        }

        if (level.ordinal() < EpeAppConstants.LOG_LEVEL.ordinal())
            return;

        if (EpeAppConstants.LOG_CONSOLE && console) {
            logSystemOutPrintln(message);
            if (t != null)
                t.printStackTrace();
        }

        if (EpeAppConstants.LOG_FILE_NAME != null) {
            EpeEncodings enc = new EpeEncodings();
            StringBuilder sb = new StringBuilder(message);
            sb.append("\n");

            if (t != null) {
                sb.append(t.getClass().getName());
                sb.append(": ");
                sb.append(t.getMessage());
                sb.append("\n");

                for (StackTraceElement elem : t.getStackTrace()) {
                    sb.append("\tat ");
                    sb.append(elem.toString());
                    sb.append("\n");
                }
            }

            try {
                enc.write(sb.toString(), EpeAppConstants.LOG_FILE_NAME, EpeAppConstants.LOG_FILE_ENCODING, true);
            } catch (EpeAppException e) {
                e.printStackTrace();
            }

        }
    }

    private static String retrieveMessageWithThreadExceptionSuffix(String message, Throwable t) {
        if (t != null) {
            Long threadId = Thread.currentThread().getId();
            String exceptionSuffix = EpeAppConstants.mapThreadIdAndExceptionSuffix.remove(threadId);
            exceptionSuffix = exceptionSuffix == null ? "" : "\nLAST SENT ON EXECUTION: " + exceptionSuffix;
            message = message + exceptionSuffix;
        }

        return message;
    }

    public static void logToFile(LEVEL level, String message) {
        try {
            String timestamp = EpeAppUtils.retrieveTimestamp(EpeAppConstants.TIMESTAMP_DEFAULT_FORMAT);
            EpeEncodings enc = new EpeEncodings();
            StringBuilder sb = new StringBuilder();
            sb.append(timestamp);
            sb.append(" ");
            sb.append(level);
            sb.append(": ");
            sb.append(message);
            sb.append("\n");
            enc.write(sb.toString(), ".tcm2.log", "UTF-8", true);
        } catch (EpeAppException e) {
        }
    }

    public static void log(String[] arrayMessage, String messageTitle, Throwable t, LEVEL level) {
        if (arrayMessage == null || arrayMessage.length == 0) {
            log("", t, level);
        }

        log(messageTitle, t, level);

        for (String messageI : arrayMessage) {
            log(messageI, t, level);
        }
    }

}
