package com.barberApp.userService.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.barberApp.userService.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class JwtUtils {


    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public JwtUtils() throws Exception {
        this.publicKey = KeyLoader.loadPublicKey("public_key.pem");
        this.privateKey = KeyLoader.loadPrivateKey("private_key.pem");
    }


    public String generateJwt(User user) {
        Builder tokenBuilder = JWT.create()
                .withIssuer("API BarberApp")
                .withSubject(user.getEmail())
                .withExpiresAt(ExpirationDate())
                .withIssuedAt(Date.from(Instant.now()))
                .withClaim("userId", user.getId().toString())
                .withClaim("user_role", user.getUserRole().toString());

        return tokenBuilder.sign(Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey));
    }

    public String generateConfirmationJwt(User user) {
        Builder tokenBuilder = JWT.create()
                .withIssuer("API BarberApp")
                .withSubject(user.getEmail())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(3600)))
                .withClaim("userId", user.getId().toString())
                .withClaim("email", user.getEmail())
                .withClaim("user_role", user.getUserRole().toString());

        return tokenBuilder.sign(Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey));
    }

    private Instant ExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }


    /**
     * Validate a JWT token
     * //@param -token
     * //@return decoded token
     */
    public String validate(String token) {
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("API BarberApp")
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            String subject = jwt.getSubject();
            if (subject == null) {
                throw new InvalidParameterException("Subject is missing in the token.");
            }

            return subject;

        } catch (Exception e) {
            logger.error("Failed to validate JWT", e);
            throw new InvalidParameterException("JWT validation failed: " + e.getMessage());
        }
    }
    public Boolean boolValidate(String token) {
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("API BarberApp")
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            if (jwt != null) {
                return true;
            }

        } catch (Exception e) {
            logger.error("Failed to validate JWT", e);
            throw new InvalidParameterException("JWT validation failed: " + e.getMessage());
        }
        return false;
    }

    public String retrieveToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null)  return null;
        return authorizationHeader.replace("Bearer ", "");
    }

}
