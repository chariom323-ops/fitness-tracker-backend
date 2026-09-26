package com.project.fitness.service;

import com.project.fitness.dto.LoginRequest;
import com.project.fitness.dto.RegisterRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.User;
import com.project.fitness.model.UserRole;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserResponse register(RegisterRequest request)
    {
        UserRole role=request.getRole()!=null?request.getRole():UserRole.USER;
        User user=User.builder()
                        .email(request.getEmail())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                      .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        /* :----we use builder pattern rather  than this specific fiewld ke liye null wagera add karna padega ----:
        User user=new User(
                null,
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                Instant.parse("2026-05-06T10:15:30Z")
                        .atZone(ZoneOffset.UTC)
                        .toLocalDateTime(),
                Instant.parse("2026-05-06T10:15:30Z")
                        .atZone(ZoneOffset.UTC)
                        .toLocalDateTime(),
                List.of(),
                List.of()

        );*/

        User userSaved= userRepository.save(user);
        return mapToResponse(userSaved);
    }

    public UserResponse mapToResponse(User userSaved) {
        UserResponse response=new UserResponse();
       response.setId(userSaved.getId());
       response.setEmail(userSaved.getEmail());
       response.setPassword(userSaved.getPassword());
        response.setFirstName(userSaved.getFirstName());
        response.setLastName(userSaved.getLastName());
        response.setCreatedAt(userSaved.getCreatedAt());
        response.setUpdatedAt(userSaved.getUpdatedAt());

return response;

    }

    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new RuntimeException("invalid credential");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("invalid credential");
        }
        return user;
    }
}
