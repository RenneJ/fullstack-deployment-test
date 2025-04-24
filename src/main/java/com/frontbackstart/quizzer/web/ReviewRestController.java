package com.frontbackstart.quizzer.web;

import com.frontbackstart.quizzer.domain.Quiz;
import com.frontbackstart.quizzer.domain.Review;
import com.frontbackstart.quizzer.repository.QuizRepository;
import com.frontbackstart.quizzer.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ReviewRestController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private QuizRepository quizRepository;

    // Lisää arvostelu
    @PostMapping("/quizzes/{quizId}/reviews")
    public Review addReview(@PathVariable Integer quizId, @RequestBody Map<String, Object> reviewData) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));

        String nickname = (String) reviewData.get("nickname");
        Integer rating = (Integer) reviewData.get("rating");
        String reviewText = (String) reviewData.get("reviewText");

        if (rating < 1 || rating > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        Review review = new Review(quiz, nickname, rating, reviewText);
        return reviewRepository.save(review);
    }

    // Hae kaikki arvostelut tietylle quizille
    @GetMapping("/quizzes/{quizId}/reviews")
    public Map<String, Object> getReviews(@PathVariable Integer quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .filter(Quiz::getPublished)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found or published"));

        List<Review> reviews = reviewRepository.findByQuiz(quiz);

        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        return Map.of(
                "quizName", quiz.getName(),
                "averageRating", averageRating,
                "reviews", reviews.stream().map(review -> Map.of(
                        "reviewId", review.getReviewId(),
                        "nickname", review.getNickname(),
                        "rating", review.getRating(),
                        "reviewText", review.getReviewText(),
                        "createdAt", review.getCreatedAt().format(DateTimeFormatter.ISO_DATE)
                )).toList()
        );
    }

    // Muokkaa arvostelua
    @PutMapping("/reviews/{reviewId}")
    public Review editReview(@PathVariable Integer reviewId, @RequestBody Map<String, Object> reviewData) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        String nickname = (String) reviewData.get("nickname");
        Integer rating = (Integer) reviewData.get("rating");
        String reviewText = (String) reviewData.get("reviewText");

        if (rating < 1 || rating > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        review.setNickname(nickname);
        review.setRating(rating);
        review.setReviewText(reviewText);

        return reviewRepository.save(review);
    }

    // Poista arvostelu
    @DeleteMapping("/reviews/{reviewId}")
    public void deleteReview(@PathVariable Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        reviewRepository.delete(review);
    }
}