package com.pokemonreview.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.controllers.ReviewController;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.service.ReviewService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pokemon pokemon;
    private Review review;
    private PokemonDto pokemonDto;
    private ReviewDto reviewDto;

    @BeforeEach
    public void init() {
        pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        pokemonDto = PokemonDto.builder()
                .name("pikachu")
                .type("electric").build();

        review = Review.builder().title("title")
                .content("content")
                .stars(5).build();

        reviewDto = ReviewDto.builder().title("title")
                .content("content")
                .stars(5).build();

    }

    @Test
    @DisplayName("Should get a pokemon and return the dto")
    public void getPokemon() throws Exception { //Exception refers to writeValueAsString and andExpect

        int pokemonId = 1;
        when(reviewService.getReviewsByPokemonId(pokemonId)).thenReturn(Arrays.asList(reviewDto));

        ResultActions response = mockMvc.perform(get("/api/pokemon/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(Arrays.asList(reviewDto).size())));
    }

    @Test
    @DisplayName("Should update a review and return the dto")
    public void updateReview() throws Exception {

        int pokemonId = 1;
        int reviewId = 1;
        when(reviewService.updateReview(pokemonId, reviewId, reviewDto)).thenReturn(reviewDto);

        ResultActions response = mockMvc.perform(put("/api/pokemon/1/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(reviewDto.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars", CoreMatchers.is(reviewDto.getStars())));
    }


    @Test
    @DisplayName("Should create a review and return the created")
    public void createReview() throws Exception {

        int pokemonId = 1;
        when(reviewService.createReview(pokemonId, reviewDto)).thenReturn(reviewDto);

        ResultActions response = mockMvc.perform(post("/api/pokemon/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(reviewDto.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars", CoreMatchers.is(reviewDto.getStars())));
    }

    @Test
    @DisplayName("Should get a review by id and return the dto")
    public void getReviewById() throws Exception { //Exception refers to writeValueAsString and andExpect

        int pokemonId = 1;
        int reviewId = 1;
        when(reviewService.getReviewById(reviewId, pokemonId)).thenReturn(reviewDto);

        ResultActions response = mockMvc.perform(get("/api/pokemon/1/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(reviewDto.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars", CoreMatchers.is(reviewDto.getStars())));
    }

    @Test
    @DisplayName("Should delete a review")
    public void deleteReview() throws Exception { //Exception refers to writeValueAsString and andExpect

        int pokemonId = 1;
        int reviewId = 1;

        doNothing().when(reviewService).deleteReview(pokemonId, reviewId);

        ResultActions response = mockMvc.perform(delete("/api/pokemon/1/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

}
