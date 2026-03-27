package com.shop.userservice.service;

import com.shop.userservice.dto.CreateUserDto;
import com.shop.userservice.dto.ShortUserDto;
import com.shop.userservice.dto.UserDto;
import com.shop.userservice.entity.User;
import java.util.List;

public interface UserService {

  UserDto createUser(CreateUserDto userData);

  UserDto getUserById(long id);

  User getDBUserById(long id);

  List<UserDto> getUsersByIds(List<Long> ids);

  UserDto getUserByEmail(String email);

  UserDto updateUserById(UserDto userData);

  void deleteUserById(long id);

  ShortUserDto getCurrentUser();

  UserDto getFullCurrentUser();
}
