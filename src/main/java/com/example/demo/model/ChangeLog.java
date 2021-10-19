package com.example.demo.model;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author rlichti
 * @version 1.0.0 2021-10-18
 * @since 1.0.0 2021-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true, setterPrefix = "with")
@Data
@ToString
@EqualsAndHashCode
@Slf4j
@Timed
@Schema(
        name = "ChangeLog",
        description = "A single changelog entry"
)
public class ChangeLog implements Serializable {
    @Schema(description = "Database row_id of the changelog entry", required = true)
    private Long id;
    @Schema(description = "UUID of the entry", required = true)
    private UUID uuid;
    @Schema(description = "Timestamp of the change", required = true)
    private LocalDateTime timestamp;
    @Schema(description = "Type of change", required = true, enumAsRef = true)
    private ChangeLogAction method;
    @Schema(description = "User who committed this change", required = true)
    private String user;

    @Schema(description = "The attributes changed", required = true, minLength = 1, maxLength = 50)
    @Builder.Default
    private final Map<String, String> attributes = new HashMap<>();
}
