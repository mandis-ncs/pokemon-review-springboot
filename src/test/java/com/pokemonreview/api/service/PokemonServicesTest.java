package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.impl.PokemonServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonServicesTest {

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonServiceImpl pokemonService;

    @Test
    @DisplayName("Should create a pokemon and return the dto")
    public void createPokemonOk() {

        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        PokemonDto pokemonDto = PokemonDto.builder()
                .name("pikachu")
                .type("electric").build();

        //mocking the following code on Service:
        //Pokemon newPokemon = pokemonRepository.save(pokemon);
        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        PokemonDto saveddPokemon = pokemonService.createPokemon(pokemonDto);

        Assertions.assertThat(saveddPokemon).isNotNull();

    }

    @Test
    @DisplayName("Should get all pokemon and return the dto")
    public void getAllPokemonOk() {
        PokemonResponse pokemonReturn = Mockito.mock(PokemonResponse.class);
        Page<Pokemon> pokemons = Mockito.mock(Page.class);

        when(pokemonRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pokemons);

        PokemonResponse savePokemon = pokemonService.getAllPokemon(1, 10);

        Assertions.assertThat(savePokemon).isNotNull();

    }

    @Test
    @DisplayName("Should get pokemon by id")
    public void getPokemonByIdOk() {

        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));

        PokemonDto savedPokemon = pokemonService.getPokemonById(1);

        Assertions.assertThat(savedPokemon).isNotNull();

    }

    @Test
    @DisplayName("Should update and save a pokemon")
    public void updatePokemonOk() {

        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        PokemonDto pokemonDto = PokemonDto.builder()
                .name("pikachu")
                .type("electric").build();

        when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));

        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        PokemonDto savedPokemon = pokemonService.updatePokemon(pokemonDto, 1);

        Assertions.assertThat(savedPokemon).isNotNull();

    }

    @Test
    @DisplayName("Should delete pokemon by id")
    public void deletePokemonByIdOk() {

        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));

        assertAll(() -> pokemonService.deletePokemonId(1));
    }

}
