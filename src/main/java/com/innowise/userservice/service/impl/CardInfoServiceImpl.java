package com.innowise.userservice.service.impl;

import com.innowise.userservice.aop.MultiCacheable;
import com.innowise.userservice.dto.CardInfoDto;
import com.innowise.userservice.entity.CardInfo;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.exception.ObjectNotFoundException;
import com.innowise.userservice.mapper.CardInfoMapper;
import com.innowise.userservice.repository.CardInfoRepository;
import com.innowise.userservice.service.CardInfoService;
import com.innowise.userservice.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardInfoServiceImpl implements CardInfoService {

  private final CardInfoRepository cardInfoRepository;
  private final UserService userService;
  private final CardInfoMapper mapper;

  @Override
  @Transactional
  @CachePut(value = "cards", key = "#cardData.id")
  public CardInfoDto createCardInfo(CardInfoDto cardData) {
    CardInfo cardInfo = mapper.toCardInfo(cardData);
    User user = userService.getDBUserById(cardInfo.getUser().getId());
    cardInfo.setUser(user);
    return mapper.toCardInfoDto(cardInfoRepository.save(cardInfo));
  }

  @Override
  @Cacheable(value = "cards", key = "#id", unless = "#result==null")
  public CardInfoDto getCardInfoById(long id) {
    Optional<CardInfo> byId = cardInfoRepository.findById(id);
    if (byId.isEmpty()) {
      throw ObjectNotFoundException.entityNotFound(CardInfo.class, "id", id);
    }
    return mapper.toCardInfoDto(byId.get());
  }

  @Override
  @MultiCacheable(value = "cards", keysParamName = "ids", keyName = "id")
  public List<CardInfoDto> getCardInfoByIds(List<Long> ids) {
    return cardInfoRepository.findAllById(ids).stream().map(mapper::toCardInfoDto).toList();
  }

  @Override
  @CachePut(value = "cards", key = "#cardInfoData.id")
  public CardInfoDto updateCardInfoById(CardInfoDto cardInfoData) {
    checkIfExists(cardInfoData.getId());
    return mapper.toCardInfoDto(cardInfoRepository.save(mapper.toCardInfo(cardInfoData)));
  }

  @Override
  @CacheEvict(value = "cards", key = "#id")
  public void deleteCardInfoById(long id) {
    checkIfExists(id);
    cardInfoRepository.deleteById(id);
  }

  private void checkIfExists(long id) {
    boolean exists = cardInfoRepository.existsById(id);
    if (!exists) {
      throw ObjectNotFoundException.entityNotFound(CardInfo.class, "id", id);
    }
  }
}
