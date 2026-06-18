package org.mybatis.generator.ui.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtils {

  public static boolean isEmpty(final CharSequence cs) {
    return cs == null || cs.length() == 0;
  }

  public static boolean containsWhitespace(final CharSequence seq) {
    if (isEmpty(seq)) {
      return false;
    }
    final int strLen = seq.length();
    for (int i = 0; i < strLen; i++) {
      if (Character.isWhitespace(seq.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  public static boolean isAnyEmpty(final CharSequence... css) {
    if (isEmpty(css)) {
      return true;
    }
    for (final CharSequence cs : css) {
      if (isEmpty(cs)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isEmpty(final Object[] array) {
    return array == null || array.length == 0;
  }

  public static String getStackTrace(final Throwable throwable) {
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw, true);
    throwable.printStackTrace(pw);
    return sw.getBuffer().toString();
  }
}
