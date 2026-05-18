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
        System.out.println("DEBUG INBOX: Request for banners with segment: " + segment);
        if (segment != null && !segment.isEmpty() && !segment.equalsIgnoreCase("Todos")) {
            List<Banner> banners = repository.findBySegmentIn(java.util.Arrays.asList(segment, "Todos"));
            System.out.println("DEBUG INBOX: Found " + banners.size() + " banners for " + segment + " + Todos");
            return ResponseEntity.ok(banners);
        }
        List<Banner> allBanners = repository.findAll();
        System.out.println("DEBUG INBOX: Found " + allBanners.size() + " banners (findAll)");
        return ResponseEntity.ok(allBanners);
    }

    @PostMapping
    public ResponseEntity<Banner> createBanner(@RequestBody Banner banner) {
        return ResponseEntity.ok(repository.save(banner));
    }
}
