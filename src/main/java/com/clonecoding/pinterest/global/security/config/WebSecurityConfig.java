package com.clonecoding.pinterest.global.security.config;

import com.clonecoding.pinterest.global.security.exception.MyAccessDeniedHandler;
import com.clonecoding.pinterest.global.security.exception.MyAuthenticationEntryPoint;
import com.clonecoding.pinterest.global.security.exception.ResponseUtil;
import com.clonecoding.pinterest.global.security.filter.JwtAuthenticationFilter;
import com.clonecoding.pinterest.global.security.filter.JwtAuthorizationFilter;
import com.clonecoding.pinterest.global.security.filter.UserDetailsServiceImpl;
import com.clonecoding.pinterest.global.security.jwt.JwtUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
    @NonNull
    private JwtUtil jwtUtil;
    @NonNull
    private UserDetailsServiceImpl userDetailsService;
    @NonNull
    private AuthenticationConfiguration authenticationConfiguration;
    @NonNull
    private ResponseUtil responseUtil;

    @Value("${client.url}")
    private String clientUrl;
    public static final String[] permitAllRouteArray = {
            "/api/user/signup", "/api/user/cors", "/api/user/kakao/login", "/swagger-ui/**", "/webjars/**",
            "/v3/api-docs/**", "/swagger-resources/**", "/api/file/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, responseUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(jwtUtil, responseUtil, userDetailsService);
    }

    @Bean
    public MyAccessDeniedHandler myAccessDeniedHandler() {
        return new MyAccessDeniedHandler(responseUtil);
    }

    @Bean
    public MyAuthenticationEntryPoint myAuthenticationEntryPoint() {
        return new MyAuthenticationEntryPoint(responseUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.cors(withDefaults());

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests((authorizeHttpRequest) ->
                        authorizeHttpRequest
                                .requestMatchers(permitAllRouteArray).permitAll()
                                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling
                        .authenticationEntryPoint(myAuthenticationEntryPoint())
                        .accessDeniedHandler(myAccessDeniedHandler())
        );
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(clientUrl));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        configuration.addExposedHeader(JwtUtil.AUTHORIZATION_HEADER);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
