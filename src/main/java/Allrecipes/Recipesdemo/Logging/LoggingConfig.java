package Allrecipes.Recipesdemo.Logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    /**
     * This method demonstrates how you might programmatically set logging levels at startup.
     * Usually, you would rely on application.properties or logback-spring.xml for configuration.
     */
    @PostConstruct
    public void initLogger() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Example: Set root logger to INFO
        loggerContext.getLogger("ROOT").setLevel(Level.INFO);

        // Example: Set package-specific logger to DEBUG
        loggerContext.getLogger("Allrecipes.Recipesdemo").setLevel(Level.DEBUG);
    }
}
