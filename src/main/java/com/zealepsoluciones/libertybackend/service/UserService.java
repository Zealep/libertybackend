package com.zealepsoluciones.libertybackend.service;

import com.zealepsoluciones.libertybackend.model.entity.User;

public interface UserService {
    User register(User user);
    User changePassword(User user, String oldPassword, String newPassword);
    User resetPassword(String username);
}
