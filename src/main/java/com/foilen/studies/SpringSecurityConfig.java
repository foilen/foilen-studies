package com.foilen.studies;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@Profile("!test")
public class SpringSecurityConfig {

    @Bean
    public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
        var csrfTokenRepository = new CookieCsrfTokenRepository();
        csrfTokenRepository.setCookieHttpOnly(false);
        return csrfTokenRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf()
                .csrfTokenRepository(cookieCsrfTokenRepository())
                .csrfTokenRequestHandler((request, response, supplier) -> {
            String token = request.getHeader("X-XSRF-TOKEN");
            if (token != null) {
                request.setAttribute("_csrf", token);
            }
        });

        http.authorizeHttpRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/appDetails/**").permitAll()
                .requestMatchers("/user/isLoggedIn").permitAll()
                .anyRequest().authenticated();
        http.oauth2Login(Customizer.withDefaults());
        http.oauth2Client();

        return http.build();
    }

}
