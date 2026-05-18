package br.com.savedra.challengebackend.controller;

import br.com.savedra.challengebackend.model.Campaign;
import br.com.savedra.challengebackend.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping
    public ResponseEntity<List<Campaign>> getAllCampaigns(
            @RequestParam(required = false) String segment
    ) {
        System.out.println("DEBUG INBOX: Request for campaigns with segment: " + segment);
        if (segment != null && !segment.isEmpty() && !segment.equalsIgnoreCase("Todos")) {
            List<Campaign> campaigns = campaignService.getCampaignsBySegments(java.util.Arrays.asList(segment, "Todos"));
            System.out.println("DEBUG INBOX: Found " + campaigns.size() + " campaigns for " + segment + " + Todos");
            return ResponseEntity.ok(campaigns);
        }
        List<Campaign> allCampaigns = campaignService.getAllCampaigns();
        System.out.println("DEBUG INBOX: Found " + allCampaigns.size() + " campaigns (findAll)");
        return ResponseEntity.ok(allCampaigns);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getCampaignById(@PathVariable String id) {
        return campaignService.getCampaignById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        return ResponseEntity.ok(campaignService.saveCampaign(campaign));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable String id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }
}
