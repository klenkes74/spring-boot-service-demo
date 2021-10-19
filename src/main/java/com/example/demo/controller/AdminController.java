package com.example.demo.controller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;

/**
 * @author rlichti
 * @version 1.0.0 2021-10-18
 * @since 1.0.0 2021-10-18
 */
@RestController
@CrossOrigin(origins = "http://localhost:9090")
@Timed
@Slf4j
@ToString
public class AdminController {
    @Value("${spring.application.name}")
    private String appName;

    @Timed
    @GetMapping(
            path = "/api/admin/ping",
            produces = { MediaType.TEXT_PLAIN }
    )
    @Secured("USER")
    @Operation(
            summary = "A simple Ping-Pong-Service",
            description = "This service will return a pong",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pong succeeded"),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access"),
            @ApiResponse(responseCode = "404", description = "Not found?")
    })
    public String ping() {
        try {
            return "pong";
        } finally {
            log.info("Answered ping request with pong");
        }
    }

    @Timed
    @GetMapping(
            path = "/api/admin/application",
            produces = { MediaType.TEXT_PLAIN }
    )
    @Secured("ADMIN")
    @Operation(
            summary = "Application name",
            description = "This service will return the application name",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pong succeeded"),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access"),
            @ApiResponse(responseCode = "404", description = "Not found?")
    })
    public String applicationName() {
        try {
            return appName;
        } finally {
            log.info("Answered request for application name with: {}", appName);
        }
    }
}
