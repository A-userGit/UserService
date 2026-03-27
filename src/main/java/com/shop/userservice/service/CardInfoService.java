package com.shop.userservice.service;

import com.shop.userservice.dto.CardInfoDto;
import com.shop.userservice.dto.CreateCardInfoDto;
import java.util.List;

public interface CardInfoService {

  CardInfoDto createCardInfo(CreateCardInfoDto cardData);

  CardInfoDto getCardInfoById(long id);

  List<CardInfoDto> getCardInfoByIds(List<Long> ids);

  CardInfoDto updateCardInfoById(CardInfoDto cardInfoData);

  void deleteCardInfoById(long id);

}
