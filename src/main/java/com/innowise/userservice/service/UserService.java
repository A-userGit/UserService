package com.innowise.userservice.service;

import com.innowise.userservice.dto.CreateUserDto;
import com.innowise.userservice.dto.UserDto;
import com.innowise.userservice.entity.User;
import java.util.List;

public interface UserService {

  UserDto createUser(CreateUserDto userData);

  UserDto getUserById(long id);

  User getDBUserById(long id);

  List<UserDto> getUsersByIds(List<Long> ids);

  UserDto getUserByEmail(String email);

  UserDto updateUserById(UserDto userData);

  void deleteUserById(long id);
}
