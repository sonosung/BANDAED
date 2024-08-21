package org.astrologist.midea.service;

import lombok.RequiredArgsConstructor;
import org.astrologist.midea.dto.CommunityDTO;
import org.astrologist.midea.entity.Community;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.CommunityRepository;
import org.astrologist.midea.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    public void createCommunityPost(CommunityDTO communityDTO) {
        try {

            User user = userRepository.findById(communityDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + communityDTO.getUserId()));


            String subcategory = communityDTO.getSubcategory().replace(" ", "");
            Community.Subcategory subcategoryEnum = Community.Subcategory.valueOf(subcategory);


            Community community = Community.builder()
                    .composer(communityDTO.getComposer())
                    .content(communityDTO.getContent())
                    .subcategory(subcategoryEnum)  // 변환된 enum 값 사용
                    .user(user)  // 조회한 User 객체를 사용합니다.
                    .timestamp(LocalDateTime.now())  // 현재 시간을 timestamp로 설정
                    .build();


            communityRepository.save(community);
        } catch (Exception e) {

            System.err.println("Failed to create community post: " + communityDTO);
            throw e;  // 예외를 다시 던짐
        }
    }

    public List<Community> getCommunitiesBySubcategory(Community.Subcategory subcategory) {
        return communityRepository.findBySubcategoryOrderByTimestampAsc(subcategory);
    }
}