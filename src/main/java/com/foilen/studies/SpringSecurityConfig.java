package com.foilen.studies;

import com.foilen.smalltools.tools.AbstractBasics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SpringSecurityConfig extends AbstractBasics {

    @Bean
    public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf
                .csrfTokenRepository(cookieCsrfTokenRepository())
                .csrfTokenRequestHandler((request, _, _) -> {
                    String token = request.getHeader("X-XSRF-TOKEN");
                    if (token != null) {
                        request.setAttribute("_csrf", token);
                    }
                }));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll()
                .requestMatchers("/index.html").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/appDetails/**").permitAll()
                .requestMatchers("/user/csrf").permitAll()
                .requestMatchers("/user/isLoggedIn").permitAll()
                .requestMatchers("/user/login").permitAll()
                .requestMatchers("/user/loginWithCodeRequest").permitAll()
                .requestMatchers("/user/loginWithCode").permitAll()
                .requestMatchers("/user/logout").permitAll()
                .anyRequest().authenticated()
        );

        // The UI handles the login page
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        return http.build();
    }

}
