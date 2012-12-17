import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.status.OnConsoleStatusListener
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.ERROR

def CATALINA_BASE = System.getProperty("catalina.base")//Obtained from Tomcat within it's distribution i.e. from catalina.sh

statusListener(OnConsoleStatusListener)//Default and required
scan("24 hours") //Scan for changes in this file in every 24 hours and reload it if there are any changes.

appender("FILE", RollingFileAppender) {//All info messages go here i.e. in messaging.log
    file = "${CATALINA_BASE}/logs/messaging.log"
    append = true
    rollingPolicy(TimeBasedRollingPolicy) {//Roll every day starting new file at midnight
        FileNamePattern = "${CATALINA_BASE}/logs/messaging.%d.log"
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%d %-5.5p %-32.32logger{32} - %message %rEx{full} %n"
    }
}

appender("DEBUG-LOGS", RollingFileAppender) {//All DEBUG messages go here i.e. in debug-messaging.log
    file = "${CATALINA_BASE}/logs/debug-messaging.log"
    append = true
    rollingPolicy(TimeBasedRollingPolicy) {//Roll every day starting new file at midnight
        FileNamePattern = "${CATALINA_BASE}/logs/debug-messaging.%d.log"
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%d %-5.5p %-32.32logger{32} - %message %rEx{full} %n"
    }
}

root(INFO, ["FILE"])
logger("com.telenor.cos.messaging", DEBUG, ["DEBUG-LOGS"], false)
logger("com.telenor.cos.messaging", ERROR, ["FILE", "DEBUG-LOGS"])
logger("org.springframework", INFO)
logger("org.apache.activemq", INFO)
logger("org.apache.camel", INFO)