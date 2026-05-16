package br.com.savedra.challengebackend.controller;

import br.com.savedra.challengebackend.model.Segment;
import br.com.savedra.challengebackend.service.SegmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/segments")
@RequiredArgsConstructor
public class SegmentController {

    private final SegmentService segmentService;

    @GetMapping
    public ResponseEntity<List<Segment>> getAllSegments() {
        return ResponseEntity.ok(segmentService.getAllSegments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Segment> getSegmentById(@PathVariable String id) {
        return segmentService.getSegmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Segment> createSegment(@RequestBody Segment segment) {
        return ResponseEntity.ok(segmentService.saveSegment(segment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Segment> updateSegment(@PathVariable String id, @RequestBody Segment segment) {
        return ResponseEntity.ok(segmentService.updateSegment(id, segment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSegment(@PathVariable String id) {
        segmentService.deleteSegment(id);
        return ResponseEntity.noContent().build();
    }
}
