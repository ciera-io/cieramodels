package logger.util;

import io.ciera.runtime.api.domain.Domain;

public class Logger {

    private final io.ciera.runtime.api.application.Logger logger;

    public Logger(Domain domain) {
        logger = domain.getApplication().getLogger();
    }

    public void notice(final String p_message) {
        logger.info(p_message);
    }

    public void trace(final String p_message) {
        logger.trace(p_message);
    }

    public void error(final String p_message) {
        logger.error(p_message);
    }

    public void fatal(final String p_message) {
        logger.error(p_message);
    }

    public void critical(final String p_message) {
        logger.error(p_message);
    }

    public void warning(final String p_message) {
        logger.warn(p_message);
    }

    public void debug(final String p_message) {
        logger.debug(p_message);
    }

    public void information(final String p_message) {
        logger.info(p_message);
    }

}
