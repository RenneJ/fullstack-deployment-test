package com.frontbackstart.quizzer.repository;

import com.frontbackstart.quizzer.domain.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Sort;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer>{
    List<Quiz> findAll(Sort sort);
}

