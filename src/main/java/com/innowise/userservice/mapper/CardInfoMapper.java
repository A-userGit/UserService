package com.innowise.userservice.mapper;

import com.innowise.userservice.dto.CardInfoDto;
import com.innowise.userservice.entity.CardInfo;
import com.innowise.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CardInfoMapper {

  @Mapping(source = "expirationDate", target = "expirationDate", dateFormat = "dd.MM.yyyy")
  @Mapping(source = "source.user.id", target = "userId")
  CardInfoDto toCardInfoDto(CardInfo source);

  @Mapping(target = "expirationDate", dateFormat = "dd-MM-yyyy")
  @Mapping(source = "userId", target = "user", qualifiedByName = "setUserWithId")
  CardInfo toCardInfo(CardInfoDto source);

  @Named("setUserWithId")
  public static User setUserWithId(int userId) {
    User user = new User();
    user.setId(userId);
    return user;
  }
}
