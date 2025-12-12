package com.zealepsoluciones.libertybackend.repository;

import com.zealepsoluciones.libertybackend.model.entity.User;
import com.zealepsoluciones.libertybackend.model.enums.State;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsernameAndState(String username, State state);
}

