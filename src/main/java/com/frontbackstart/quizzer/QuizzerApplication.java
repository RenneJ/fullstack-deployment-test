package com.frontbackstart.quizzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;
import java.util.List;

import com.frontbackstart.quizzer.repository.AnswerRepository;
import com.frontbackstart.quizzer.domain.Answer;
import com.frontbackstart.quizzer.repository.QuestionRepository;
import com.frontbackstart.quizzer.domain.Question;
import com.frontbackstart.quizzer.repository.QuizRepository;
import com.frontbackstart.quizzer.domain.Quiz;
import com.frontbackstart.quizzer.repository.CategoryRepository;
import com.frontbackstart.quizzer.domain.Category;
import com.frontbackstart.quizzer.repository.ReviewRepository;
import com.frontbackstart.quizzer.domain.Review;

@SpringBootApplication
public class QuizzerApplication {

	private static final Logger log = LoggerFactory.getLogger(QuizzerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(QuizzerApplication.class, args);
	}

	@Bean
		public CommandLineRunner testDataInitializer(CategoryRepository categoryRepo, AnswerRepository answerRepo, QuestionRepository questionRepo, QuizRepository quizRepo, ReviewRepository reviewRepo) {
			LocalDateTime added = LocalDateTime.of(2021, 11, 07, 15, 15);
			return (args) -> {
				List<Quiz> quizzes = quizRepo.findAll();
				if (quizzes.isEmpty()){
					log.info("QuizRepo empty");

					log.info("Create Category");

					Category category1 = new Category("Maantieto", "Mantsan kysymyksiä");
					categoryRepo.save(category1);

					log.info("Create Quizzes");
					Quiz quiz1 = new Quiz(category1, "Capital cities", "Quiz about capital cities", true, added);
					quizRepo.save(quiz1);
					LocalDateTime quiz2Added = LocalDateTime.of(2022, 11, 9, 9, 30);
					Quiz quiz2 = new Quiz(category1, "Famous countries", "Quiz about countries", true, quiz2Added);
					quizRepo.save(quiz2);
					LocalDateTime quiz3Added = LocalDateTime.of(2023, 11, 9, 9, 30);
					Quiz quiz3 = new Quiz(category1, "Planets", "Quiz about planets", false, quiz3Added);
					quizRepo.save(quiz3);

					log.info("Create Questions");
					Question question1 = new Question(quiz1, "What is the capital of Finland?", "Easy", 10, 8);
					questionRepo.save(question1);
					Question question2 = new Question(quiz1, "What is the capital of Sweden?", "Easy", 10, 6);
					questionRepo.save(question2);
					Question question3 = new Question(quiz3, "How many planets are there in our solar system?", "Medium", 12, 4);
					questionRepo.save(question3);

					log.info("Create Answers");
					Answer ansA = new Answer(question1, "New York", false);
					answerRepo.save(ansA);
					Answer ansB = new Answer(question1, "Helsinki", true);
					answerRepo.save(ansB);
					Answer ansC = new Answer(question1, "Stockholm", false);
					answerRepo.save(ansC);
					Answer ansD = new Answer(question1, "Timbuktu", false);
					answerRepo.save(ansD);

					Answer ansE = new Answer(question2, "New York", false);
					answerRepo.save(ansE);
					Answer ansF = new Answer(question2, "Helsinki", false);
					answerRepo.save(ansF);
					Answer ansG = new Answer(question2, "Stockholm", true);
					answerRepo.save(ansG);
					Answer ansH = new Answer(question2, "Timbuktu", false);
					answerRepo.save(ansH);

					Answer ansI = new Answer(question3, "8", true);
					answerRepo.save(ansI);
					Answer ansJ = new Answer(question3, "9", false);
					answerRepo.save(ansJ);

					log.info("Create Reviews");

					Review rewA = new Review(quiz1, "GeoGraphy Lover", 5, "Absolutely love this quiz. 10/10");
					reviewRepo.save(rewA);
					Review rewb = new Review(quiz1, "QuizHater95", 1, "This quiz sucks :(");
					reviewRepo.save(rewb);
					Review rewc = new Review(quiz1, "MediocreMatt", 3, "Too much water.");
					reviewRepo.save(rewc);

					Review rewD = new Review(quiz1, "GeoGraphy Lover's Alt", 4, "Why hasn't anyone else reviewed this baller quiz?");
					reviewRepo.save(rewD);
				};
			};
		}
}
