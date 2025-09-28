package com.innowise.userservice.service;

import com.innowise.userservice.dto.UserDto;
import com.innowise.userservice.entity.User;
import java.util.List;

public interface UserService {

  public UserDto createUser(UserDto userData);

  public UserDto getUserById(long id);

  public User getDBUserById(long id);

  public List<UserDto> getUsersByIds(List<Long> ids);

  public UserDto getUserByEmail(String email);

  public UserDto updateUserById(UserDto userData);

  public void deleteUserById(long id);
}
