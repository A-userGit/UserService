package com.innowise.userservice.mapper;

import com.innowise.userservice.dto.CreateUserDto;
import com.innowise.userservice.dto.UserDto;
import com.innowise.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "cards", expression = "java(null)")
  @Mapping(target = "birthDate", dateFormat = "dd-MM-yyyy")
  public User toUser(UserDto source);

  @Mapping(source = "birthDate", target = "birthDate", dateFormat = "dd-MM-yyyy")
  public UserDto toUserDto(User source);

  @Mapping(target = "cards", expression = "java(null)")
  @Mapping(target = "birthDate", dateFormat = "dd-MM-yyyy")
  public User toUser(CreateUserDto source);

}
