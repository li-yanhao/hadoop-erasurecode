/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.io.erasurecode.coder;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.io.erasurecode.CodecUtil;
import org.apache.hadoop.io.erasurecode.ECBlock;
import org.apache.hadoop.io.erasurecode.ECBlockGroup;
import org.apache.hadoop.io.erasurecode.ErasureCodeConstants;
import org.apache.hadoop.io.erasurecode.ErasureCoderOptions;
import org.apache.hadoop.io.erasurecode.rawcoder.RawErasureEncoder;

/**
 * Reed-Solomon erasure encoder that encodes a block group.
 *
 * It implements {@link ErasureCoder}.
 */
@InterfaceAudience.Private
public class LRCErasureEncoder extends ErasureEncoder {
  private RawErasureEncoder rawEncoder;

  public LRCErasureEncoder(ErasureCoderOptions options) {
    super(options);
  }

  @Override
  protected ErasureCodingStep prepareEncodingStep(final ECBlockGroup blockGroup) {

    RawErasureEncoder rawEncoder = checkCreateLRCRawEncoder();

    ECBlock[] inputBlocks = getInputBlocks(blockGroup);

    return new ErasureEncodingStep(inputBlocks,
        getOutputBlocks(blockGroup), rawEncoder);
  }

  private RawErasureEncoder checkCreateLRCRawEncoder() {
    if (rawEncoder == null) {
      // TODO: we should create the raw coder according to codec.
      rawEncoder = CodecUtil.createRawEncoder(getConf(),
          ErasureCodeConstants.LRC_CODEC_NAME, getOptions());
    }
    return rawEncoder;
  }

  @Override
  public void release() {
    if (rawEncoder != null) {
      rawEncoder.release();
    }
  }

  @Override
  public boolean preferDirectBuffer() {
    return false;
  }
}
