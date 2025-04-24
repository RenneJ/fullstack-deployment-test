package com.frontbackstart.quizzer.domain;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Category{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer categoryId;

	private String name;

	private String description;

	@JsonIgnore
	@OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
	private List<Quiz> quizzes;

	@PreRemove
	private void setNull(){
		for(Quiz quiz : quizzes){
			quiz.setCategory(null);
		}
	};

	public Category(){
		super();
	}

	public Category(String name, String description){
		this.name = name;
		this.description = description;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Quiz> getQuizzes() {
		return quizzes;
	}

	public void setQuizzes(List<Quiz> quizzes) {
		this.quizzes = quizzes;
	}

	@Override
	public String toString() {
		return name;
	}

}
