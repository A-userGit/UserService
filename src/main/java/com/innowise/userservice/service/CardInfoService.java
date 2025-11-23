package com.innowise.userservice.service;

import com.innowise.userservice.dto.CardInfoDto;
import com.innowise.userservice.dto.CreateCardInfoDto;
import java.util.List;

public interface CardInfoService {

  CardInfoDto createCardInfo(CreateCardInfoDto cardData);

  CardInfoDto getCardInfoById(long id);

  List<CardInfoDto> getCardInfoByIds(List<Long> ids);

  CardInfoDto updateCardInfoById(CardInfoDto cardInfoData);

  void deleteCardInfoById(long id);

}
