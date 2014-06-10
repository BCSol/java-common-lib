/*
 *  SoSy-Lab Common is a library of useful utilities.
 *  This file is part of SoSy-Lab Common.
 *
 *  Copyright (C) 2007-2014  Dirk Beyer
 *  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.sosy_lab.common;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.sosy_lab.common.Appenders.AbstractAppender;

public class AppendersTest {

  private final Appender testAppender = new AbstractAppender() {
      @Override
      public void appendTo(Appendable out) throws IOException {
        out.append("123");
        out.append("456");
        out.append("7");
        out.append("8");
        out.append("9");
      }
    };

  @Test
  public void testToStringWithTruncation_NoLimit() {
    assertEquals("123456789", Appenders.toStringWithTruncation(testAppender, 100));
    assertEquals("123456789", Appenders.toStringWithTruncation(testAppender, 10));
    assertEquals("123456789", Appenders.toStringWithTruncation(testAppender, 9));
  }

  @Test
  public void testToStringWithTruncation_Limit() {
    assertEquals("12345678", Appenders.toStringWithTruncation(testAppender, 8));
    assertEquals("1234567",  Appenders.toStringWithTruncation(testAppender, 7));
    assertEquals("123456",   Appenders.toStringWithTruncation(testAppender, 6));
    assertEquals("12345",    Appenders.toStringWithTruncation(testAppender, 5));
    assertEquals("1234",     Appenders.toStringWithTruncation(testAppender, 4));
    assertEquals("123",      Appenders.toStringWithTruncation(testAppender, 3));
    assertEquals("12",       Appenders.toStringWithTruncation(testAppender, 2));
    assertEquals("1",        Appenders.toStringWithTruncation(testAppender, 1));
    assertEquals("",         Appenders.toStringWithTruncation(testAppender, 0));
  }
}
