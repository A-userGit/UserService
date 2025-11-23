package com.innowise.userservice.integration;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.innowise.userservice.dto.CardInfoDto;
import com.innowise.userservice.dto.CreateCardInfoDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
public class CardInfoControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("Integrational cardInfo get by id mvc test")
  @WithMockUser
  public void getByIDTest() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cards/card?id={id}", -1)
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
          .andExpect(jsonPath("$.holder").value("test1"));
    } catch (Exception e) {
      fail("Exception during cardInfo get by id mvc test " + e.getMessage());
    }
  }

  @Test
  @DisplayName("Integrational cardInfo create mvc test")
  @WithMockUser
  public void createTest() {
    try {
      CreateCardInfoDto cardInfoDto = new CreateCardInfoDto(-1, "4242 4242 4242 4242", "TestH1",
          "11-11-2011");
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String cardInfoJSON = ow.writeValueAsString(cardInfoDto);
      mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cards/create").content(cardInfoJSON)
              .with(csrf())
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
          .andExpect(jsonPath("$.holder").value("TestH1")).andExpect(jsonPath("$.id").value(1));
    } catch (Exception e) {
      fail("Exception during cardInfo create mvc test " + e.getMessage());
    }
  }

  @Test
  @DisplayName("Integrational cardInfo update mvc test")
  @WithMockUser
  public void updateTest() {
    try {
      CardInfoDto cardInfoDto = new CardInfoDto(-3, -1, "4242 4242 4242 4242", "TestH1New",
          "11-11-2011");
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String cardInfoJSON = ow.writeValueAsString(cardInfoDto);
      mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/cards/update").content(cardInfoJSON)
              .with(csrf())
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
          .andExpect(jsonPath("$.holder").value("TestH1New"));
    } catch (Exception e) {
      fail("Exception during user update mvc test " + e.getMessage());
    }
  }

  @Test
  @DisplayName("Integrational card delete mvc test")
  @WithMockUser
  public void deleteTest() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cards/delete").param("id", "-4")
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(204));
    } catch (Exception e) {
      fail("Exception during card delete mvc test " + e.getMessage());
    }
  }

  @Test
  @DisplayName("Integrational cardInfo get by id list mvc test")
  @WithMockUser
  public void getByIdsTest() {
    try {
      List<Long> ids = List.of(-1L, -3L, -10L);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String idsJSON = ow.writeValueAsString(ids);
      mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cards/search").content(idsJSON)
              .with(csrf())
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
          .andExpect(jsonPath("$.[0].id").value(-1)).andExpect(jsonPath("$.[1].id").value(-3));
    } catch (Exception e) {
      fail("Exception during card get by ids mvc test " + e.getMessage());
    }
  }
}
