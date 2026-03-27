package com.shop.userservice.mapper;

import com.shop.userservice.dto.CreateUserDto;
import com.shop.userservice.dto.ShortUserDto;
import com.shop.userservice.dto.UserDto;
import com.shop.userservice.entity.User;
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

  ShortUserDto toShortUserDto(User user);

}
