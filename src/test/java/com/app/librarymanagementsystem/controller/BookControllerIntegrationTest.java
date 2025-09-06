package com.app.librarymanagementsystem.controller;

import com.app.librarymanagementsystem.dto.BookDTO;
import com.app.librarymanagementsystem.exception.ResourceNotFoundException;
import com.app.librarymanagementsystem.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
@ActiveProfiles("test")
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private BookDTO testBookDTO;

    @BeforeEach
    void setUp() {
        testBookDTO = new BookDTO(1L, "1234567890", "Sample Book", "John Doe", null);
    }

    @Test
    void testRegisterBook() throws Exception {
        Mockito.when(bookService.registerBook(Mockito.any(BookDTO.class))).thenReturn(testBookDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBookDTO)));

        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testBookDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(testBookDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(testBookDTO.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDTO.getIsbn()));
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<BookDTO> bookList = Collections.singletonList(testBookDTO);
        Mockito.when(bookService.getAllBooks()).thenReturn(bookList);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/books"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(testBookDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(testBookDTO.getTitle()));
    }

    @Test
    void testGetBookById() throws Exception {
        Mockito.when(bookService.getBookById(1L)).thenReturn(Optional.ofNullable(testBookDTO));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{id}", 1L));

        result.andExpect(MockMvcResultMatchers.status().isOk());
    }}
