package com.example.address_book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.address_book.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
