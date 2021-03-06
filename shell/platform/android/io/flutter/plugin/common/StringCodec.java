// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugin.common;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * A {@link MessageCodec} using UTF-8 encoded String messages.
 *
 * <p>This codec is guaranteed to be compatible with the corresponding
 * <a href="https://docs.flutter.io/flutter/services/StringCodec-class.html">StringCodec</a>
 * on the Dart side. These parts of the Flutter SDK are evolved synchronously.</p>
 */
public final class StringCodec implements MessageCodec<String> {
    private static final Charset UTF8 = Charset.forName("UTF8");
    public static final StringCodec INSTANCE = new StringCodec();

    private StringCodec() {
    }

    @Override
    public ByteBuffer encodeMessage(String message) {
        if (message == null) {
            return null;
        }
        // TODO(mravn): Avoid the extra copy below.
        final byte[] bytes = message.getBytes(UTF8);
        final ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
        buffer.put(bytes);
        return buffer;
    }

    @Override
    public String decodeMessage(ByteBuffer message) {
        if (message == null) {
            return null;
        }
        final byte[] bytes;
        final int offset;
        final int length = message.remaining();
        if (message.hasArray()) {
            bytes = message.array();
            offset = message.arrayOffset();
        } else {
            // TODO(mravn): Avoid the extra copy below.
            bytes = new byte[length];
            message.get(bytes);
            offset = 0;
        }
        return new String(bytes, offset, length, UTF8);
    }
}
