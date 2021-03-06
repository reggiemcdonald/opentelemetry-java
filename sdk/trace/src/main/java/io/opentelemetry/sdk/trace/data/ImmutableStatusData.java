/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.sdk.trace.data;

import com.google.auto.value.AutoValue;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import java.util.EnumMap;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Defines the status of a {@link Span} by providing a standard {@link StatusCode} in conjunction
 * with an optional descriptive message. Instances of {@code Status} are created by starting with
 * the template for the appropriate {@link StatusCode} and supplementing it with additional
 * information: {@code Status.NOT_FOUND.withDescription("Could not find 'important_file.txt'");}
 */
@AutoValue
@Immutable
abstract class ImmutableStatusData implements StatusData {
  /**
   * The operation has been validated by an Application developers or Operator to have completed
   * successfully.
   */
  static final StatusData OK = createInternal(StatusCode.OK, null);

  /** The default status. */
  static final StatusData UNSET = createInternal(StatusCode.UNSET, null);

  /** The operation contains an error. */
  static final StatusData ERROR = createInternal(StatusCode.ERROR, null);

  // Visible for test
  static final EnumMap<StatusCode, StatusData> codeToStatus = new EnumMap<>(StatusCode.class);

  static {
    codeToStatus.put(StatusCode.UNSET, StatusData.unset());
    codeToStatus.put(StatusCode.OK, StatusData.ok());
    codeToStatus.put(StatusCode.ERROR, StatusData.error());

    // Ensure all values are in the map, even if we don't have constants defined.
    // This can happen if the API version is newer than the SDK and new values were added there.
    StatusCode[] codes = StatusCode.values();
    for (StatusCode code : codes) {
      StatusData status = codeToStatus.get(code);
      if (status == null) {
        codeToStatus.put(code, createInternal(code, null));
      }
    }
  }

  /**
   * Creates a derived instance of {@code Status} with the given description.
   *
   * @param description the new description of the {@code Status}.
   * @return The newly created {@code Status} with the given description.
   */
  static StatusData create(StatusCode statusCode, @Nullable String description) {
    if (description == null) {
      return codeToStatus.get(statusCode);
    }
    return createInternal(statusCode, description);
  }

  private static StatusData createInternal(StatusCode statusCode, @Nullable String description) {
    return new AutoValue_ImmutableStatusData(statusCode, description);
  }
}
