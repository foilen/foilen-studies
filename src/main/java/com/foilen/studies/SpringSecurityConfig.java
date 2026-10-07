package com.foilen.studies;

import com.foilen.smalltools.tools.AbstractBasics;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import jakarta.servlet.http.HttpSession;

import java.time.Duration;

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

    /**
     * The session cookie lasts 2 weeks. It is sent again on every request (see {@link #sessionCookieRenewFilter(CookieSerializer)}) to push the expiration forward.
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieMaxAge((int) Duration.ofDays(14).toSeconds());
        return serializer;
    }

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> sessionCookieRenewFilter(CookieSerializer cookieSerializer) {
        OncePerRequestFilter filter = new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                    throws jakarta.servlet.ServletException, java.io.IOException {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, session.getId()));
                }
                chain.doFilter(request, response);
            }
        };
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>(filter);
        // Right after the Spring Session filter (which is at HIGHEST_PRECEDENCE + 50)
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 60);
        return registration;
    }

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
