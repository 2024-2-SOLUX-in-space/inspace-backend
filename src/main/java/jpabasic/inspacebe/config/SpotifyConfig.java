package jpabasic.inspacebe.config;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SpotifyConfig {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Bean
    public SpotifyApi spotifyApi() {
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
    }

    @Bean
    public String spotifyAccessToken(SpotifyApi spotifyApi) {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            System.out.println("[soyg] ✅ Spotify Token: " + clientCredentials.getAccessToken());

            return clientCredentials.getAccessToken();
        } catch (IOException | se.michaelthelin.spotify.exceptions.SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.err.println("[soyg] ❌ Spotify Token Fetch Error: " + e.getMessage());
            return null;
        }
    }
}