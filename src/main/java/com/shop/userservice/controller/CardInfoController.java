package com.innowise.userservice.controller;

import com.innowise.userservice.dto.CardInfoDto;
import com.innowise.userservice.dto.CreateCardInfoDto;
import com.innowise.userservice.service.CardInfoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "user_security")
@RestController
@RequestMapping("/api/v1/cards/")
@RequiredArgsConstructor
public class CardInfoController {

  private final CardInfoService cardInfoService;

  @PostMapping("create")
  ResponseEntity<CardInfoDto> createCardInfo(@Valid @RequestBody CreateCardInfoDto data) {
    CardInfoDto cardInfo = cardInfoService.createCardInfo(data);
    return new ResponseEntity<>(cardInfo, HttpStatus.CREATED);
  }

  @PatchMapping("update")
  ResponseEntity<CardInfoDto> updateUser(@Valid @RequestBody CardInfoDto data) {
    CardInfoDto cardInfo = cardInfoService.updateCardInfoById(data);
    return new ResponseEntity<>(cardInfo, HttpStatus.OK);
  }

  @GetMapping("card")
  ResponseEntity<CardInfoDto> getById(@RequestParam(name = "id") Long id) {
    CardInfoDto cardInfoById = cardInfoService.getCardInfoById(id);
    return new ResponseEntity<>(cardInfoById, HttpStatus.OK);
  }

  @PostMapping("search")
  ResponseEntity<List<CardInfoDto>> getByIds(@RequestBody List<Long> ids) {
    List<CardInfoDto> cards = cardInfoService.getCardInfoByIds(ids);
    return new ResponseEntity<>(cards, HttpStatus.OK);
  }

  @DeleteMapping("delete")
  ResponseEntity<?> deleteById(@RequestParam(name = "id") Long id) {
    cardInfoService.deleteCardInfoById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
