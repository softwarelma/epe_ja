package com.softwarelma.epe.p1.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EpeAppLogger {

    private static Logger logger;
    private static Logger loggerExternal;

    static {
        try {
            addLoggerInternal();
        } catch (EpeAppException e) {
            new EpeAppException("Adding internal logger", e);
        }
    }

    public static void addLoggerInternal() throws EpeAppException {
        EpeAppLogger.logger = LoggerFactory.getLogger(EpeAppLogger.class);
    }

    public static void removeLoggerInternal() throws EpeAppException {
        EpeAppLogger.logger = null;
    }

    public static void setLoggerExternal(Logger loggerExternal) throws EpeAppException {
        EpeAppUtils.checkNull("loggerExternal", loggerExternal);
        EpeAppLogger.loggerExternal = loggerExternal;
    }

    /**
     * default is INFO, see also .properties
     */
    public enum LEVEL {
        TRACE, DEBUG, INFO, WARN, ERROR, FATAL, FORCE
    }

    public static void logSystemOutPrintln(String message) {
        System.out.println(message);
    }

    public static void logSystemOutPrint(String message) {
        System.out.print(message);
    }

    public static void log(String message) {
        log(message, null, null, null);
    }

    public static void log(String message, Throwable t) {
        log(message, t, null, null);
    }

    public static void log(String message, Logger logger) {
        log(message, null, logger, null);
    }

    public static void log(String message, LEVEL level) {
        log(message, null, null, level);
    }

    public static void log(String message, Throwable t, Logger logger) {
        log(message, t, logger, null);
    }

    public static void log(String message, Throwable t, LEVEL level) {
        log(message, t, null, level);
    }

    public static void log(String message, Logger logger, LEVEL level) {
        log(message, null, logger, level);
    }

    public static void log(Throwable t) {
        log(null, t, null, null);
    }

    public static void log(Throwable t, Logger logger) {
        log(null, t, logger, null);
    }

    public static void log(Throwable t, LEVEL level) {
        log(null, t, null, level);
    }

    public static void log(Throwable t, Logger logger, LEVEL level) {
        log(null, t, logger, level);
    }

    public static void log(String[] arrayMessage, String messageTitle, Throwable t, Logger logger, LEVEL level) {
        if (arrayMessage == null || arrayMessage.length == 0) {
            log("", t, logger, level);
        }

        log(messageTitle, t, logger, level);

        for (String messageI : arrayMessage) {
            log(messageI, t, logger, level);
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

    public static void log(String message, Throwable t, Logger logger, LEVEL level) {
        message = message == null || message.isEmpty() ? "Message not found" : message;
        message = retrieveMessageWithThreadExceptionSuffix(message, t);
        logger = logger == null ? EpeAppLogger.logger : logger;
        logger = loggerExternal == null ? logger : loggerExternal;
        level = level == null ? LEVEL.INFO : level;

        if (!EpeAppConstants.SHOW_EXCEPTIONS && t != null) {
            return;
        }

        if (logger == null) {
            if (t == null) {
                logSystemOutPrintln(message);
            } else {
                logSystemOutPrintln(message);
                t.printStackTrace();
            }

            return;
        }

        switch (level) {
        case TRACE:
            if (t == null) {
                logger.trace(message);
            } else {
                logger.trace(message, t);
            }

            return;
        case DEBUG:
            if (t == null) {
                logger.debug(message);
            } else {
                logger.debug(message, t);
            }

            return;
        case INFO:
            if (t == null) {
                logger.info(message);
            } else {
                logger.info(message, t);
            }

            return;
        case WARN:
            if (t == null) {
                logger.warn(message);
            } else {
                logger.warn(message, t);
            }

            return;
        case ERROR:
            if (t == null) {
                logger.error(message);
            } else {
                logger.error(message, t);
            }

            return;
        case FATAL:
            // fatal, it can be configured with the markers if necessary

            if (t == null) {
                logger.error(message);
            } else {
                logger.error(message, t);
            }

            return;
        case FORCE:
            if (logger.isTraceEnabled()) {
                if (t == null) {
                    logger.trace(message);
                } else {
                    logger.trace(message, t);
                }
            } else if (logger.isDebugEnabled()) {
                if (t == null) {
                    logger.debug(message);
                } else {
                    logger.debug(message, t);
                }
            } else if (logger.isInfoEnabled()) {
                if (t == null) {
                    logger.info(message);
                } else {
                    logger.info(message, t);
                }
            } else if (logger.isWarnEnabled()) {
                if (t == null) {
                    logger.warn(message);
                } else {
                    logger.warn(message, t);
                }
            } else if (logger.isErrorEnabled()) {
                if (t == null) {
                    logger.error(message);
                } else {
                    logger.error(message, t);
                }
            }

            return;
        default:
            logger.error("Invalid level: " + level);
        }
    }

}
