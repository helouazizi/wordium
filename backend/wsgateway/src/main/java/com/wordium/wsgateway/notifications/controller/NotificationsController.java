package com.wordium.wsgateway.notifications.controller;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.wsgateway.common.dto.NotificationsResponse;
import com.wordium.wsgateway.notifications.service.NotificationsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/wsgateway")
public class NotificationsController {

    private final NotificationsService service;

    public NotificationsController(NotificationsService service) {
        this.service = service;
    }

    @GetMapping("/notifications")
    @Operation(summary = "Get all notifications for the current user",
               description = "Returns all notifications including actor, type, read status, and creation time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of notifications returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationsResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<NotificationsResponse> getMyNotifications(
            @RequestHeader("User-Id") Long userId) {
        NotificationsResponse notifs = service.getUserNotifications(userId);
        return ResponseEntity.ok(notifs);
    }


    @GetMapping("/notifications/unread-count")
    @Operation(summary = "Get unread notifications count",
               description = "Returns the number of unread notifications for the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unread count returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationsResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<NotificationsResponse> unreadCount(
            @RequestHeader("User-Id") Long userId) {
        long unread = service.unreadCount(userId);
        return ResponseEntity.ok(new NotificationsResponse(null, unread, 0));
    }


    @PostMapping("/notifications/{id}/read")
    @Operation(summary = "Mark a notification as read",
               description = "Marks a specific notification as read by the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification marked as read, no content"),
            @ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId) {
        service.markAsRead(id, userId);
        return ResponseEntity.noContent().build();
    }
}
