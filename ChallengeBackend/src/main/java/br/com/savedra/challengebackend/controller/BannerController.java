package br.com.savedra.challengebackend.controller;

import br.com.savedra.challengebackend.model.Banner;
import br.com.savedra.challengebackend.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerRepository repository;

    @GetMapping
    public ResponseEntity<List<Banner>> getBanners(@RequestParam(required = false) String segment) {
        if (segment != null) {
            return ResponseEntity.ok(repository.findBySegment(segment));
        }
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Banner> createBanner(@RequestBody Banner banner) {
        return ResponseEntity.ok(repository.save(banner));
    }
}
