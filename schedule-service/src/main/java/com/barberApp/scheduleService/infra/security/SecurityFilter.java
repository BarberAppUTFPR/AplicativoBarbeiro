package com.barberApp.scheduleService.infra.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.barberApp.scheduleService.dtos.AuthenticatedUserDTO;
import com.barberApp.scheduleService.enums.User_Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
    private final JwtUtils jwtUtils;

    public SecurityFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = jwtUtils.retrieveToken(request);

        if (tokenJWT != null) {
            try {
                DecodedJWT jwt = jwtUtils.validateAndDecode(tokenJWT);
                AuthenticatedUserDTO userDTO = extractUserFromJwt(jwt);
                List<SimpleGrantedAuthority> authorities = buildAuthorities(userDTO.role());

                var authentication = new UsernamePasswordAuthenticationToken(userDTO, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                logger.error("Error processing JWT token", e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private AuthenticatedUserDTO extractUserFromJwt(DecodedJWT jwt) {
        String email = jwt.getSubject();
        String userIdStr = jwt.getClaim("userId").asString();
        String userRole = jwt.getClaim("user_role").asString();
        UUID userId = UUID.fromString(userIdStr);

        return new AuthenticatedUserDTO(userId, email, userRole);
    }

    private List<SimpleGrantedAuthority> buildAuthorities(String userRole) {
        if (User_Role.ADMIN.name().equals(userRole)) {
            return Stream.of(User_Role.values())
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList());
        } else {
            return List.of(new SimpleGrantedAuthority(userRole));
        }
    }
}
