/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.trace;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link SpanId}. */
class SpanIdTest {
  private static final String first = "0000000000000061";
  private static final String second = "ff00000000000041";

  @Test
  void invalid() {
    assertThat(SpanId.getInvalid()).isEqualTo("0000000000000000");
  }

  @Test
  void isValid() {
    assertThat(SpanId.isValid(null)).isFalse();
    assertThat(SpanId.isValid("001")).isFalse();
    assertThat(SpanId.isValid("000000000000z000")).isFalse();
    assertThat(SpanId.isValid(SpanId.getInvalid())).isFalse();

    assertThat(SpanId.isValid(first)).isTrue();
    assertThat(SpanId.isValid(second)).isTrue();
  }

  @Test
  void fromLong() {
    assertThat(SpanId.fromLong(0)).isEqualTo(SpanId.getInvalid());
    assertThat(SpanId.fromLong(0x61)).isEqualTo(first);
    assertThat(SpanId.fromLong(0xff00000000000041L)).isEqualTo(second);
  }

  @Test
  void fromBytes() {
    assertThat(SpanId.fromBytes(null)).isEqualTo(SpanId.getInvalid());

    String spanId = "090a0b0c0d0e0f00";
    assertThat(SpanId.fromBytes(BigendianEncoding.bytesFromBase16(spanId, SpanId.getLength())))
        .isEqualTo(spanId);
  }
}
