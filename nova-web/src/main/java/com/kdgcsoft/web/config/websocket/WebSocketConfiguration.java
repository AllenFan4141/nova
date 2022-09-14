package com.kdgcsoft.web.config.websocket;

import com.kdgcsoft.web.common.consts.WebConst;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * @description:
 * @author: fyin
 * @date: 2018/9/29/029 10:34
 * @version: 1.0
 **/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WebConst.WEB_SOCKET_ENDPOINT)
                .addInterceptors()
                .withSockJS();
    }
}
