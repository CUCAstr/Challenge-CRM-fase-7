package br.com.savedra.challengebackend.controller;

import br.com.savedra.challengebackend.model.Invite;
import br.com.savedra.challengebackend.repository.InviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteRepository repository;

    @GetMapping
    public ResponseEntity<List<Invite>> getInvites(@RequestParam(required = false) String segment) {
        if (segment != null) {
            return ResponseEntity.ok(repository.findBySegment(segment));
        }
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Invite> createInvite(@RequestBody Invite invite) {
        return ResponseEntity.ok(repository.save(invite));
    }
}
