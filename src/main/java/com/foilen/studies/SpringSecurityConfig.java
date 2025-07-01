package com.foilen.studies;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.studies.security.LocalAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SpringSecurityConfig extends AbstractBasics {

    @Value("${app.auth.local.enabled:false}")
    private boolean localAuthEnabled;
    @Value("${app.auth.local.userId:local-user}")
    private String localUserId;

    @Bean
    public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf
                .csrfTokenRepository(cookieCsrfTokenRepository())
                .csrfTokenRequestHandler((request, response, supplier) -> {
                    String token = request.getHeader("X-XSRF-TOKEN");
                    if (token != null) {
                        request.setAttribute("_csrf", token);
                    }
                }));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/appDetails/**").permitAll()
                .requestMatchers("/user/isLoggedIn").permitAll()
                .anyRequest().authenticated()
        );

        // Configure authentication based on local or OAuth2
        if (localAuthEnabled) {
            // Add local authentication filter for development
            http.addFilterBefore(
                    new LocalAuthenticationFilter(localUserId),
                    BasicAuthenticationFilter.class
            );
            logger.info("Local authentication enabled with user ID: {}", localUserId);
        } else {
            // Use OAuth2 login for production
            http.oauth2Login(Customizer.withDefaults());
            http.oauth2Client(oauth2 -> {
            });
        }

        return http.build();
    }

}
