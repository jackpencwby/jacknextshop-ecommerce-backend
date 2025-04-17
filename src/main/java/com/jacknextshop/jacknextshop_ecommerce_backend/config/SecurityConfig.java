package com.jacknextshop.jacknextshop_ecommerce_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.Customizer;

import com.jacknextshop.jacknextshop_ecommerce_backend.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        private final CustomOAuth2UserService customOAuth2UserService;
        private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

        @Value("${frontend.url}")
        private String frontendUrl;

        public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                                RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
                this.customOAuth2UserService = customOAuth2UserService;
                this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                        .cors(Customizer.withDefaults())
                        .csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/login", "/hello").permitAll()
                                .requestMatchers
                                (HttpMethod.GET, 
                                "/api/product",
                                "/api/category"
                                ).permitAll()
                                .anyRequest().authenticated())
                        .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        )
                        .oauth2Login(oauth2 -> oauth2
                                .loginPage("/login")
                                .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                                .defaultSuccessUrl(frontendUrl, true)
                        )
                        .logout(logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                                .logoutSuccessHandler((request, response, authentication) -> {
                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write("{\"message\": \"Logout Success\"}");
                                })
                        )
                        .build();
        }
}