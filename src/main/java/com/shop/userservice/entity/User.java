package com.innowise.userservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
  @SequenceGenerator(name = "users_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @Column(name = "birth_date")
  private Date birthDate;

  @Column(name = "email")
  private String email;

  @Column(name = "external_id")
  private long externalId;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<CardInfo> cards;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id == user.id && Objects.equals(name, user.name) && Objects.equals(
        surname, user.surname) && Objects.equals(birthDate, user.birthDate)
        && Objects.equals(email, user.email) && Objects.equals(cards, user.cards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, surname, birthDate, email, cards);
  }
}
