package org.mybatis.generator.logging.log4j2;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

public class LogExtendedLoggerWrapper extends ExtendedLoggerWrapper {
  private static final long serialVersionUID = -7161795014237720199L;

  public LogExtendedLoggerWrapper(ExtendedLogger logger, String name,
      MessageFactory messageFactory) {
    super(logger, name, messageFactory);
  }

  public void logIfEnabledExtended(final String fqcn, final Level level, final Marker marker,
      final Message msg, final Throwable t) {
    this.logIfEnabled(fqcn, Level.ERROR, marker, msg, t);
  }
}
