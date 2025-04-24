package com.frontbackstart.quizzer.repository;

import com.frontbackstart.quizzer.domain.Category;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Sort;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
    List<Category> findAll(Sort sort);
}
