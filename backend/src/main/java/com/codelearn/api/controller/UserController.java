package com.codelearn.api.controller;

import com.codelearn.api.dto.AnonymousUserResponse;
import com.codelearn.api.model.User;
import com.codelearn.api.model.UserStats;
import com.codelearn.api.repository.UserRepository;
import com.codelearn.api.repository.UserStatsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;

    public UserController(UserRepository userRepository, UserStatsRepository userStatsRepository) {
        this.userRepository = userRepository;
        this.userStatsRepository = userStatsRepository;
    }

    @PostMapping("/anonymous")
    public ResponseEntity<AnonymousUserResponse> createAnonymousUser() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String username = "guest_" + suffix;

        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@codelearn.local");
        user.setPasswordHash(UUID.randomUUID().toString());
        user = userRepository.save(user);

        UserStats stats = new UserStats();
        stats.setUser(user);
        stats.setTotalXp(0);
        stats.setLevel(1);
        userStatsRepository.save(stats);

        return ResponseEntity.ok(new AnonymousUserResponse(user.getId(), username, 0, 1));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<AnonymousUserResponse> getUserStats(@PathVariable Long id) {
        return userStatsRepository.findById(id)
                .map(s -> ResponseEntity.ok(new AnonymousUserResponse(id, s.getUser().getUsername(), s.getTotalXp(), s.getLevel())))
                .orElse(ResponseEntity.notFound().build());
    }
}
