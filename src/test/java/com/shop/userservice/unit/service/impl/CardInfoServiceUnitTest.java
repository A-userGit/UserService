package com.innowise.userservice.unit.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.userservice.dto.CardInfoDto;
import com.innowise.userservice.dto.CreateCardInfoDto;
import com.innowise.userservice.entity.CardInfo;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.exception.ObjectNotFoundException;
import com.innowise.userservice.mapper.CardInfoMapper;
import com.innowise.userservice.redis.RedisCacheRepository;
import com.innowise.userservice.repository.CardInfoRepository;
import com.innowise.userservice.service.UserService;
import com.innowise.userservice.service.impl.CardInfoServiceImpl;
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

@DisplayName("Tests for cardInfo service")
@ExtendWith(MockitoExtension.class)
public class CardInfoServiceUnitTest {

  @Mock
  private CardInfoRepository cardInfoRepository;

  @Mock
  private RedisCacheRepository redisCacheRepository;

  @Mock
  private UserService userService;
  @Mock
  private CardInfoMapper mapper;

  @InjectMocks
  private CardInfoServiceImpl cardInfoService;

  @Test
  @DisplayName("Try to create record")
  public void createCardInfoUnitTest() {
    CardInfoDto cardInfoDto = getCardInfoDto();
    CardInfo cardInfo = getCardInfo();
    User user = new User();
    user.setId(1);
    when(userService.getDBUserById(anyLong())).thenReturn(user);
    when(cardInfoRepository.save(any())).thenReturn(cardInfo);
    when(mapper.toCardInfoDto(any(CardInfo.class))).thenReturn(cardInfoDto);
    when(mapper.toCardInfo(any(CreateCardInfoDto.class))).thenReturn(cardInfo);
    CardInfoDto cardInfoCreated = cardInfoService.createCardInfo(cardInfoDto);
    verify(cardInfoRepository, times(1)).save(any());
    verify(mapper, times(1)).toCardInfoDto(any());
    assertEquals(3, cardInfoCreated.getId());
  }

  @Test
  @DisplayName("Try to update record that does not exist")
  public void updateCardInfoUnitTestFail() {
    CardInfoDto cardInfoDto = getCardInfoDto();
    when(cardInfoRepository.existsById(any())).thenReturn(false);
    assertThrows(ObjectNotFoundException.class,
        () -> cardInfoService.updateCardInfoById(cardInfoDto));
    verify(cardInfoRepository, times(1)).existsById(any());
  }

  @Test
  @DisplayName("Try to update record that exists")
  public void updateCardInfoUnitTest() {
    CardInfoDto cardInfoDto = getCardInfoDto();
    CardInfo cardInfo = getCardInfo();
    when(cardInfoRepository.existsById(any())).thenReturn(true);
    when(cardInfoRepository.save(any())).thenReturn(cardInfo);
    when(mapper.toCardInfoDto(any())).thenReturn(cardInfoDto);
    when(mapper.toCardInfo(any())).thenReturn(cardInfo);
    cardInfoService.updateCardInfoById(cardInfoDto);
    verify(cardInfoRepository, times(1)).existsById(any());
    verify(cardInfoRepository, times(1)).save(any());
    verify(mapper, times(1)).toCardInfoDto(any());
  }

  @Test
  @DisplayName("Try to get record that exists by id")
  public void getCardInfoByIdUnitTest() {
    CardInfoDto cardInfoDto = getCardInfoDto();
    CardInfo cardInfo = getCardInfo();
    when(cardInfoRepository.findById(any())).thenReturn(Optional.of(cardInfo));
    when(mapper.toCardInfoDto(any())).thenReturn(cardInfoDto);
    cardInfoService.getCardInfoById(1);
    verify(cardInfoRepository, times(1)).findById(any());
    verify(mapper, times(1)).toCardInfoDto(any());
  }

  @Test
  @DisplayName("Try to get record that don't exists by id")
  public void getCardInfoByIdUnitTestFail() {
    when(cardInfoRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class, () -> cardInfoService.getCardInfoById(1));
    verify(cardInfoRepository, times(1)).findById(any());
  }

  @Test
  @DisplayName("Try to get records by id list")
  public void getCardInfoByIdsUnitTest() {
    List<Long> ids = List.of(1L, 2L, 3L, 4L);
    List<CardInfo> cards = new ArrayList<>();
    cards.add(getCardInfo());
    cards.add(getCardInfo());
    when(cardInfoRepository.findAllById(any())).thenReturn(cards);
    when(mapper.toCardInfoDto(any())).thenReturn(getCardInfoDto());
    List<CardInfoDto> cardInfoByIds = cardInfoService.getCardInfoByIds(ids);
    assertEquals(2, cardInfoByIds.size());
    verify(cardInfoRepository, times(1)).findAllById(any());
  }

  @Test
  @DisplayName("Try to delete existing record")
  public void UnitTest() {
    when(cardInfoRepository.existsById(any())).thenReturn(true);
    cardInfoService.deleteCardInfoById(1);
    verify(cardInfoRepository, times(1)).deleteById(any());
  }

  private CardInfo getCardInfo() {
    Date date = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
    CardInfo cardInfo = new CardInfo();
    cardInfo.setNumber("4242 4242 4242 4242");
    cardInfo.setId(3);
    cardInfo.setExpirationDate(date);
    User user = new User();
    user.setId(1);
    cardInfo.setUser(user);
    return cardInfo;
  }

  private CardInfoDto getCardInfoDto() {
    CardInfoDto cardInfoDto = new CardInfoDto();
    cardInfoDto.setHolder("test");
    cardInfoDto.setUserId(1);
    cardInfoDto.setId(3);
    cardInfoDto.setNumber("4242 4242 4242 4242");
    return cardInfoDto;
  }
}
