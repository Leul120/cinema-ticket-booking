package com.cinema_ticket.repositories;


import com.cinema_ticket.entities.Role;
import com.cinema_ticket.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    User findByRole(Role role);
}
