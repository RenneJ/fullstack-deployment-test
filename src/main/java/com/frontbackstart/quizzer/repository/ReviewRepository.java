package com.frontbackstart.quizzer.repository;

import com.frontbackstart.quizzer.domain.Review;
import com.frontbackstart.quizzer.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByQuiz(Quiz quiz);
}
