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

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public List<Campaign> getCampaignsBySegment(String segment) {
        return campaignRepository.findBySegment(segment);
    }

    public Optional<Campaign> getCampaignById(String id) {
        return campaignRepository.findById(id);
    }

    public Campaign saveCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    public void deleteCampaign(String id) {
        campaignRepository.deleteById(id);
    }
}
