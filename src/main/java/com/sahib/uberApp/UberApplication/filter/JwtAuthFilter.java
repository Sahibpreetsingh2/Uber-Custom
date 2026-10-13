package com.sahib.uberApp.UberApplication.filter;

import com.sahib.uberApp.UberApplication.entities.Users;
import com.sahib.uberApp.UberApplication.services.JwtService;
import com.sahib.uberApp.UberApplication.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        try {
            String authorization = request.getHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorization.substring(7);
            Long userId = jwtService.generateUserIdFromToken(token);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                Users users = userService.findById(userId);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        users, null, users.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // system info store :
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // store user  in the context holder
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;   //  important: stop chain
        }
    }
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return request.getServletPath().equals("/ws")
//                || request.getServletPath().startsWith("/ws/");
//    }
}
