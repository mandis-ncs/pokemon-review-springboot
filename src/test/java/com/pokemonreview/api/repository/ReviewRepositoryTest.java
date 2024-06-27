package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import org.assertj.core.api.Assertions;
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
class ReviewRepositoryTest {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewRepositoryTest(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Test
    @DisplayName("Should save and return review")
    public void saveReviewOk() {

        Review review = Review.builder().title("title")
                .content("content")
                .stars(5).build();

        Review savedReview = reviewRepository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should return more than one review")
    public void getAllReview() {

        Review review = Review.builder().title("title")
                .content("content")
                .stars(5).build();

        Review review2 = Review.builder().title("title")
                .content("content")
                .stars(5).build();

        reviewRepository.save(review);
        reviewRepository.save(review2);

        List<Review> reviewList = reviewRepository.findAll();

        Assertions.assertThat(reviewList).isNotNull();
        Assertions.assertThat(reviewList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should get review by id")
    public void getReviewByIdOk() {

        Review review = Review.builder().title("title")
                .content("content")
                .stars(5).build();

        Review savedReview = reviewRepository.save(review);

        Review reviewReturn = reviewRepository.findById(review.getId()).get();

        Assertions.assertThat(reviewReturn).isNotNull();
    }

    @Test
    @DisplayName("Should update and save review")
    public void updateReviewOk() {

        Review review = Review.builder().title("title")
                .content("content")
                .stars(5).build();

        reviewRepository.save(review);

        Review reviewSave = reviewRepository.findById(review.getId()).get();
        reviewSave.setTitle("title2");
        reviewSave.setContent("content2");
        Review updatedPokemon = reviewRepository.save(reviewSave);

        Assertions.assertThat(updatedPokemon.getTitle()).isNotNull();
        Assertions.assertThat(updatedPokemon.getContent()).isNotNull();
    }

    @Test
    @DisplayName("Should delete review by id")
    public  void deleteReviewOk() {

        Review review = Review.builder().title("title")
                .content("content")
                .stars(5).build();

        reviewRepository.save(review);

        reviewRepository.deleteById(review.getId());
        Optional<Review> reviewReturn = reviewRepository.findById(review.getId());

        //assert
        Assertions.assertThat(reviewReturn).isEmpty();
    }

}