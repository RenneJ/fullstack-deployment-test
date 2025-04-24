package com.frontbackstart.quizzer.web;

import com.frontbackstart.quizzer.domain.Category;
import com.frontbackstart.quizzer.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import java.util.List;
@Controller
public class CategoryController{
	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/categories")
	public String getCategories(Model model){
		model.addAttribute("categories", categoryRepository.findAll());
		return "categories";
	}

	@GetMapping("/addcategory")
	public String addCategory(Model model){
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("category", new Category());
		return "addcategory";
	}
	

	@PostMapping("/savecategory")
    public String saveCategory(Category category) {
        categoryRepository.save(category); // Tallentaa kategorian
        return "redirect:/categories"; // Ohjaa takaisin kategorioiden listaukseen
    }

	@GetMapping("/deletecategory/{categoryId}")
	public String deleteCategory(@PathVariable("categoryId") Integer categoryId, Model model){
		categoryRepository.deleteById(categoryId);
		return "redirect:/categories";
	}
};
