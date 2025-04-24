package com.frontbackstart.quizzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontbackstart.quizzer.domain.Answer;
import com.frontbackstart.quizzer.domain.Question;
import com.frontbackstart.quizzer.domain.Quiz;
import com.frontbackstart.quizzer.repository.AnswerRepository;
import com.frontbackstart.quizzer.repository.QuestionRepository;
import com.frontbackstart.quizzer.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class AnswerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        // Tyhjennä kaikki repositoriot ennen testin pyörittämistä
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        quizRepository.deleteAll();
    }
    
    @Test
public void createAnswerSavesAnswerForPublishedQuiz() throws Exception {
    // Arrange: Luodaan julkaistu Quiz
    Quiz quiz = new Quiz();
    quiz.setPublished(true);
    quiz.setName("Sample Quiz");
    quizRepository.save(quiz);

    // Luodaan kysymys julkaistuun quiziin
    Question question = new Question(quiz, "Sample Question", "easy", 0, 0);
    questionRepository.save(question);

    // Luodaan vastaus, joka liittyy kysymykseen
    Answer answer = new Answer(question, "Sample Answer", true);
    answerRepository.save(answer);
    
    Map<String, Object> requestBody = Map.of("answerId", answer.getAnswerId());

    // Muutetaan vastaus JSON-pyynnöksi
    String requestJson = mapper.writeValueAsString(requestBody);

    // Act: Lähetetään POST-pyyntö vastauksen luomiseksi
    this.mockMvc.perform(post("/api/answers/" + question.getQuestionId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.correct").value(true));; // Odotetaan onnistunutta tallennusta
            

    // Assert: Varmistetaan, että tietokannassa on yksi tallennettu vastaus
    List<Question> questions = questionRepository.findAll();
    assertEquals(1, questions.get(0).getTotalAnswers());
    assertEquals(1, questions.get(0).getTotalRightAnswers());
}

@Test
public void createAnswerDoesNotSaveAnswerWithoutAnswerOptionId() throws Exception {
    Quiz quiz = new Quiz();
    quiz.setPublished(true);
    quiz.setName("Sample Quiz");
    quizRepository.save(quiz);

    Question question = new Question(quiz, "Sample Question", "easy", 0, 0);
    questionRepository.save(question);

    Answer answer = new Answer(question, "Sample Answer", true);
    answerRepository.save(answer);

    Map<String, Object> requestBody = Map.of("answerId", null);

    // Muutetaan vastaus JSON-pyynnöksi
    String requestJson = mapper.writeValueAsString(requestBody);

    // Lähetetään POST-pyyntö ilman answerOptionId
    this.mockMvc.perform(post("/api/answers/" + question.getQuestionId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isNotFound()); // Odotetaan 404  -tilaa

    // Varmistaa, että tietokannassa ei ole tallennettu uusia vastauksia
    List<Question> questions = questionRepository.findAll();
    assertEquals(0, questions.get(0).getTotalAnswers());
    assertEquals(0, questions.get(0).getTotalRightAnswers());
}

@Test
public void createAnswerDoesNotSaveAnswerForNonExistingAnswerOption() throws Exception {
    Quiz quiz = new Quiz();
    quiz.setPublished(true);
    quiz.setName("Sample Quiz");
    quizRepository.save(quiz);

    Question question = new Question(quiz, "Sample Question", "easy", 0, 0);
    questionRepository.save(question);

    Answer answer = new Answer(question, "Sample Answer", true);
    answerRepository.save(answer);

    Map<String, Object> requestBody = Map.of("answerId", 999);

    // Muutetaan vastaus JSON-pyynnöksi
    String requestJson = mapper.writeValueAsString(requestBody);

    // Lähetetään POST-pyyntö ilman answerOptionId
    this.mockMvc.perform(post("/api/answers/" + question.getQuestionId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isNotFound()); // Odotetaan 404 -tilaa

    // Varmistaa, että tietokannassa ei ole tallennettu uusia vastauksia
    List<Question> questions = questionRepository.findAll();
    assertEquals(0, questions.get(0).getTotalAnswers());
    assertEquals(0, questions.get(0).getTotalRightAnswers());
}

@Test
public void createAnswerDoesNotSaveAnswerForNonPublishedQuiz() throws Exception {
     // Luodaan julkaisematon Quiz
     Quiz quiz = new Quiz();
     quiz.setPublished(false); // Quiz ei ole julkaistu
     quiz.setName("Non-Published Quiz");
     quizRepository.save(quiz);
 
     // Luodaan kysymys julkaisemattomaan quiziin
     Question question = new Question(quiz, "Sample Question", "easy", 0, 0);
     questionRepository.save(question);
 
     // Luodaan vastaus kysymykseen
     Answer answer = new Answer(question, "Sample Answer", false);
     answerRepository.save(answer);
 
     // Rakennetaan JSON-pyyntö validilla vastausvaihtoehdon ID:llä
     Map<String, Object> requestBody = Map.of("answerId", answer.getAnswerId());
     String requestJson = mapper.writeValueAsString(requestBody);
 
     // Act: Lähetetään POST-pyyntö vastauksen valitsemiseksi
     this.mockMvc.perform(post("/answers/" + question.getQuestionId())
             .contentType(MediaType.APPLICATION_JSON)
             .content(requestJson))
             .andExpect(status().isNotFound()); // Odotetaan 404 -tilaa
             
 
     // Assert: Varmistetaan, että tietokannan tila ei muuttunut
     Question updatedQuestion = questionRepository.findById(question.getQuestionId())
             .orElseThrow(() -> new RuntimeException("Question not found"));
     assertEquals(0, updatedQuestion.getTotalAnswers());
     assertEquals(0, updatedQuestion.getTotalRightAnswers());
}

}

