package com.sahib.uberApp.UberApplication.handler;

import com.sahib.uberApp.UberApplication.entities.Users;
import com.sahib.uberApp.UberApplication.services.JwtService;
import com.sahib.uberApp.UberApplication.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    @Value("${deploy.env}")
    private String env;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) oAuth2AuthenticationToken.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        System.out.println("email : "+email);

        Users users = userService.findByEmail(email);

        if (users == null){
            users = Users.builder()
                    .name(name)
                    .email(email)
                    .build();

            userService.save(users);
        }

        String accessToken = jwtService.generateAccessToken(users);
        String refreshToken = jwtService.generateRefreshToken(users);

        Cookie cookie = new Cookie("refreshToken" ,refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(env.equals("production")); // https
        response.addCookie(cookie);


        String url = "http://localhost:8080/home.html?token="+accessToken;
        response.sendRedirect(url);
    }
}
