package com.innowise.userservice.integration;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.innowise.userservice.dto.CreateUserDto;
import com.innowise.userservice.dto.UserDto;
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
public class UserControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("Integrational user get by id mvc test")
  @WithMockUser
  public void getByIDTest() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/user?id={id}", -1)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.surname").value("testS1"));
    } catch (Exception e) {
      fail("Exception during user get by id mvc test " + e.getMessage());
    }
  }

  @Test
  @DisplayName("Integrational user get by email mvc test")
  @WithMockUser
  public void getByEmailTest() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/user/by-email")
              .param("email", "test2@mail.com")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.surname").value("testS2"))
          .andExpect(jsonPath("$.id").value(-2))
          .andExpect(jsonPath("$.email").value("test2@mail.com"));
    } catch (Exception e) {
      fail("Exception during user get by email mvc test " + e.getMessage());
    }
  }

  @Test
  @DisplayName("Integrational user create mvc test")
  @WithMockUser
  public void createTest() {
    try {
      CreateUserDto user = new CreateUserDto("testNew", "testNewS",1, "11-11-2222",
          "testNew1@mail.com");
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String userJSON = ow.writeValueAsString(user);
      mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/create")
              .with(csrf())
              .content(userJSON)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.surname").value("testNewS"))
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.email").value("testNew1@mail.com"));
    } catch (Exception e) {
      fail("Exception during user create mvc test " + e.getMessage());
    }
  }

  @Test
  @DisplayName("Integrational user update mvc test")
  @WithMockUser
  public void updateTest() {
    try {
      UserDto user = new UserDto(-3, null, "testNewS", null,
          "testNew@mail.com");
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String userJSON = ow.writeValueAsString(user);
      mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/update")
              .with(csrf())
              .content(userJSON)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.surname").value("testNewS"))
          .andExpect(jsonPath("$.id").value(-3))
          .andExpect(jsonPath("$.email").value("testNew@mail.com"));
    } catch (Exception e) {
      fail("Exception during user update mvc test " + e.getMessage());
    }
  }

  @Test
  @DisplayName("Integrational user delete mvc test")
  @WithMockUser
  public void deleteTest() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/delete")
              .with(csrf())
              .param("id", "-2")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().is(204));
    } catch (Exception e) {
      fail("Exception during user delete mvc test " + e.getMessage());
    }
  }

  @Test
  @DisplayName("Integrational user get by id list mvc test")
  @WithMockUser
  public void getByIdsTest() {
    try {
      List<Long> ids = List.of(-1L, -3L, -10L);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String idsJSON = ow.writeValueAsString(ids);
      mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/search")
              .with(csrf())
              .content(idsJSON)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.[0].id").value(-1))
          .andExpect(jsonPath("$.[1].id").value(-3));
    } catch (Exception e) {
      fail("Exception during user get by id mvc test " + e.getMessage());
    }
  }
}
