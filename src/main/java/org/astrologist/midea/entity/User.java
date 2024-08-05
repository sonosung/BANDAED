package org.astrologist.midea.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // 이 클래스가 JPA 엔티티임을 나타냅니다.
@Data // Lombok 어노테이션으로, getter, setter, toString, equals, hashCode 메소드를 자동으로 생성합니다.
@Builder // Lombok 어노테이션으로, 빌더 패턴을 적용할 수 있게 합니다.
@NoArgsConstructor // Lombok 어노테이션으로, 파라미터가 없는 기본 생성자를 생성합니다.
@AllArgsConstructor // Lombok 어노테이션으로, 모든 필드를 파라미터로 가지는 생성자를 생성합니다.
@Table(name = "users") // 이 클래스가 매핑될 데이터베이스 테이블의 이름을 지정합니다.
public class User {
    @Id // 이 필드가 엔티티의 기본 키임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임합니다 (AUTO_INCREMENT).
    private Long id;

    @NotBlank(message = "Email is mandatory") // 필드가 비어있거나 공백 문자열일 수 없음을 나타냅니다.
    @Email(message = "Email should be valid") // 필드가 유효한 이메일 주소 형식이어야 함을 나타냅니다.
    @Column(nullable = false, unique = true) // 이 필드가 데이터베이스에서 null이 될 수 없고, 유일해야 함을 나타냅니다.
    private String email;

    @NotBlank(message = "Password is mandatory") // 필드가 비어있거나 공백 문자열일 수 없음을 나타냅니다.
    @Size(min = 6, message = "Password must be at least 6 characters long") // 필드의 최소 길이를 6자로 제한합니다.
    @Column(nullable = false) // 이 필드가 데이터베이스에서 null이 될 수 없음을 나타냅니다.
    private String password;

    @NotBlank(message = "Nickname is mandatory") // 필드가 비어있거나 공백 문자열일 수 없음을 나타냅니다.
    @Column(nullable = false, unique = true) // 이 필드가 데이터베이스에서 null이 될 수 없고, 유일해야 함을 나타냅니다.
    private String nickname;

    @Enumerated(EnumType.STRING) // 이 필드가 열거형이며, 데이터베이스에 문자열로 저장됨을 나타냅니다.
    @Column(nullable = false) // 이 필드가 데이터베이스에서 null이 될 수 없음을 나타냅니다.
    @Builder.Default // 빌더 패턴을 사용할 때 이 필드의 기본값을 설정합니다.
    private UserRole userRole = UserRole.MEMBER; // 기본값으로 MEMBER를 설정합니다.

    @Builder.Default // 빌더 패턴을 사용할 때 이 필드의 기본값을 설정합니다.
    @Column(nullable = false) // 이 필드가 데이터베이스에서 null이 될 수 없음을 나타냅니다.
    private boolean emailActive = true; // 기본값으로 true를 설정합니다.

    @Builder.Default // 빌더 패턴을 사용할 때 이 필드의 기본값을 설정합니다.
    @Column(nullable = false) // 이 필드가 데이터베이스에서 null이 될 수 없음을 나타냅니다.
    private boolean nicknameActive = true; // 기본값으로 true를 설정합니다.

    @Builder.Default // 빌더 패턴을 사용할 때 이 필드의 기본값을 설정합니다.
    private boolean happy = false; // 기본값으로 false를 설정합니다.

    @Builder.Default // 빌더 패턴을 사용할 때 이 필드의 기본값을 설정합니다.
    private boolean sad = false; // 기본값으로 false를 설정합니다.

    @Builder.Default // 빌더 패턴을 사용할 때 이 필드의 기본값을 설정합니다.
    private boolean calm = false; // 기본값으로 false를 설정합니다.

    @Builder.Default // 빌더 패턴을 사용할 때 이 필드의 기본값을 설정합니다.
    private boolean stressed = false; // 기본값으로 false를 설정합니다.

    @Builder.Default // 빌더 패턴을 사용할 때 이 필드의 기본값을 설정합니다.
    private boolean joyful = false; // 기본값으로 false를 설정합니다.

    @Builder.Default // 빌더 패턴을 사용할 때 이 필드의 기본값을 설정합니다.
    private boolean energetic = false; // 기본값으로 false를 설정합니다.

    public enum UserRole {
        GUEST, MEMBER, ADMIN // 사용자 역할을 정의하는 열거형 상수입니다.
    }
}
