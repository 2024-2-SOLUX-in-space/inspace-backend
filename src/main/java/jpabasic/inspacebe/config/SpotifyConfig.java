package jpabasic.inspacebe.config;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;

@Configuration
public class SpotifyConfig {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;


    private static String accessToken;
    private static Instant tokenExpiryTime;


    @Bean
    public SpotifyApi spotifyApi() {
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
    }

    public String getAccessToken(SpotifyApi spotifyApi) {
        // 토큰이 없거나 만료된 경우 갱신
        if (accessToken == null || tokenExpiryTime == null || Instant.now().isAfter(tokenExpiryTime)) {
            refreshAccessToken(spotifyApi);
        }
        return accessToken;
    }

    @Bean
    public String refreshAccessToken(SpotifyApi spotifyApi) {
        if (accessToken == null || tokenExpiryTime == null || Instant.now().isAfter(tokenExpiryTime)) {
            System.out.println("[Spotify] Access Token expired. Requesting a new one...");

            try {
                ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
                ClientCredentials clientCredentials = clientCredentialsRequest.execute();

                accessToken = clientCredentials.getAccessToken();
                tokenExpiryTime = Instant.now().plusSeconds(clientCredentials.getExpiresIn()); // 만료 시간 설정
                spotifyApi.setAccessToken(accessToken);
                System.out.println("[Spotify] New Access Token: " + accessToken);
            } catch (IOException | se.michaelthelin.spotify.exceptions.SpotifyWebApiException |
                     org.apache.hc.core5.http.ParseException e) {
                System.err.println("[Spotify] Failed to get Access Token: " + e.getMessage());
                return null;
            }
        }

        return accessToken;
    }
}