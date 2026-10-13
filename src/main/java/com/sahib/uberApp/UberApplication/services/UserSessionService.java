package com.sahib.uberApp.UberApplication.services;

import com.sahib.uberApp.UberApplication.entities.UserSession;
import com.sahib.uberApp.UberApplication.entities.Users;
import com.sahib.uberApp.UberApplication.exception.ResourceNotFound;
import com.sahib.uberApp.UberApplication.repositories.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    @Value("${max_user_session}")
    private int max_user_session;  // 2

    public void generateUserSession(Users users, String refreshToken) {

        List<UserSession> sessionList = userSessionRepository.findByUsers(users);

        if (sessionList.size() == max_user_session){
            sessionList.sort(Comparator.comparing(UserSession::getCreatedAt)); // sort ascending order :
            UserSession userSession = sessionList.getFirst();
            userSessionRepository.delete(userSession);
        }

        UserSession userSession = UserSession.builder()
                .users(users)
                .refreshToken(refreshToken)
                .createdAt(LocalDateTime.now())
                .build();
        userSessionRepository.save(userSession);
    }

    public void validateUserSession(String refreshToken) {
        UserSession userSession = userSessionRepository.findByRefreshToken(refreshToken).orElseThrow(() ->
                new ResourceNotFound("User not found for refresh tokem  : " + refreshToken));

        userSession.setCreatedAt(LocalDateTime.now());
        userSessionRepository.save(userSession);
    }
}
