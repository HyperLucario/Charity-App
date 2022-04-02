package com.mihneatudor.softbinatorcharityproject.services;

import com.mihneatudor.softbinatorcharityproject.dtos.UserDto;
import com.mihneatudor.softbinatorcharityproject.models.User;
import com.mihneatudor.softbinatorcharityproject.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUser(Long id) {
        User user =  userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .toList();
    }

    public Long addUser(User user) {
        // Check for unique username & email
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with the given username already" +
                    " exists in the database");
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with the given email already" +
                    " exists in the database");
        }

        return userRepository.save(user).getId();
    }

    public UserDto deleteUser(Long id) {
        User user =  userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        UserDto userData = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        userRepository.delete(user);

        return userData;
    }
}
