package com.innowise.userservice.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardInfoDto extends CreateCardInfoDto {

  private long id;

  public CardInfoDto(long id, long userId, String number, String holder, String expirationDate) {
    super(userId, number, holder, expirationDate);
    this.id = id;
  }
}
