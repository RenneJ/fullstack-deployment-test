package com.frontbackstart.quizzer.web;

import com.frontbackstart.quizzer.domain.Quiz;
import com.frontbackstart.quizzer.domain.Answer;
import com.frontbackstart.quizzer.domain.AnswerResultDto;
import com.frontbackstart.quizzer.domain.Question;
import com.frontbackstart.quizzer.repository.AnswerRepository;
import com.frontbackstart.quizzer.repository.QuestionRepository;
import com.frontbackstart.quizzer.repository.QuizRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Tag(name = "Quiz",
description = "Operations for listing all quizzes, answering quizzes and browsing quiz results")
public class QuizRestController{
	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

     @Operation(
    summary = "Get all published quizzes",
    description = "Returns all published quizzes"
)
@ApiResponses(value = {
    // The responseCode property defines the HTTP status code of the response
    @ApiResponse(responseCode = "200", description = "Successful operation"),
    @ApiResponse(responseCode = "404", description = "Quizzes not found")
})

	@GetMapping("/quizzes")
	public List<Quiz> getQuizzes(){
		List<Quiz> quizzes = quizRepository.findAll(Sort.by(Sort.Order.desc("created")));
		ArrayList<Quiz> publishedQuizzes = new ArrayList<Quiz>();
		for (Quiz quiz : quizzes){
			if (quiz.getPublished() == true){
				publishedQuizzes.add(quiz);
			}
		}
		return publishedQuizzes;
	}

    @Operation(
        summary = "Get published quiz by id",
        description = "Returns published quizzes with the provided id"
    )
    @ApiResponses(value = {
        // The responseCode property defines the HTTP status code of the response
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404", description = "Quiz with the provided id not found or published")
    })

	@GetMapping("/quizzes/{quizId}")
    public Map<String, Object> getQuestionsForQuiz(@PathVariable Integer quizId) {
        // Hakee quizin, joka on julkaistu
        Quiz quiz = quizRepository.findById(quizId)
                .filter(Quiz::getPublished) // Tarkistaa, että quiz on julkaistu
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id " + quizId + " not found or published"));

        // Hakee kaikki kysymykset, jotka liittyvät tähän quiziin
        List<Question> questions = questionRepository.findByQuiz(quiz);
        //
        //List<Answer> answers =

        // Palauttaa kaikki quizin tiedot sekä kysymykset ja niiden määrän
        return Map.of(
            "quizId", quiz.getQuizId(),
			"category", quiz.getCategory().getName(),
            "name", quiz.getName(),
            "description", quiz.getDescription(),
            "published", quiz.getPublished(),
            "created", quiz.getCreated(),
            "questions", questions,
            "questionCount", questions.size()
        );
    }

    @Operation(
        summary = "Receive submitted answer option",
        description = "Receives submitted anwser by id and sends response if the anwer is right or wrong"
    )
    @ApiResponses(value = {
        // The responseCode property defines the HTTP status code of the response
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404", description = "Question or answer with the provided id not found ")
    })

   	@PostMapping("/answers/{questionId}")
	public AnswerResultDto submitAnswer(@PathVariable Integer questionId, @RequestBody Map<String, Object> answerData ){
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));

        Quiz quiz = question.getQuiz();
        if(!quiz.getPublished()){
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }

            Integer selectedAnswerId = (Integer) answerData.get("answerId");

             Answer selectedAnswer = answerRepository.findById(selectedAnswerId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found"));

            question.setTotalAnswers(question.getTotalAnswers() + 1);
            if (selectedAnswer.getIsRight()) {
                question.setTotalRightAnswers(question.getTotalRightAnswers() + 1);
                questionRepository.save(question);
                AnswerResultDto result = new AnswerResultDto(true);
                return result;
            } else {
                questionRepository.save(question);
                AnswerResultDto result = new AnswerResultDto(false);
                return result;
            }
}

@Operation(
    summary = "Get results for a quiz",
    description = "Returns total answers and total right answers for a quiz"
)
@ApiResponses(value = {
    // The responseCode property defines the HTTP status code of the response
    @ApiResponse(responseCode = "200", description = "Successful operation"),
    @ApiResponse(responseCode = "404", description = "Quiz with the provided id not found")
})

@GetMapping("/seeresults/{quizId}")
public Map<String, Object> getQuizResults(@PathVariable Integer quizId) {

    Quiz quiz = quizRepository.findById(quizId)
            .filter(Quiz::getPublished)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found or published"));


    List<Question> questions = questionRepository.findByQuiz(quiz);


    int totalAnswers = questions.stream().mapToInt(Question::getTotalAnswers).sum();
    int totalRightAnswers = questions.stream().mapToInt(Question::getTotalRightAnswers).sum();


    List<Map<String, Object>> questionDetails = questions.stream()
            .map(question -> {
                Map<String, Object> questionMap = Map.of(
                        "questionText", question.getQuestionText(),
                        "difficulty", question.getDifficulty(),
                        "totalAnswers", question.getTotalAnswers(),
                        "totalRightAnswers", question.getTotalRightAnswers()
                );
                return questionMap;
            })
            .toList();


    return Map.of(
            "quizName", quiz.getName(),
            "questionCount", questions.size(),
            "questions", questionDetails,
            "totalAnswers", totalAnswers,
            "totalRightAnswers", totalRightAnswers
    );
}
}
