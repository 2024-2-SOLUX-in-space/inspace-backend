package jpabasic.inspacebe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(30))  // Read timeout 설정 (기본 30초)
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);  // Connect timeout 설정 (기본 10초)

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://inspace-proxy:8000");
    }
}
