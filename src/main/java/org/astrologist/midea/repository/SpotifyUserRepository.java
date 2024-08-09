package org.astrologist.midea.repository;

import org.astrologist.midea.entity.SpotifyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotifyUserRepository extends JpaRepository<SpotifyUser, Long> {

    Optional<SpotifyUser> findBySpotifyId(String spotifyId);  // Spotify ID로 SpotifyUser 조회
}