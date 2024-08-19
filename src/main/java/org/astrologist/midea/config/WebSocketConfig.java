package org.astrologist.midea.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration  // 이 클래스가 Spring의 설정 클래스임을 나타냅니다.
@EnableWebSocketMessageBroker  // 이 어노테이션은 WebSocket 메시지 브로커를 사용하도록 설정합니다.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 메시지 브로커를 구성하는 메서드입니다.
     * 메시지 브로커는 메시지를 목적지로 라우팅하는 역할을 합니다.
     *
     * @param config - MessageBrokerRegistry 객체로 메시지 브로커 설정을 위한 API를 제공합니다.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 간단한 메모리 기반 메시지 브로커를 활성화하여 클라이언트에게 메시지를 전달합니다.
        // "/topic" 접두사를 가진 메시지가 브로커에 의해 처리됩니다.
        config.enableSimpleBroker("/topic");

        // 클라이언트에서 서버로 전송되는 메시지의 목적지 앞에 "/app" 접두사를 추가합니다.
        // 예를 들어, 클라이언트가 "/app/chat.sendMessage"로 메시지를 보내면,
        // 이 메시지는 WebSocket 컨트롤러에서 처리됩니다.
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * 클라이언트가 WebSocket 서버에 연결할 수 있도록 엔드포인트를 등록하는 메서드입니다.
     *
     * @param registry - StompEndpointRegistry 객체로 엔드포인트 등록을 위한 API를 제공합니다.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // "/ws" 엔드포인트를 통해 WebSocket 연결을 설정합니다.
        // 클라이언트는 이 경로로 WebSocket 서버에 연결할 수 있습니다.
        // setAllowedOrigins("*")는 모든 도메인에서의 요청을 허용함을 의미합니다.
        // withSockJS()는 SockJS를 활성화하여 WebSocket이 지원되지 않는 환경에서도
        // WebSocket-like 동작을 가능하게 합니다.
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }
}
