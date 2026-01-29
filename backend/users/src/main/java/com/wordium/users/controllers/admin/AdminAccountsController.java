package com.wordium.users.controllers.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.dto.PaginatedResponse;
import com.wordium.users.dto.PaginationRequest;
import com.wordium.users.dto.users.CountResponse;
import com.wordium.users.dto.users.RoleChangeRequest;
import com.wordium.users.dto.users.UserProfile;
import com.wordium.users.models.Users;
import com.wordium.users.services.admin.impl.AdminAccountServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Admin - Accounts Management", description = "Admin endpoints for managing user accounts")
@RestController
@RequestMapping("/users/admin/accounts")
public class AdminAccountsController {

        private final AdminAccountServiceImpl adminAccountService;

        public AdminAccountsController(AdminAccountServiceImpl adminAccountService) {
                this.adminAccountService = adminAccountService;
        }

        @Operation(summary = "List all user accounts", description = "Retrieve a list of all user accounts. Supports optional filtering by active/banned status (currently disabled in code).")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Accounts fetched successfully", content = @Content(schema = @Schema(implementation = Users.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - User is not an admin", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @GetMapping
        public ResponseEntity<PaginatedResponse<UserProfile>> getAllAccounts(@RequestHeader("User-Id") Long id,
                        @Valid PaginationRequest paginationRequest) {
                Pageable pageable = paginationRequest.toPageable();
                Page<UserProfile> accounts = adminAccountService.getAllAccounts(id, pageable);
                return ResponseEntity.ok(PaginatedResponse.fromPage(accounts));
        }

        @Operation(summary = "Get user account by ID", description = "Retrieve detailed information about a specific user account.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Account fetched successfully", content = @Content(schema = @Schema(implementation = Users.class))),
                        @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Not an admin", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @GetMapping("/{id}")
        public ResponseEntity<Users> getAccountById(@PathVariable Long id) {
                Users account = adminAccountService.getAccountById(id);
                return ResponseEntity.ok(account);
        }

        @Operation(summary = "Ban a user", description = "Ban a user account by ID. Sets the banned flag to true.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User banned successfully", content = @Content(schema = @Schema(implementation = Users.class))),
                        @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Not an admin", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PatchMapping("/{id}/ban")
        public ResponseEntity<Users> banAccount(@PathVariable Long id) {
                Users account = adminAccountService.banAccount(id);
                return ResponseEntity.ok(account);
        }

        @Operation(summary = "Unban a user", description = "Remove ban from a user account by ID. Sets the banned flag to false.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User unbanned successfully", content = @Content(schema = @Schema(implementation = Users.class))),
                        @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Not an admin", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PatchMapping("/{id}/unban")
        public ResponseEntity<Users> unbanAccount(@PathVariable Long id) {
                Users account = adminAccountService.unbanAccount(id);
                return ResponseEntity.ok(account);
        }

        @Operation(summary = "Change user role", description = "Update the role of a user (e.g., USER → MODERATOR → ADMIN). Requires a JSON body with the new role.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Role changed successfully", content = @Content(schema = @Schema(implementation = Users.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid role provided", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Not an admin or insufficient privileges", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PatchMapping("/{id}/role")
        public ResponseEntity<Users> changeRole(
                        @PathVariable Long id,
                        @RequestBody RoleChangeRequest request) {
                Users account = adminAccountService.changeRole(id, request.role());
                return ResponseEntity.ok(account);
        }

        @Operation(summary = "Delete a user account", description = "Permanently delete a user account by ID. Use with caution.")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Not an admin", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
                adminAccountService.deleteAccount(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/count")
        public ResponseEntity<CountResponse> getTotalUsers() {
                return ResponseEntity.ok(
                                new CountResponse(adminAccountService.getTotalUsers()));
        }

}