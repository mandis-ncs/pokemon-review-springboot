package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PokemonRepositoryTests {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    @DisplayName("Should return saved pokemon")
    public  void savedPokemonOk() {

        //arrange
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        //act
        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        //assert
        Assertions.assertThat(savedPokemon).isNotNull();
        Assertions.assertThat(savedPokemon.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should return all saved pokemon")
    public  void getAllPokemonOk() {

        //arrange
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        Pokemon pokemon2 = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        //act
        pokemonRepository.save(pokemon);
        pokemonRepository.save(pokemon2);

        List<Pokemon> pokemonList = pokemonRepository.findAll();

        //assert
        Assertions.assertThat(pokemonList).isNotNull();
        Assertions.assertThat(pokemonList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should return pokemon by id")
    public  void findPokemonByIdOk() {

        //arrange
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        //act
        pokemonRepository.save(pokemon);
        Pokemon savedPokemon = pokemonRepository.findById(pokemon.getId()).get();

        //assert
        Assertions.assertThat(savedPokemon).isNotNull();
    }

    @Test
    @DisplayName("Should return pokemon by type")
    public  void findPokemonByTypeOk() {

        //arrange
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        //act
        pokemonRepository.save(pokemon);
        Pokemon savedPokemon = pokemonRepository.findByType(pokemon.getType()).get();

        //assert
        Assertions.assertThat(savedPokemon).isNotNull();
    }

    @Test
    @DisplayName("Should update and save pokemon")
    public  void updatePokemonOk() {

        //arrange
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        //act
        pokemonRepository.save(pokemon);
        Pokemon savedPokemon = pokemonRepository.findById(pokemon.getId()).get();
        savedPokemon.setType("Electric");
        savedPokemon.setName("Raichu");

        Pokemon updatedPokemon = pokemonRepository.save(savedPokemon);

        //assert
        Assertions.assertThat(savedPokemon.getName()).isNotNull();
        Assertions.assertThat(savedPokemon.getType()).isNotNull();

    }

    @Test
    @DisplayName("Should delete pokemon by id")
    public  void deletePokemonOk() {

        //arrange
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        //act
        pokemonRepository.save(pokemon);

        pokemonRepository.deleteById(pokemon.getId());
        Optional<Pokemon> savedPokemon = pokemonRepository.findById(pokemon.getId());

        //assert
        Assertions.assertThat(savedPokemon).isEmpty();
    }


}
