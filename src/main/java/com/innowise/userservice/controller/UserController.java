package com.innowise.userservice.controller;

import com.innowise.userservice.dto.CreateUserDto;
import com.innowise.userservice.dto.UserDto;
import com.innowise.userservice.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "user_security")
@RestController
@RequestMapping("/api/v1/users/")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("create")
  ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto data) {
    UserDto user = userService.createUser(data);
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  @PatchMapping("update")
  ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto data) {
    UserDto user = userService.updateUserById(data);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("user/by-email")
  ResponseEntity<UserDto> getByEmail(@RequestParam(name = "email") @Email String email) {
    UserDto userByEmail = userService.getUserByEmail(email);
    return new ResponseEntity<>(userByEmail, HttpStatus.OK);
  }

  @GetMapping("user")
  ResponseEntity<UserDto> getById(@RequestParam(name = "id") Long id) {
    UserDto userById = userService.getUserById(id);
    return new ResponseEntity<>(userById, HttpStatus.OK);
  }

  @PostMapping("search")
  ResponseEntity<List<UserDto>> getByIds(@RequestBody List<Long> ids) {
    List<UserDto> users = userService.getUsersByIds(ids);
    return new ResponseEntity<List<UserDto>>(users, HttpStatus.OK);
  }

  @DeleteMapping("delete")
  ResponseEntity<?> deleteById(@RequestParam(name = "id") Long id) {
    userService.deleteUserById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
