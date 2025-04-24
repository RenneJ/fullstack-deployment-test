import { useState, useEffect } from "react";
import { AgGridReact } from 'ag-grid-react';
import { useParams } from "react-router-dom";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import { Card, FormControl, FormControlLabel, Radio, RadioGroup } from "@mui/material";
import { getQuizByID } from "../services/fetches";
import { getAnswersByQuestionID } from "../services/fetches";

function QuizView() {
    const [questions, setQuestions] = useState([]);
    const { quizId } = useParams();
    const [quizDetails, setQuizDetails] = useState(null);
    const [selectedAnswers, setSelectedAnswers] = useState({}); // Track selected answers

    async function fetchQuizData() {
            const data = await getQuizByID(quizId);

            setQuizDetails({
                name: data.name,
                description: data.description,
                created: data.created,
                questionCount: data.questionCount,
                category: data.category,
            });
            setQuestions(data.questions);
    }

    useEffect(() => {
        fetchQuizData();
    }, [quizId]);

    const handleAnswerChange = (questionId, answerId) => {
        setSelectedAnswers((prev) => ({
            ...prev,
            [questionId]: answerId,
        }));
    };

    const handleSubmitAnswer = async (questionId) => {
        const selectedAnswerId = selectedAnswers[questionId];
        if (!selectedAnswerId) {
            alert("Please select an answer before submitting.");
            return;
        }

        try {
            const result = await getAnswersByQuestionID(questionId, selectedAnswerId);

            const isCorrect = result.correct === true;

            alert(isCorrect ? "Correct! Great job!" : "Wrong answer. Try again!");
        } catch (error) {
            alert("Error submitting your answer. Please try again.");
            console.error(error);
        }
    };


    return (
        <>
            <Typography variant="h4" gutter>
                {quizDetails?.name || "Loading..."}
            </Typography>
            <Box sx={{ p: 1 }}>
                {quizDetails && (
                    <>
                        <div>{quizDetails.description}</div>
                        <div>
                            Added on: {quizDetails.created ? new Date(quizDetails.created).toLocaleDateString('de-DE') : "Loading..."} -
                            Questions: {quizDetails.questionCount} -
                            Category: {quizDetails?.category || "Loading..."}
                        </div>
                    </>
                )}
            </Box>
            <div>
            {questions.map((question, index) => (
                    <Card key={index} style={{ marginBottom: 10, padding: 20 }}>
                        <Typography variant="h6">{question.questionText}</Typography>
                        <Typography variant="body2">
                            Question {index + 1} of {quizDetails?.questionCount} - Difficulty:{" "}
                            {question.difficulty || "Unknown"}
                        </Typography>
                        <FormControl component="fieldset">
                            <RadioGroup
                                value={selectedAnswers[question.questionId] || ""}
                                onChange={(e) =>
                                    handleAnswerChange(question.questionId, e.target.value)
                                }
                            >
                                {question.answers.map((answer) => (
                                    <FormControlLabel
                                        key={answer.answerId}
                                        value={answer.answerId}
                                        control={<Radio />}
                                        label={answer.answerText}
                                    />
                                ))}
                            </RadioGroup>
                        </FormControl>
                        <br />
                        <button
                            style={{
                                backgroundColor: "transparent",
                                color: "blue",
                                border: "none",
                                cursor: "pointer",
                            }}
                            onClick={() => handleSubmitAnswer(question.questionId)}
                        >
                            SUBMIT YOUR ANSWER
                        </button>
                    </Card>
                ))}
            </div>
        </>
    );
}

export default QuizView;
