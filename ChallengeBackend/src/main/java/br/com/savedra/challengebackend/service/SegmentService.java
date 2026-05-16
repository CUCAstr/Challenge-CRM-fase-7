package br.com.savedra.challengebackend.service;

import br.com.savedra.challengebackend.model.Segment;
import br.com.savedra.challengebackend.repository.SegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SegmentService {

    private final SegmentRepository segmentRepository;

    public List<Segment> getAllSegments() {
        return segmentRepository.findAll();
    }

    public Optional<Segment> getSegmentById(String id) {
        return segmentRepository.findById(id);
    }

    public Segment saveSegment(Segment segment) {
        return segmentRepository.save(segment);
    }

    public void deleteSegment(String id) {
        segmentRepository.deleteById(id);
    }

    public Segment updateSegment(String id, Segment segmentDetails) {
        Segment segment = segmentRepository.findById(id).orElseThrow();
        segment.setName(segmentDetails.getName());
        segment.setDescription(segmentDetails.getDescription());
        return segmentRepository.save(segment);
    }
}
