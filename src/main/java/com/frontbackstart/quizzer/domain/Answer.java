package com.frontbackstart.quizzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Answer{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer answerId;

	@JsonIgnore
	@ManyToOne
	private Question question;

	private String answerText;

	@JsonIgnore
	private Boolean isRight;

	public Answer(){
		super();
	}
	public Answer(Question question, String answerText, Boolean isRight){
		this.question = question;
		this.answerText = answerText;
		this.isRight = isRight;
	}

	public Integer getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public Boolean getIsRight() {
		return isRight;
	}

	public void setIsRight(Boolean isRight) {
		this.isRight = isRight;
	}


}
