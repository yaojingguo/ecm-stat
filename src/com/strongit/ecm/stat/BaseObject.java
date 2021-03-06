package com.strongit.ecm.stat;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BaseObject {
  /**
   * Use Java reflection to do <code>toString</code>.
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this,
                                              ToStringStyle.MULTI_LINE_STYLE);
  }
}