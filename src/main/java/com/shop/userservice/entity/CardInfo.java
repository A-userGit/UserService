package com.innowise.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "card_info")
public class CardInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq")
  @SequenceGenerator(name = "card_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "number", nullable = false, unique = true)
  private String number;

  @Column(name = "holder", nullable = false)
  private String holder;

  @Column(name = "expiration_date", nullable = false)
  private Date expirationDate;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CardInfo cardInfo = (CardInfo) o;
    return id == cardInfo.id && Objects.equals(user, cardInfo.user)
        && Objects.equals(number, cardInfo.number) && Objects.equals(holder,
        cardInfo.holder) && Objects.equals(expirationDate, cardInfo.expirationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, number, holder, expirationDate);
  }
}