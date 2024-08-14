package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity  // 이 클래스가 JPA 엔티티임을 나타냅니다. 이 클래스는 데이터베이스 테이블과 매핑됩니다.
@ToString  // Lombok 어노테이션으로, 이 클래스의 toString() 메서드를 자동 생성합니다.
@Getter  // Lombok 어노테이션으로, 모든 필드에 대한 getter 메서드를 자동 생성합니다.
@Builder  // Lombok 어노테이션으로, 이 클래스의 빌더 패턴을 자동 생성합니다.
@AllArgsConstructor  // Lombok 어노테이션으로, 모든 필드를 파라미터로 받는 생성자를 자동 생성합니다.
@NoArgsConstructor  // Lombok 어노테이션으로, 파라미터가 없는 기본 생성자를 자동 생성합니다.
@Table(name = "community")  // 이 엔티티가 "community"라는 이름의 데이터베이스 테이블과 매핑됨을 나타냅니다.
public class Community extends BaseEntity {  // Community 클래스는 BaseEntity를 상속받아 생성 및 수정 시간 필드를 상속받습니다.

    @Id  // 이 필드가 엔티티의 기본 키임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본 키가 데이터베이스에서 자동으로 생성되도록 설정합니다.
    private Long mno;  // Community 엔티티의 고유 ID, 데이터베이스에서 자동 생성됩니다.

    @Column(length = 100, nullable = false)
    private String composer;  // 작성자 이름을 저장하는 필드입니다.

    @Column(length = 1500, nullable = false)
    private String content;  // 커뮤니티 게시물의 내용을 저장하는 필드입니다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 이 커뮤니티 게시물의 작성자(User)를 나타내는 필드입니다.

    @Column(nullable = false)
    @Builder.Default
    private int likeCount = 0;  // 게시물에 대한 좋아요 수를 저장하는 필드입니다. 기본값은 0으로 초기화됩니다.

    @Column(nullable = false)
    @Builder.Default
    private boolean isArchived = false;  // 게시물이 아카이브 상태인지 여부를 나타내는 필드입니다. 기본값은 false로 설정됩니다.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Subcategory subcategory;  // 게시물이 속한 소분류를 저장하는 필드입니다.

    public enum Subcategory {
        MinimalTechno, MinimalHouse, MinimalClassical, SteveReich, PhilipGlass, TerryRiley, Synthesizer, Piano, Percussion
    }

    // 게시물 내용을 변경하는 메서드
    public void changeContent(String content) {
        this.content = content;
    }

    // User 엔티티의 ID를 반환하는 메서드
    public Long getUserId() {
        return user.getId();
    }

    // User 엔티티의 닉네임을 반환하는 메서드
    public String getNickname() {
        return user.getNickname();
    }

    // User 엔티티의 역할(UserRole)을 반환하는 메서드
    public User.UserRole getUserRole() {
        return user.getUserRole();
    }

    // User 엔티티의 프로필 이미지 경로를 반환하는 메서드
    public String getProfileImagePath() {
        return user.getProfileImagePath();
    }

    // 좋아요 수를 증가시키는 메서드
    public void incrementLikeCount() {
        this.likeCount++;
    }

    // 좋아요 수를 감소시키는 메서드 (옵션)
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    // 아카이브 상태를 변경하는 메서드
    public void archive() {
        this.isArchived = true;
    }

    // 아카이브 해제 상태로 변경하는 메서드
    public void unarchive() {
        this.isArchived = false;
    }
}
