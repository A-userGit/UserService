package com.innowise.userservice.service;

import com.innowise.userservice.dto.CardInfoDto;
import java.util.List;

public interface CardInfoService {

  public CardInfoDto createCardInfo(CardInfoDto cardData);

  public CardInfoDto getCardInfoById(long id);

  public List<CardInfoDto> getCardInfoByIds(List<Long> ids);

  public CardInfoDto updateCardInfoById(CardInfoDto cardInfoData);

  public void deleteCardInfoById(long id);

}
