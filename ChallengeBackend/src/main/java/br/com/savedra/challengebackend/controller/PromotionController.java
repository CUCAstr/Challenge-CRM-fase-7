package br.com.savedra.challengebackend.controller;

import br.com.savedra.challengebackend.model.Promotion;
import br.com.savedra.challengebackend.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionRepository repository;

    @GetMapping
    public ResponseEntity<List<Promotion>> getPromotions(@RequestParam(required = false) String segment) {
        if (segment != null) {
            return ResponseEntity.ok(repository.findBySegment(segment));
        }
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        return ResponseEntity.ok(repository.save(promotion));
    }
}
