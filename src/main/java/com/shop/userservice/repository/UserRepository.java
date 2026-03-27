package com.innowise.userservice.repository;

import com.innowise.userservice.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query(value = "from users as u where u.email = :email")
  User findByEmail(String email);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  void deleteById(Long id);
}
