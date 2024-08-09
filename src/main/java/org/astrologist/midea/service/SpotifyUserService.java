package org.astrologist.midea.service;

import org.astrologist.midea.dto.SpotifyUserDTO;
import org.astrologist.midea.entity.SpotifyUser;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.SpotifyUserRepository;
import org.astrologist.midea.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

@Service
public class SpotifyUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpotifyUserRepository spotifyUserRepository;

    private final String clientId = "3024f8d92232437c8d80cd5d74617ed9";
    private final String clientSecret = "eb77bb24123c4ea6ba80fbc85da43a65";
    private final String redirectUri = "http://localhost:8080/midea/index";

    // Spotify 로그인 URL을 생성하여 리턴
    public String getSpotifyLoginUrl() {
        try {
            return "https://accounts.spotify.com/authorize?" +
                    "client_id=" + clientId +
                    "&response_type=code" +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
                    "&scope=user-read-email,user-read-private";
        } catch (IOException e) {
            throw new RuntimeException("Error encoding redirect URI", e);
        }
    }

    // Spotify에서 받은 인증 코드를 사용해 Access Token을 얻음
    public User handleSpotifyCallback(String code) throws IOException {
        // Access Token 요청
        String accessToken = getSpotifyAccessToken(code);

        // Access Token을 사용해 사용자 정보 요청
        SpotifyUserDTO spotifyUserDTO = getSpotifyUserDTO(accessToken);

        // Spotify ID를 사용해 SpotifyUser 조회, 없으면 새로 생성
        Optional<SpotifyUser> optionalSpotifyUser = spotifyUserRepository.findBySpotifyId(spotifyUserDTO.getId());
        SpotifyUser spotifyUser;
        if (optionalSpotifyUser.isPresent()) {
            spotifyUser = optionalSpotifyUser.get();
        } else {
            spotifyUser = new SpotifyUser();
            spotifyUser.setSpotifyId(spotifyUserDTO.getId());
            spotifyUser.setSpotifyAccessToken(accessToken);
            spotifyUser.setSpotifyLoggedIn(true);

            // 새로운 사용자 생성
            User user = new User();
            user.setEmail(spotifyUserDTO.getEmail());
            user.setUserRole(User.UserRole.MEMBER); // 기본 역할을 MEMBER로 설정
            userRepository.save(user);

            spotifyUser.setUser(user);
            spotifyUserRepository.save(spotifyUser);
        }

        return spotifyUser.getUser();
    }

    // Access Token을 요청하는 메서드
    private String getSpotifyAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://accounts.spotify.com/api/token";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 바디 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        // HttpEntity 객체 생성 (헤더와 바디 포함)
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // 응답에서 액세스 토큰 추출
        String responseBody = response.getBody();
        JSONObject json = new JSONObject(responseBody);
        return json.getString("access_token");
    }

    // Access Token을 사용해 사용자 정보를 가져오는 메서드
    private SpotifyUserDTO getSpotifyUserDTO(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.spotify.com/v1/me";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        // HttpEntity 객체 생성 (헤더 포함, 바디는 필요 없음)
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // GET 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        // 응답에서 사용자 정보 추출
        String responseBody = response.getBody();
        JSONObject json = new JSONObject(responseBody);

        // SpotifyUserDTO 객체 생성 및 반환
        return new SpotifyUserDTO(json.getString("id"), json.getString("email"));
    }
}
