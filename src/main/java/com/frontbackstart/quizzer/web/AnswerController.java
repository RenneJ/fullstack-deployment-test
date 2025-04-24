package com.frontbackstart.quizzer.web;

import com.frontbackstart.quizzer.domain.Answer;
import com.frontbackstart.quizzer.domain.Question;
import com.frontbackstart.quizzer.repository.AnswerRepository;
import com.frontbackstart.quizzer.repository.QuestionRepository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



import java.util.List;

@Controller
public class AnswerController{
	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionRepository questionRepository;


	@GetMapping("/questions/{questionId}")
    public String getAnswersForQuestion(@PathVariable Integer questionId, Model model) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        model.addAttribute("question", question);
        model.addAttribute("answers", question.getAnswers());
        return "answers";
    }

	@GetMapping("/questions/{questionId}/addanswer")
    public String addAnswer(@PathVariable Integer questionId, Model model) {
        model.addAttribute("answer", new Answer());
        // Fetch the Question object using the questionId
        Question question = questionRepository.findById(questionId).orElseThrow();
        // Pass the Question object to the model
        model.addAttribute("question", question);
        List<Answer> answer = answerRepository.findByQuestion(question); //Added to pass the questions to page
        model.addAttribute("answers", answer); //Added to pass the questions to page
        return "addanswer";
    }

	@PostMapping("/questions/{questionId}/saveanswer")
    public String saveAnswer(@PathVariable Question questionId, @ModelAttribute("answer") Answer answer) {
        answer.setQuestion(questionId);
        answerRepository.save(answer);
        return "redirect:/questions/{questionId}/addanswer";

    }

    @GetMapping("/questions/{questionId}/editanswer/{answerId}")
    public String editAnswer(@PathVariable("questionId") Integer questionId, @PathVariable("answerId") Integer answerId, Model model){
    	model.addAttribute("answer", answerRepository.findById(answerId).orElseThrow());
    	return "editanswer";
    }

    @GetMapping("/questions/{questionId}/deleteanswer/{answerId}")
	public String deleteAnswer(@PathVariable("questionId") Integer questionId, @PathVariable("answerId") Integer answerId, Model model){
		// save questionId to variable...
		//Answer answer = answerRepository.findById(answerId).orElseThrow();
		//Integer questionId = answer.getQuestion().getQuestionId();
		// ...before deleting the answer
		answerRepository.deleteById(answerId);
		return "redirect:/questions/{questionId}/addanswer";
	}
}
