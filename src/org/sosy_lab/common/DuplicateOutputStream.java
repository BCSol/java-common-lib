/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2010  Dirk Beyer
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
 *
 *
 *  CPAchecker web page:
 *    http://cpachecker.sosy-lab.org
 */
package org.sosy_lab.common;

import java.io.IOException;
import java.io.OutputStream;

import com.google.common.base.Preconditions;

/**
 * This class is an OutputStream implementation that sends everything to two
 * other OutputStreams.
 * 
 * Exceptions thrown by any of the streams will be relayed to the caller.
 * 
 * @author Philipp Wendler
 */
public class DuplicateOutputStream extends OutputStream {

  private final OutputStream stream1;
  private final OutputStream stream2;    
  
  public DuplicateOutputStream(OutputStream pStream1, OutputStream pStream2) {
    stream1 = Preconditions.checkNotNull(pStream1);
    stream2 = Preconditions.checkNotNull(pStream2);
  }

  @Override
  public void write(int pB) throws IOException {
    try {
      stream1.write(pB);
    } finally {
      stream2.write(pB);
    }
  }

  @Override
  public void write(byte[] pB) throws IOException {
    try {
      stream1.write(pB);
    } finally {
      stream2.write(pB);
    }
  }

  @Override
  public void write(byte[] pB, int pOff, int pLen) throws IOException {
    try {
    stream1.write(pB, pOff, pLen);
    } finally {
      stream2.write(pB, pOff, pLen);
    }
  }

  @Override
  public void flush() throws IOException {
    try {
      stream1.flush();
    } finally {
      stream2.flush();
    }
  }

  @Override
  public void close() throws IOException {
    try {
      stream1.close();
    } finally {
      stream2.close();
    }
  }
}