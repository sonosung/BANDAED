package org.astrologist.midea.service;

import lombok.RequiredArgsConstructor;
import org.astrologist.midea.dto.CommunityDTO;
import org.astrologist.midea.entity.Community;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.CommunityRepository;
import org.astrologist.midea.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public Community createCommunityPost(CommunityDTO communityDTO) {
        User user = userRepository.findById(communityDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Community community = Community.builder()
                .user(user)
                .composer(communityDTO.getComposer())
                .content(communityDTO.getContent())
                .subcategory(communityDTO.getSubcategory())
                .build();

        return communityRepository.save(community);
    }

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }
}
