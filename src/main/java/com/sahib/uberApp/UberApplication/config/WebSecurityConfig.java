package com.sahib.uberApp.UberApplication.config;

import com.sahib.uberApp.UberApplication.enums.Roles;
import com.sahib.uberApp.UberApplication.filter.JwtAuthFilter;
import com.sahib.uberApp.UberApplication.handler.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 1 step : we have to enable
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    public static final String[] PUBLIC_ROUTES = {"/auth/signUp" ,
            "/auth/login" ,
            "/swagger-ui/index.html" ,
            "/v3/api-docs/**" ,
            "/swagger-ui/**" ,
            "/auth/refreshToken",
    };

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
        // ✅ ENABLE CORS (THIS WAS MISSING)
        .cors(Customizer.withDefaults())
                .
                authorizeHttpRequests(auth->
                auth.requestMatchers(PUBLIC_ROUTES).permitAll()
                        .requestMatchers("/ws", "/ws/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/admin/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())
                        .requestMatchers("/driver/onBoardNewDriver").hasRole(Roles.RIDER.name())
                        .requestMatchers("/driver/**").hasRole(Roles.DRIVER.name())
                        .requestMatchers("/rider/**" , "/ride/**").hasRole(Roles.RIDER.name())
                        .anyRequest().authenticated())

                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oAuth->oAuth.failureUrl("/login?error=true")
                        .successHandler(oAuth2SuccessHandler)

                )
                .formLogin(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }


    @Bean
    AuthenticationManager getAuthenticationManager(AuthenticationConfiguration configuration) throws Exception {
     return  configuration.getAuthenticationManager();
    }

//    @Bean
//    @org.springframework.core.annotation.Order(1)
//    public SecurityFilterChain webSocketSecurityFilterChain(
//            HttpSecurity http) throws Exception {
//
//        http
//                .securityMatcher("/ws", "/ws/**")
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
//
//        return http.build();
//    }
}
