package com.project.fitness.Security;

import com.project.fitness.Security.JwtUtils;
import java.util.stream.Collectors;
import io.jsonwebtoken.Claims;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.List;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    // PARSE JWT
    private String parseJwt(
            HttpServletRequest request
    ) {

        String jwt =
                jwtUtils.getJwtFromHeader(request);

        return jwt;

    }

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    ) throws ServletException, IOException {

        System.out.println(
                "AuthTokenFilter Executed"
        );

        try {

            String jwt = parseJwt(request);

            if (

                    jwt != null

                            &&

                            jwtUtils.validateJwtToken(jwt)

            ) {

                String userId =

                        jwtUtils.getUserIdFromToken(jwt);

                // GET CLAIMS
                Claims claims =
                        jwtUtils.getAllClaims(jwt);

                // GET ROLES
                List<String> roles =

                        claims.get(
                                "roles",
                                List.class
                        );

                System.out.println(
                        "Roles : " + roles
                );

                // ROLE TO AUTHORITIEs

                List<GrantedAuthority> authorities =

                        roles.stream()

                                .map(SimpleGrantedAuthority::new)

                                .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication =

                        new UsernamePasswordAuthenticationToken(

                                userId,

                                null,

                                authorities

                        );

                authentication.setDetails(

                        new WebAuthenticationDetailsSource()

                                .buildDetails(request)

                );

                SecurityContextHolder

                        .getContext()

                        .setAuthentication(authentication);

            }

        }

        catch (Exception e) {

            e.printStackTrace();

        }

        filterChain.doFilter(
                request,
                response
        );

    }

}
