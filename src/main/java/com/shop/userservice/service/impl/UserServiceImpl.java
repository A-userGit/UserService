package com.shop.userservice.service.impl;

import com.shop.userservice.aop.MultiCacheable;
import com.shop.userservice.dto.CreateUserDto;
import com.shop.userservice.dto.ShortUserDto;
import com.shop.userservice.dto.UserDto;
import com.shop.userservice.entity.User;
import com.shop.userservice.exception.ObjectAlreadyExistsException;
import com.shop.userservice.exception.ObjectNotFoundException;
import com.shop.userservice.mapper.UserMapper;
import com.shop.userservice.redis.RedisCacheRepository;
import com.shop.userservice.repository.UserRepository;
import com.shop.userservice.service.UserService;
import com.shop.userservice.util.SecurityUtil;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RedisCacheRepository redisCacheRepository;
  private final UserMapper mapper;

  @Override
  @Transactional
  public UserDto createUser(CreateUserDto userData) {
    User foundByEmail = userRepository.findByEmail(userData.getEmail());
    if(foundByEmail!=null){
      throw ObjectAlreadyExistsException.entityAlreadyExists("User", "email", userData.getEmail());
    }
    User newUser = mapper.toUser(userData);
    User savedUser = userRepository.save(newUser);
    UserDto userDto = mapper.toUserDto(savedUser);
    redisCacheRepository.putObjectInCache("users", String.valueOf(savedUser.getId()), userDto);
    return userDto;
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

  @Override
  public ShortUserDto getCurrentUser() {
    String email= SecurityUtil.getAuthenticatedUserEmail();
    User byEmail = userRepository.findByEmail(email);
    if(byEmail==null){
      throw new AuthenticationServiceException("User not found.");
    }
    return mapper.toShortUserDto(byEmail);
  }

  @Override
  public UserDto getFullCurrentUser() {
    String email= SecurityUtil.getAuthenticatedUserEmail();
    User byEmail = userRepository.findByEmail(email);
    if(byEmail==null){
      throw new AuthenticationServiceException("User not found.");
    }
    return mapper.toUserDto(byEmail);
  }

  private void checkIfExists(long id) {
    boolean exists = userRepository.existsById(id);
    if (!exists) {
      throw ObjectNotFoundException.entityNotFound(User.class, "id", id);
    }
  }
}
