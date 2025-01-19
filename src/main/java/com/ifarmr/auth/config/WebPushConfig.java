package com.ifarmr.auth.config;

import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

@Configuration
public class WebPushConfig implements WebMvcConfigurer {


    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")  // Allow all headers
                .allowCredentials(true);  // Allow credentials (cookies);
    }
    @Bean
    public PushService pushService() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        PushService pushService = new PushService();
        pushService.setPublicKey("BPTTIG9nAK9XPPjt3mkkl7iAm3HWliKl1D6WdYf8nIhFJLM9rK3-TATobVfEmzjP6qhlcx0mhlkF6QiWRRtp5HE");
        pushService.setPrivateKey("D5yHdk-c5RS5aZOceaS0_KWZyGY9eNvUx5gIJzFsdYw");
        pushService.setSubject("mailto:tomilolaaturaka@gmail.com");
        return pushService;
    }
}

