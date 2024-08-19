package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity  // 이 클래스가 JPA 엔티티임을 나타냅니다. 이 클래스는 데이터베이스 테이블과 매핑됩니다.
@ToString  // Lombok 어노테이션으로, 이 클래스의 toString() 메서드를 자동 생성합니다.
@Getter  // Lombok 어노테이션으로, 모든 필드에 대한 getter 메서드를 자동 생성합니다.
@Builder  // Lombok 어노테이션으로, 이 클래스의 빌더 패턴을 자동 생성합니다.
@AllArgsConstructor  // Lombok 어노테이션으로, 모든 필드를 파라미터로 받는 생성자를 자동 생성합니다.
@NoArgsConstructor  // Lombok 어노테이션으로, 파라미터가 없는 기본 생성자를 자동 생성합니다.
@Setter
@Table(name = "community")  // 이 엔티티가 "community"라는 이름의 데이터베이스 테이블과 매핑됨을 나타냅니다.
public class Community extends BaseEntity {  // Community 클래스는 BaseEntity를 상속받아 생성 및 수정 시간 필드를 상속받습니다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 이 엔티티의 고유 ID (게시글 또는 채팅 메시지)

    @Column(length = 100, nullable = false)
    private String composer;  // 작성자 이름 또는 닉네임

    @Column(length = 1500, nullable = false)
    private String content;  // 게시글 내용 또는 채팅 메시지 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 메시지나 게시글을 작성한 사용자

    @Column(nullable = false)
    @Builder.Default
    private int likeCount = 0;  // 게시글에 대한 좋아요 수

    @Column(nullable = false)
    @Builder.Default
    private boolean isArchived = false;  // 게시글이 아카이브(보관) 상태인지 여부

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Subcategory subcategory;  // 게시글 또는 채팅방의 주제 (카테고리)

    @Column(nullable = false)
    @Builder.Default
    private boolean isChatMessage = false;  // 이 엔티티가 채팅 메시지인지 여부

    @Column(nullable = true)
    private String room;  // 채팅방 식별자 (채팅 메시지에만 사용)

    @Column(nullable = true)
    private LocalDateTime timestamp;  // 메시지가 전송된 시간 (채팅 메시지에만 사용)

    // Subcategory는 게시글과 채팅방의 주제를 나타냅니다.
    public enum Subcategory {
        MinimalTechno, MinimalHouse, MinimalClassical, SteveReich, PhilipGlass, TerryRiley, Synthesizer, Piano, Percussion
    }

    // 게시글 내용을 변경하는 메서드
    public void changeContent(String content) {
        this.content = content;
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
    // isChatMessage를 설정하는 메서드
    public void setIsChatMessage(boolean isChatMessage) {
        this.isChatMessage = isChatMessage;
    }
    // timestamp를 설정하는 메서드
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}