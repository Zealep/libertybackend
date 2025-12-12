package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.model.entity.User;
import com.zealepsoluciones.libertybackend.model.enums.State;
import com.zealepsoluciones.libertybackend.repository.UserRepository;
import com.zealepsoluciones.libertybackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameAndState(username, State.ACTIVE).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }


    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setState(State.ACTIVE);
        return userRepository.save(user);
    }

    @Override
    public User changePassword(User user, String oldPassword, String newPassword) {
        return null;
    }

    @Override
    public User resetPassword(String username) {
        return null;
    }
}

