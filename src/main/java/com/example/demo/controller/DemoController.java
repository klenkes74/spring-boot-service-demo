package com.example.demo.controller;

import com.example.demo.model.ChangeLog;
import com.example.demo.model.StartAck;
import com.example.demo.service.ChangelogService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author rlichti
 * @version 1.0.0 2021-10-18
 * @since 1.0.0 2021-10-18
 */
@RestController
@RequestMapping(
        path = "/api/work",
        produces = { MediaType.APPLICATION_JSON }
)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@CrossOrigin(origins = "http://localhost:9090")
@Timed
@Slf4j
@ToString
public class DemoController {
    private final ChangelogService changelogService;

    @Timed
    @GetMapping(path = "changelog")
    @Secured("USER")
    @Operation(
            summary = "Loads the changelog",
            description = "This service will return a pong",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of matching changelog entries found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access"),
            @ApiResponse(responseCode = "404", description = "Not found?")
    })
    public List<ChangeLog> changeLogIndex(
            @Parameter(
                    name = "number",
                    description = "Number of changelog entries to retrieve",
                    example = "10",
                    required = true
            )
            @RequestParam int number
    ) {
        List<ChangeLog> result = changelogService.getLastChangeLogEntries(number);
        try {
            return result;
        } finally {
            log.info("Returned the last {} entries: {}", number, result);
        }
    }

    @Timed
    @PostMapping(path = "start")
    @Secured("USER")
    @Operation(
            summary = "Starts the process",
            description = "Starts the process for the changelog entries",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Process started"),
            @ApiResponse(responseCode = "403", description = "Unauthorized Access")
    })
    public StartAck start(
            @Parameter(name = "end", description = "Last timestamp to work with", example = "2021-10-17T18:23:15.001232")
            @RequestParam(required = false)
            LocalDateTime end
    ) {
        if (end == null) {
            end = LocalDateTime.now();
        }
        log.info("Start requested for entries prior: {}", end);

        List<ChangeLog> list = changelogService.getEarlierEntries(end);
        Set<Long> ids = list.stream().map(ChangeLog::getId).collect(Collectors.toSet());

        StartAck result = StartAck.builder()
                .withStart(list.get(list.size() - 1).getTimestamp())
                .withEnd(list.get(0).getTimestamp())
                .withChangeLogIds(ids)
                .build();

        try {
            return result;
        } finally {
            log.info("Started process for {} entries: {}", list.size(), result);
        }
    }
}
