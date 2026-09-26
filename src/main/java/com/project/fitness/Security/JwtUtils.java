package com.project.fitness.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

public class JwtUtils {

    private String jwtSecret =
            "bXlzZWNyZXRrZXlteXNlY3JldGtleW15c2VjcmV0a2V5";

    private long jwtExpirationMs = 86400000;

    // SECRET KEY
    private Key key() {

        return Keys.hmacShaKeyFor(

                Decoders.BASE64.decode(jwtSecret)

        );

    }

    // GENERATE TOKEN
    public String generateToken(String userId,String role
    ) {

        return Jwts.builder()

                .subject(userId)

                .claim(

                        "roles",

                        List.of(role)

                )

                .issuedAt(new Date())

                .expiration(

                        new Date(

                                new Date().getTime()
                                        + jwtExpirationMs

                        )

                )

                .signWith(key())

                .compact();

    }

    // GET JWT FROM HEADER
    public String getJwtFromHeader(
            HttpServletRequest request
    ) {

        String bearerToken =

                request.getHeader("Authorization");

        if (

                bearerToken != null

                        &&

                        bearerToken.startsWith("Bearer ")

        ) {

            return bearerToken.substring(7);

        }

        return null;

    }

    // VALIDATE TOKEN
    public boolean validateJwtToken(
            String jwtToken
    ) {

        try {

            Jwts.parser()

                    .verifyWith((SecretKey) key())

                    .build()

                    .parseSignedClaims(jwtToken);

        }

        catch (Exception e) {

            e.printStackTrace();

            return false;

        }

        return true;

    }

    // GET USERNAME
    public String getUserIdFromToken(
            String jwtToken
    ) {

        return Jwts.parser()

                .verifyWith((SecretKey) key())

                .build()

                .parseSignedClaims(jwtToken)

                .getPayload()

                .getSubject();

    }

    // GET ALL CLAIMS
    public Claims getAllClaims(
            String jwt
    ) {

        return Jwts.parser()

                .verifyWith((SecretKey) key())

                .build()

                .parseSignedClaims(jwt)

                .getPayload();

    }


}
