// src/main/java/com/wordium/users/controllers/admin/AdminAccountsController.java
package com.wordium.users.controllers.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.dto.RoleChangeRequest;
import com.wordium.users.models.Users;
import com.wordium.users.services.admin.impl.AdminAccountServiceImpl;

@RestController
@RequestMapping("/users/admin/accounts")
public class AdminAccountsController {

    private final AdminAccountServiceImpl adminAccountService;

    public AdminAccountsController(AdminAccountServiceImpl adminAccountService) {
        this.adminAccountService = adminAccountService;
    }

    // GET /admin/accounts?active=true&banned=false
    @GetMapping
    // @RequestParam(required = false) Boolean active,
    // @RequestParam(required = false) Boolean banned
    public ResponseEntity<List<Users>> getAllAccounts() {
        List<Users> accounts = adminAccountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    // GET /admin/accounts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Users> getAccountById(@PathVariable Long id) {
        Users account = adminAccountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    // PATCH /admin/accounts/{id}/ban
    @PatchMapping("/{id}/ban")
    public ResponseEntity<Users> banAccount(@PathVariable Long id) {
        Users account = adminAccountService.banAccount(id);
        return ResponseEntity.ok(account);
    }

    // PATCH /admin/accounts/{id}/unban
    @PatchMapping("/{id}/unban")
    public ResponseEntity<Users> unbanAccount(@PathVariable Long id) {
        Users account = adminAccountService.unbanAccount(id);
        return ResponseEntity.ok(account);
    }

    // PATCH /admin/accounts/{id}/role
    @PatchMapping("/{id}/role")
    public ResponseEntity<Users> changeRole(
            @PathVariable Long id,
            @RequestBody RoleChangeRequest request) {
        Users account = adminAccountService.changeRole(id, request.role());
        return ResponseEntity.ok(account);
    }

    // DELETE /admin/accounts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        adminAccountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

}