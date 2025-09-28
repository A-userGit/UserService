package com.innowise.userservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.userservice.dto.UserDto;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.exception.ObjectNotFoundException;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.repository.UserRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Tests for user service")
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper mapper;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  @DisplayName("Try to create user")
  public void createUserUnitTest() {
    User user = getUser();
    UserDto userDto = getUserDto();
    when(userRepository.save(any())).thenReturn(user);
    when(mapper.toUserDto(any())).thenReturn(userDto);
    when(mapper.toUser(any())).thenReturn(user);
    UserDto result = userService.createUser(userDto);
    verify(userRepository, times(1)).save(any());
    verify(mapper, times(1)).toUserDto(any());
    assertEquals(3, result.getId());
  }

  @Test
  @DisplayName("Try to update user that does not exist")
  public void updateCardInfoUnitTestFail() {
    UserDto userDto = getUserDto();
    when(userRepository.existsById(any())).thenReturn(false);
    assertThrows(ObjectNotFoundException.class,
        () -> userService.updateUserById(userDto));
    verify(userRepository, times(1)).existsById(any());
  }

  @Test
  @DisplayName("Try to update user that exists")
  public void updateCardInfoUnitTest() {
    UserDto userDto = getUserDto();
    User user = getUser();
    when(userRepository.existsById(any())).thenReturn(true);
    when(userRepository.save(any())).thenReturn(user);
    when(mapper.toUserDto(any())).thenReturn(userDto);
    when(mapper.toUser(any())).thenReturn(user);
    userService.updateUserById(userDto);
    verify(userRepository, times(1)).existsById(any());
    verify(userRepository, times(1)).save(any());
    verify(mapper, times(1)).toUserDto(any());
  }

  @Test
  @DisplayName("Try to get user that exists by id")
  public void getCardInfoByIdUnitTest() {
    UserDto userDto = getUserDto();
    User user = getUser();
    when(userRepository.findById(any())).thenReturn(Optional.of(user));
    when(mapper.toUserDto(any())).thenReturn(userDto);
    userService.getUserById(1);
    verify(userRepository, times(1)).findById(any());
    verify(mapper, times(1)).toUserDto(any());
  }

  @Test
  @DisplayName("Try to get user that don't exists by id")
  public void getCardInfoByIdUnitTestFail() {
    when(userRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class, () -> userService.getUserById(1));
    verify(userRepository, times(1)).findById(any());
  }

  @Test
  @DisplayName("Try to get users by id list")
  public void getCardInfoByIdsUnitTest() {
    List<Long> ids = List.of(1L, 2L, 3L, 4L);
    List<User> users = new ArrayList<>();
    users.add(getUser());
    users.add(getUser());
    when(userRepository.findAllById(any())).thenReturn(users);
    when(mapper.toUserDto(any())).thenReturn(getUserDto());
    List<UserDto> usersByIds = userService.getUsersByIds(ids);
    assertEquals(2, usersByIds.size());
    verify(userRepository, times(1)).findAllById(any());
  }

  @Test
  @DisplayName("Try to delete existing user")
  public void UnitTest() {
    when(userRepository.existsById(any())).thenReturn(true);
    userService.deleteUserById(1);
    verify(userRepository, times(1)).deleteById(any());
  }

  private User getUser() {
    Date date = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
    User user = new User();
    user.setId(3);
    user.setSurname("test");
    user.setName("test");
    user.setEmail("test@mail.com");
    user.setBirthDate(date);
    return user;
  }

  private UserDto getUserDto() {
    UserDto userDto = new UserDto();
    userDto.setBirthDate("12-11-2005");
    userDto.setId(3);
    userDto.setName("test");
    userDto.setSurname("test");
    userDto.setEmail("test@mail.com");
    return userDto;
  }

}
