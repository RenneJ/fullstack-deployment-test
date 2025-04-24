package com.frontbackstart.quizzer.web;

import com.frontbackstart.quizzer.domain.Category;
import com.frontbackstart.quizzer.domain.Quiz;
import com.frontbackstart.quizzer.repository.CategoryRepository;
import com.frontbackstart.quizzer.repository.QuizRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Tag(name = "Category", description = "Operations for accessing and managing categories")
public class CategoryRestController{
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
    private QuizRepository quizRepository;

    @Operation(
    summary = "Get all categories",
    description = "Returns all categories"
)
@ApiResponses(value = {
    // The responseCode property defines the HTTP status code of the response
    @ApiResponse(responseCode = "200", description = "Successful operation"),
    @ApiResponse(responseCode = "404", description = "Categories not found")
})

	@GetMapping("/categories")
	public List<Category> getCategories(){
		List<Category> categories = categoryRepository.findAll();
		return categories;
	}

    @Operation(
    summary = "Get quizzes by category id",
    description = "Returns all quizzes with the provided category id"
)
@ApiResponses(value = {
    // The responseCode property defines the HTTP status code of the response
    @ApiResponse(responseCode = "200", description = "Successful operation"),
    @ApiResponse(responseCode = "404", description = "Category with the provided id not found")
})

	@GetMapping("/categories/{categoryId}")
    public List<Quiz> getPublishedQuizzesByCategory(@PathVariable Integer categoryId) {
        // Hakee kategorian
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + categoryId + " not found"));
        
        // Suodattaa julkaistut quizit, jotka kuuluvat tähän kategoriaan
        return quizRepository.findAll().stream()
            .filter(quiz -> quiz.getCategory() != null && quiz.getCategory().equals(category) && quiz.getPublished())
            .collect(Collectors.toList());
    }
}
