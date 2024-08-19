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
    private final UserRepository userRepository;  // UserRepository 추가

    /**
     * 모든 게시글을 가져옵니다.
     *
     * @return 게시글 리스트
     */
    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    /**
     * 새로운 게시글을 생성합니다.
     *
     * @param communityDTO - 게시글 생성에 필요한 데이터
     */
    public void createCommunityPost(CommunityDTO communityDTO) {
        // userId로 User 객체를 조회합니다.
        User user = userRepository.findById(communityDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + communityDTO.getUserId()));

        Community community = Community.builder()
                .composer(communityDTO.getComposer())
                .content(communityDTO.getContent())
                .subcategory(communityDTO.getSubcategory())
                .user(user)  // 조회한 User 객체를 사용합니다.
                .build();

        communityRepository.save(community);
    }

    /**
     * 채팅 메시지를 저장합니다.
     *
     * @param chatMessage - 저장할 채팅 메시지
     */
    public void saveChatMessage(Community chatMessage) {
        communityRepository.save(chatMessage);
    }

    /**
     * 특정 주제에 대한 채팅 기록을 가져옵니다.
     *
     * @param subcategory - 조회할 주제
     * @return 해당 주제의 채팅 메시지 리스트
     */
    public List<Community> getChatHistory(Community.Subcategory subcategory) {
        return communityRepository.findBySubcategoryAndIsChatMessageTrueOrderByTimestampAsc(subcategory);
    }
}
