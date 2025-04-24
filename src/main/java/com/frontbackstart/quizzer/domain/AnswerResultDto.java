package com.frontbackstart.quizzer.domain;

public class AnswerResultDto{
private boolean isCorrect;

public AnswerResultDto(){
	super();
}

public AnswerResultDto(boolean isCorrect){
	this.isCorrect = isCorrect;
}

public boolean isCorrect() {
	return isCorrect;
}

public void setCorrect(boolean isCorrect) {
	this.isCorrect = isCorrect;
}
}
