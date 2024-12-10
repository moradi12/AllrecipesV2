package Allrecipes.Recipesdemo.Logging;

import org.slf4j.Logger;

public class LogUtils {

    /**
     * Log an informational message with a given logger and a simple format.
     */
    public static void logInfo(Logger logger, String message) {
        if (logger.isInfoEnabled()) {
            logger.info("[INFO] {}", message);
        }
    }

    /**
     * Log a debug message with a given logger and a simple format.
     */
    public static void logDebug(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug("[DEBUG] {}", message);
        }
    }

    /**
     * Log a warning message with a given logger and a simple format.
     */
    public static void logWarn(Logger logger, String message) {
        logger.warn("[WARN] {}", message);
    }

    /**
     * Log an error message with a given logger and a simple format.
     */
    public static void logError(Logger logger, String message, Throwable t) {
        logger.error("[ERROR] {}", message, t);
    }

    /**
     * Utility method to format messages consistently before logging.
     */
    public static String formatLogMessage(String action, String entity, Object identifier, String details) {
        return String.format("Action=%s, Entity=%s, Id=%s, Details=%s", action, entity, identifier, details);
    }
}
