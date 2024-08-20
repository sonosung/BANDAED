package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertMembers(){

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Member member = Member.builder()
                    .email("user" + i + "@naver.com")
                    .password("123456")
                    .nickname("nickname" + i)
                    .calm(true)
                    .energetic(true)
                    .happy(true)
                    .joyful(true)
                    .sad(true)
                    .stressed(true)
                    .profileImagePath(null)
                    .emailActive(true)
                    .nicknameActive(true)
                    .userRole(Member.UserRole.MEMBER)
                    .build();

            memberRepository.save(member);
        });
    }

}
