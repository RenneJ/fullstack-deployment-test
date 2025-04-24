package com.frontbackstart.quizzer.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.frontbackstart.quizzer.domain.Question;
import com.frontbackstart.quizzer.domain.Quiz;

import com.frontbackstart.quizzer.repository.QuestionRepository;
import com.frontbackstart.quizzer.repository.QuizRepository;

import java.util.List;

@Controller
public class QuestionController {


    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    private String[] difficulties = {"Easy", "Medium", "Hard"};

    @GetMapping("/quizzes/{quizId}")
    public String getQuestionsForQuiz(@PathVariable Integer quizId, Model model) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", quiz.getQuestions());
        return "questions";
    }

    @GetMapping("/quizzes/{quizId}/addquestion")
    public String addQuestion(@PathVariable Integer quizId, Model model) {
    	//model.addAttribute("quiz", quizRepository.findById(quizId));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(); //Added to pass the quiz name to the page
        model.addAttribute("quiz", quiz); //Added to pass the quiz name to the page
        List<Question> question = questionRepository.findByQuiz(quiz); //Added to pass the questions to page
        model.addAttribute("questions", question); //Added to pass the questions to page
     	model.addAttribute("difficulties", difficulties);
        model.addAttribute("question", new Question());
        model.addAttribute("quizzes", quizRepository.findAll(Sort.by(Sort.Order.desc("created"))));
        //model.addAttribute("quizzes", quizRepository.findAll());
        return "addquestion";
    }

    @PostMapping("/quizzes/{quizId}/savequestion")
    public String saveQuestion(@PathVariable Quiz quizId, @ModelAttribute("question") Question question) {
    	question.setQuiz(quizId);
        questionRepository.save(question);
        return "redirect:/questions/" + question.getQuestionId() + "/addanswer";
    }

    @GetMapping("/quizzes/{quizId}/editquestion/{questionId}")
    public String editQuestion(@PathVariable("quizId") Integer quizId, @PathVariable("questionId") Integer questionId, Model model){
    	model.addAttribute("difficulties", difficulties);
    	model.addAttribute("question", questionRepository.findById(questionId).orElseThrow());
    	return "editquestion";
    }

    @GetMapping("/quizzes/{quizId}/deletequestion/{questionId}")
	public String deleteQuestion(@PathVariable("quizId") Integer quizId, @PathVariable("questionId") Integer questionId, Model model){
		// save quizId to variable...
		//Question question = questionRepository.findById(questionId).orElseThrow();
		// ...before deleting the question
		questionRepository.deleteById(questionId);
		return "redirect:/quizzes/" + quizId + "/addquestion";
	}
}
