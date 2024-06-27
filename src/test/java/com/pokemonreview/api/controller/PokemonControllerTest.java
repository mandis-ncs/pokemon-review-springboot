package com.pokemonreview.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.controllers.PokemonController;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.service.PokemonService;
import org.checkerframework.checker.units.qual.A;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

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
    @DisplayName("Should create a pokemon and return the created")
    public void createPokemon() throws Exception { //Exception refers to writeValueAsString and andExpect

        given(pokemonService.createPokemon(ArgumentMatchers.any()))
                .willAnswer((invocation -> invocation.getArgument(0) //index = 0 because only have one argument
        ));

        ResultActions response = mockMvc.perform(post("/api/pokemon/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(pokemonDto.getType())));
    }

    @Test
    @DisplayName("Should get All pokemon and return a list of dtos")
    public void getAllPokemon() throws Exception {

        PokemonResponse responseDto = PokemonResponse.builder()
                        .pageSize(10)
                        .last(true)
                        .pageNo(1)
                        .content(Arrays.asList(pokemonDto)).build();

        when(pokemonService.getAllPokemon(1, 10)).thenReturn(responseDto);

        ResultActions response = mockMvc.perform(get("/api/pokemon")
                        .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()", CoreMatchers.is(responseDto.getContent().size())));
    }

    @Test
    @DisplayName("Should get a pokemon and return the dto")
    public void getPokemon() throws Exception { //Exception refers to writeValueAsString and andExpect

        int pokemonId = 1;
        when(pokemonService.getPokemonById(pokemonId)).thenReturn(pokemonDto);

        ResultActions response = mockMvc.perform(get("/api/pokemon/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(pokemonDto.getType())));
    }


    @Test
    @DisplayName("Should update a pokemon and return the dto")
    public void updatePokemon() throws Exception { //Exception refers to writeValueAsString and andExpect

        int pokemonId = 1;
        when(pokemonService.updatePokemon(pokemonDto, pokemonId)).thenReturn(pokemonDto);

        ResultActions response = mockMvc.perform(put("/api/pokemon/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(pokemonDto.getType())));
    }


    @Test
    @DisplayName("Should delete a pokemon")
    public void deletePokemon() throws Exception { //Exception refers to writeValueAsString and andExpect

        int pokemonId = 1;
        doNothing().when(pokemonService).deletePokemonId(pokemonId);

        ResultActions response = mockMvc.perform(delete("/api/pokemon/1//delete")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }



}
