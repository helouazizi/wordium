package com.wordium.users.controllers.followers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.services.followers.FollowersService;

@RestController
@RequestMapping("/users/internal")
public class InternalFollowersController {

    private final FollowersService followersService;

    public InternalFollowersController(FollowersService followersService) {
        this.followersService = followersService;
    }

   
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<Long>> getFollowingIds(@PathVariable Long userId) {
        List<Long> followingIds = followersService.getFollowingIds(userId);
        return ResponseEntity.ok(followingIds);
    }
}
