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
public class CardInfoDto {

  private long id;
  private long userId;

  @CreditCardNumber(message = "Invalid credit card number", ignoreNonDigitCharacters = true)
  private String number;

  private String holder;

  @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-\\d{4}$",
      message = "Invalid date format. The expected format is dd-MM-yyyy")
  private String expirationDate;
}
