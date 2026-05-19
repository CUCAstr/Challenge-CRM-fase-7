package br.com.savedra.challengebackend.service;

import br.com.savedra.challengebackend.model.Campaign;
import br.com.savedra.challengebackend.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final AuditService auditService;

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public List<Campaign> getCampaignsBySegment(String segment) {
        return campaignRepository.findBySegment(segment);
    }

    public List<Campaign> getCampaignsBySegments(List<String> segments) {
        return campaignRepository.findBySegmentIn(segments);
    }

    public Optional<Campaign> getCampaignById(String id) {
        return campaignRepository.findById(id);
    }

    public Campaign saveCampaign(Campaign campaign) {
        Campaign saved = campaignRepository.save(campaign);
        auditService.log("CREATE_CAMPAIGN", "Campanha criada: " + saved.getTitle());
        return saved;
    }

    public void deleteCampaign(String id) {
        campaignRepository.findById(id).ifPresent(c -> {
            auditService.log("DELETE_CAMPAIGN", "Campanha excluída: " + c.getTitle());
            campaignRepository.deleteById(id);
        });
    }
}
