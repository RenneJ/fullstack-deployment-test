package com.frontbackstart.quizzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontbackstart.quizzer.domain.Answer;
import com.frontbackstart.quizzer.domain.Category;
import com.frontbackstart.quizzer.domain.Question;
import com.frontbackstart.quizzer.domain.Quiz;
import com.frontbackstart.quizzer.repository.AnswerRepository;
import com.frontbackstart.quizzer.repository.CategoryRepository;
import com.frontbackstart.quizzer.repository.QuestionRepository;
import com.frontbackstart.quizzer.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class QuizRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Tyhjennetään tietokanta ennen jokaista testiä
        quizRepository.deleteAll();
        questionRepository.deleteAll();
        answerRepository.deleteAll();
    }
    @Test
    public void getAllQuizzesReturnsEmptyListWhenNoQuizzesExist() throws Exception {
        // Suoritetaan GET-pyyntö
        mockMvc.perform(get("/api/quizzes"))
                // Odotetaan, että HTTP-status on OK (200)
                .andExpect(status().isOk())
                // Odotetaan, että JSON-lista on tyhjä
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getAllQuizzesReturnsListOfQuizzesWhenPublishedQuizzesExist() throws Exception {
        // Luodaan ja tallennetaan julkaistuja quizzeja
        Quiz quiz1 = new Quiz(null, "Dinosaurukset", "First quiz description", true, LocalDateTime.now());
        Quiz quiz2 = new Quiz(null, "Maanosat", "Second quiz description", true, LocalDateTime.now());

        quizRepository.saveAll(List.of(quiz1, quiz2));

        // Suoritetaan GET-pyyntö
        mockMvc.perform(get("/api/quizzes"))
                // Odotetaan, että HTTP-status on OK (200)
                .andExpect(status().isOk())
                // Odotetaan, että JSON-listassa on kaksi quizia
                .andExpect(jsonPath("$", hasSize(2)))
                // Odotetaan, että ensimmäisen quizin nimi on "Maanosat"
                .andExpect(jsonPath("$[0].name").value("Maanosat"))
                // Odotetaan, että toisen quizin nimi on "Dinosaurukset". Tämä on näin, koska meidän ohjelmassa viimeisimmäksi luotu quiz sortataan listan esnimmäiseksi"
                .andExpect(jsonPath("$[1].name").value("Dinosaurukset"));
    }

    @Test
public void getAllQuizzesDoesNotReturnUnpublishedQuizzes() throws Exception {
    // Luodaan ja tallennetaan quizzeja: osa julkaistuja, osa julkaisemattomia
    Quiz publishedQuiz1 = new Quiz(null, "Dinosaurukset", "First published quiz", true, LocalDateTime.now());
    Quiz publishedQuiz2 = new Quiz(null, "Maanosat", "Second published quiz", true, LocalDateTime.now());
    Quiz unpublishedQuiz1 = new Quiz(null, "UnPublished 1", "First unpublished quiz", false, LocalDateTime.now());
    Quiz unpublishedQuiz2 = new Quiz(null, "UnPublished 2", "Second unpublished quiz", false, LocalDateTime.now());

    quizRepository.saveAll(List.of(publishedQuiz1, publishedQuiz2, unpublishedQuiz1, unpublishedQuiz2));

    // Suoritetaan GET-pyyntö
    mockMvc.perform(get("/api/quizzes"))
            // Odotetaan, että HTTP-status on OK (200)
            .andExpect(status().isOk())
            // Odotetaan, että JSON-listassa on kaksi quizia (vain julkaistut)
            .andExpect(jsonPath("$", hasSize(2)))
            // Odotetaan, että ensimmäisen julkaistun quizin nimi on "Published Quiz 1"
            .andExpect(jsonPath("$[0].name").value("Maanosat"))
            // Odotetaan, että toisen julkaistun quizin nimi on "Published Quiz 2"
            .andExpect(jsonPath("$[1].name").value("Dinosaurukset"));
}

@Test
public void getQuestionsByQuizIdReturnsEmptyListWhenQuizDoesNotHaveQuestions() throws Exception {
    //Set a category, because the API Endpoint does not get quizzes with category:null
    Category category = new Category("General", "Category for undescripted quizzes");
categoryRepository.save(category);
    
    Quiz quizWithoutQuestions = new Quiz(category, "Quiz Without Questions", "This quiz has no questions", true, LocalDateTime.now());
    quizRepository.save(quizWithoutQuestions);

    
    mockMvc.perform(get("/api/quizzes/" + quizWithoutQuestions.getQuizId()))
            .andExpect(status().isOk()) // Odotetaan, että HTTP-status on OK
            .andExpect(jsonPath("$.questions", hasSize(0))) // Tarkistetaan, että "questions" on tyhjä lista
            .andExpect(jsonPath("$.quizId").value(quizWithoutQuestions.getQuizId())) // Tarkistetaan, että quizId vastaa tallennettua quizia
            .andExpect(jsonPath("$.name").value("Quiz Without Questions")) // Tarkistetaan quizin nimi
            .andExpect(jsonPath("$.description").value("This quiz has no questions")); // Tarkistetaan quizin kuvaus
}

@Test
public void getQuestionsByQuizIdReturnsListOfQuestionsWhenQuizHasQuestions() throws Exception {
    
    Category category = new Category("General", "Category for undescripted quizzes");
    categoryRepository.save(category);

    Quiz quiz = new Quiz(category, "Quiz with Questions", "This quiz has questions and answers", true, LocalDateTime.now());
    quizRepository.save(quiz);

    Question question1 = new Question(quiz, "What is 2 + 2?", "Easy", 10, 8);
    Question question2 = new Question(quiz, "What is the capital of France?", "Medium", 15, 10);

    questionRepository.save(question1);
    questionRepository.save(question2);

    Answer answer1 = new Answer(question1, "4", true);
    Answer answer2 = new Answer(question1, "3", false);
    Answer answer3 = new Answer(question2, "Paris", true);
    Answer answer4 = new Answer(question2, "London", false);

    answerRepository.saveAll(List.of(answer1, answer2, answer3, answer4));

    mockMvc.perform(get("/api/quizzes/" + quiz.getQuizId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.quizId").value(quiz.getQuizId()))
            .andExpect(jsonPath("$.name").value("Quiz with Questions"))
            .andExpect(jsonPath("$.questions", hasSize(2)))
            .andExpect(jsonPath("$.questions[0].questionText").value("What is 2 + 2?"))
            .andExpect(jsonPath("$.questions[0].answers", hasSize(2)))
            .andExpect(jsonPath("$.questions[0].answers[0].answerText").value("4"))
            .andExpect(jsonPath("$.questions[0].answers[1].answerText").value("3"))
            .andExpect(jsonPath("$.questions[1].questionText").value("What is the capital of France?"))
            .andExpect(jsonPath("$.questions[1].answers", hasSize(2)))
            .andExpect(jsonPath("$.questions[1].answers[0].answerText").value("Paris"))
            .andExpect(jsonPath("$.questions[1].answers[1].answerText").value("London"));
            
}

@Test
public void getQuestionsByQuizIdReturnsErrorWhenQuizDoesNotExist() throws Exception {
    
    mockMvc.perform(get("/api/quizzes/999")) // 999 on ID, jota ei ole tietokannassa
            .andExpect(status().isNotFound()); // Odotetaan 404 NOT FOUND
            
}

@Test
public void getQuizResultsReturnsCorrectData() throws Exception {
    Category category = new Category("General", "Category for undescripted quizzes");
    categoryRepository.save(category);

    Quiz quiz = new Quiz(category, "Sample Quiz", "This quiz has questions and answers", true, LocalDateTime.now());
    quizRepository.save(quiz);

    Question question1 = new Question(quiz, "What is 2 + 2?", "Easy", 1, 1);
    Question question2 = new Question(quiz, "What is the capital of France?", "Medium", 1, 1);

    questionRepository.save(question1);
    questionRepository.save(question2);

    Answer answer1 = new Answer(question1, "4", true);
    Answer answer2 = new Answer(question1, "3", false);
    Answer answer3 = new Answer(question2, "Paris", true);
    Answer answer4 = new Answer(question2, "London", false);


    answerRepository.saveAll(List.of(answer1, answer2, answer3, answer4));
    
    this.mockMvc.perform(get("/api/seeresults/" + quiz.getQuizId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.quizName").value("Sample Quiz"))
            .andExpect(jsonPath("$.questionCount").value(2))
            .andExpect(jsonPath("$.totalAnswers").value(2))
            .andExpect(jsonPath("$.totalRightAnswers").value(2));
}

@Test
public void getQuizResultsReturnsNotFoundForNonExistingQuiz() throws Exception {
    
    this.mockMvc.perform(get("/api/seeresults/999"))
            .andExpect(status().isNotFound());
}
}



