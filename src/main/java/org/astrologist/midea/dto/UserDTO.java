package org.astrologist.midea.dto;

import org.astrologist.midea.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserDTO {

    @NotEmpty(message = "Nickname is mandatory")
    private String nickname;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private boolean happy;
    private boolean sad;
    private boolean calm;
    private boolean stressed;
    private boolean joyful;
    private boolean energetic;

    // Getters and Setters

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHappy() {
        return happy;
    }

    public void setHappy(boolean happy) {
        this.happy = happy;
    }

    public boolean isSad() {
        return sad;
    }

    public void setSad(boolean sad) {
        this.sad = sad;
    }

    public boolean isCalm() {
        return calm;
    }

    public void setCalm(boolean calm) {
        this.calm = calm;
    }

    public boolean isStressed() {
        return stressed;
    }

    public void setStressed(boolean stressed) {
        this.stressed = stressed;
    }

    public boolean isJoyful() {
        return joyful;
    }

    public void setJoyful(boolean joyful) {
        this.joyful = joyful;
    }

    public boolean isEnergetic() {
        return energetic;
    }

    public void setEnergetic(boolean energetic) {
        this.energetic = energetic;
    }

    // DTO to Entity conversion
    public User toEntity() {
        return User.builder()
                .nickname(this.nickname)
                .email(this.email)
                .password(this.password)
                .happy(this.happy)
                .sad(this.sad)
                .calm(this.calm)
                .stressed(this.stressed)
                .joyful(this.joyful)
                .energetic(this.energetic)
                .build();
    }
}