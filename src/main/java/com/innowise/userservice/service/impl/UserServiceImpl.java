package com.innowise.userservice.service.impl;

import com.innowise.userservice.aop.MultiCacheable;
import com.innowise.userservice.dto.UserDto;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.exception.ObjectNotFoundException;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper mapper;

  @Override
  @Transactional
  @CachePut(value = "users", key = "#userData.id")
  public UserDto createUser(UserDto userData) {
    User newUser = mapper.toUser(userData);
    return mapper.toUserDto(userRepository.save(newUser));
  }

  @Override
  @Cacheable(value = "users", key = "#id")
  public UserDto getUserById(long id) {
    return mapper.toUserDto(getDBUserById(id));
  }

  @Override
  public User getDBUserById(long id) {
    Optional<User> byId = userRepository.findById(id);
    if (byId.isEmpty()) {
      throw ObjectNotFoundException.entityNotFound(User.class, "id", id);
    }
    return byId.get();
  }

  @Override
  @MultiCacheable(value = "users", keysParamName = "ids", keyName = "id")
  public List<UserDto> getUsersByIds(List<Long> ids) {
    return userRepository.findAllById(ids).stream().map(mapper::toUserDto).toList();
  }

  @Override
  @Cacheable(value = "users", key = "#email")
  public UserDto getUserByEmail(String email) {
    User byEmail = userRepository.findByEmail(email);
    if (byEmail == null) {
      throw ObjectNotFoundException.entityNotFound(User.class, "email", email);
    }
    return mapper.toUserDto(byEmail);
  }

  @Override
  @Transactional
  public UserDto updateUserById(UserDto userData) {
    checkIfExists(userData.getId());
    return mapper.toUserDto(userRepository.save(mapper.toUser(userData)));
  }

  @Override
  @Transactional
  public void deleteUserById(long id) {
    checkIfExists(id);
    userRepository.deleteById(id);
  }

  private void checkIfExists(long id) {
    boolean exists = userRepository.existsById(id);
    if (!exists) {
      throw ObjectNotFoundException.entityNotFound(User.class, "id", id);
    }
  }
}
