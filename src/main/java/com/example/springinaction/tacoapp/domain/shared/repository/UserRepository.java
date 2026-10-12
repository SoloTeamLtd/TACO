package com.example.springinaction.tacoapp.domain.shared.repository;

import com.example.springinaction.tacoapp.domain.shared.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
