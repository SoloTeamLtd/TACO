package com.example.springinaction.tacoapp.repository;

import com.example.springinaction.tacoapp.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
