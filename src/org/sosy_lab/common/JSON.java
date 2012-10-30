/*
 *  SoSy-Lab Common is a library of useful utilities.
 *  This file is part of SoSy-Lab Common.
 *
 *  Copyright (C) 2007-2012  Dirk Beyer
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

import java.io.File;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.io.Closeables;
import com.google.common.io.Files;

/**
 * This class is based on code from the library JSON.simple in version 1.1
 * (https://code.google.com/p/json-simple/)
 * by Fang Yidong <fangyidong@yahoo.com.cn>.
 * The license is the Apache 2.0 License (http://www.apache.org/licenses/LICENSE-2.0.txt).
 *
 * Significant performance improvements were made compared to the library.
 */
public class JSON {

  /**
   * Encode an object into JSON text and write it to a file.
   * @throws IOException
   */
  public static void writeJSONString(Object value, File file, Charset charset) throws IOException {
    Files.createParentDirs(file);
    Writer out = Files.newWriter(file, charset);
    try {
      writeJSONString(value, out);
    } finally {
      Closeables.closeQuietly(out);
    }
  }

  /**
   * Encode an object into JSON text and write it to out.
   * @throws IOException
   */
  public static void writeJSONString(Object value, Appendable out) throws IOException {
    if (value == null) {
      out.append("null");

    } else if (value instanceof String) {
      out.append('\"');
      escape((String)value, out);
      out.append('\"');

    } else if (value instanceof Double) {
      if (((Double)value).isInfinite() || ((Double)value).isNaN())
        out.append("null");
      else
        out.append(value.toString());

    } else if (value instanceof Float) {
      if (((Float)value).isInfinite() || ((Float)value).isNaN())
        out.append("null");
      else
        out.append(value.toString());

    } else if (value instanceof Number) {
      out.append(value.toString());

    } else if (value instanceof Boolean) {
      out.append(value.toString());

    } else if (value instanceof Map<?,?>) {
      writeJSONString((Map<?,?>)value, out);

    } else if (value instanceof List<?>) {
      writeJSONString((List<?>)value, out);

    } else {
      throw new NotSerializableException("Object of class " + value.getClass().getName() + " cannot be written as JSON");
    }
  }

  /**
   * Encode a list into JSON text and write it to out.
   */
  private static void writeJSONString(List<?> list, Appendable out) throws IOException {
    boolean first = true;
    Iterator<?> iter = list.iterator();

    out.append('[');
    while (iter.hasNext()) {
      if (first) {
        first = false;
      } else {
        out.append(',');
      }

      Object value = iter.next();
      if (value == null) {
        out.append("null");
        continue;
      }

      JSON.writeJSONString(value, out);
    }
    out.append(']');
  }


  /**
   * Encode a map into JSON text and write it to out.
   */
  private static <K,V> void writeJSONString(Map<K,V> map, Appendable out) throws IOException {
    boolean first = true;
    Iterator<Map.Entry<K,V>> iter = map.entrySet().iterator();

    out.append('{');
    while (iter.hasNext()) {
      if (first) {
        first = false;
      } else {
        out.append(',');
      }
      Map.Entry<K,V> entry = iter.next();
      out.append('\"');
      escape(String.valueOf(entry.getKey()), out);
      out.append('\"');
      out.append(':');
      writeJSONString(entry.getValue(), out);
    }
    out.append('}');
  }

  /**
   * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
   * @param s Must not be null.
   * @param out
   */
  private static void escape(String s, Appendable out) throws IOException {
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      switch(ch) {
      case '"':
        out.append("\\\"");
        break;
      case '\\':
        out.append("\\\\");
        break;
      case '\b':
        out.append("\\b");
        break;
      case '\f':
        out.append("\\f");
        break;
      case '\n':
        out.append("\\n");
        break;
      case '\r':
        out.append("\\r");
        break;
      case '\t':
        out.append("\\t");
        break;
      case '/':
        out.append("\\/");
        break;
      default:
        //Reference: http://www.unicode.org/versions/Unicode5.1.0/
        if ((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')) {
          String ss = Integer.toHexString(ch);
          out.append("\\u");
          for (int k=0;k<4-ss.length();k++) {
            out.append('0');
          }
          out.append(ss.toUpperCase());
        } else {
          out.append(ch);
        }
      }
    }//for
  }

}