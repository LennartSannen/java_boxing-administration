package com.defence.administration.repository;

import com.defence.administration.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional <User> findByUsernameIgnoreCase(String name);

}
