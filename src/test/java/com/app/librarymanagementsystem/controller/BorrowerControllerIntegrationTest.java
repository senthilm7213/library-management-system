package com.app.librarymanagementsystem.controller;

import com.app.librarymanagementsystem.dto.BorrowerDTO;
import com.app.librarymanagementsystem.exception.ResourceNotFoundException;
import com.app.librarymanagementsystem.service.BorrowerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BorrowerController.class)
class BorrowerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BorrowerService borrowerService;

    private BorrowerDTO testBorrowerDTO;

    @BeforeEach
    void setUp() {
        testBorrowerDTO = new BorrowerDTO(1L, "janedoe@example.com", "Jane Doe");
    }

    @Test
    void testRegisterBorrower() throws Exception {
        Mockito.when(borrowerService.registerBorrower(Mockito.any(BorrowerDTO.class)))
                .thenReturn(testBorrowerDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/borrowers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBorrowerDTO)));

        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testBorrowerDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testBorrowerDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(testBorrowerDTO.getEmail()));
    }
}