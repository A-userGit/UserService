package com.innowise.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private long id;
  private String name;
  private String surname;

  @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-\\d{4}$",
      message = "Invalid date format. The expected format is dd-MM-yyyy")
  private String birthDate;

  @Email
  private String email;
}
