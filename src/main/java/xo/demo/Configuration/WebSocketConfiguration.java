package xo.demo.Configuration;
import org.springframework.context.annotation.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer{
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // Register Stomp endpoints mapping to "/ws" and enable SockJS fallback options
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        // Register Stomp endpoints mapping to "/ws" and enable SockJS fallback options
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/queue", "topic", "user");
        registry.setUserDestinationPrefix("/user");
    }
}