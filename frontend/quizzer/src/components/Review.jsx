import { useState, useEffect } from "react";
import { Box, Button, TextField, FormControl, FormLabel, FormControlLabel, Typography, Radio, RadioGroup } from "@mui/material";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { getQuizByID } from "../services/fetches";
import { saveReviewForQuiz } from "../services/fetches";

function Review() {
    const { quizId } = useParams();
    const [review, setReview] = useState({
        nickname: "",
        rating: 0,
        reviewText: ""
    })
    const [ratings, setRatings] = useState([1, 2, 3, 4, 5])
    const [quizDetails, setQuizDetails] = useState([]);
    const navigate = useNavigate();

    async function fetchQuizById() {
        const data = await getQuizByID(quizId);
        setQuizDetails(data)
    }

    useEffect(() => {
        fetchQuizById();
    }, [quizId]);

    const handleSubmitAnswer = async () => {
        console.log(review)
        saveReviewForQuiz(quizId, review)
        navigate(`/quizzes/${quizId}/reviews`)
    }
    return (
        <>
            <Typography sx={{ pt: 1.5, pl: 1 }} variant="h4">Add a review for "{quizDetails.name}"</Typography>
            <Box sx={{ m: 1 }}>
                <TextField
                    fullWidth
                    label="Nickname"
                    onChange={event => setReview({ ...review, nickname: event.target.value })}
                />
            </Box>
            <Box sx={{ m: 1 }}>
                <FormControl>
                    <FormLabel>Rating</FormLabel>
                    <RadioGroup
                        onChange={(event) =>
                            setReview({ ...review, rating: Number(event.target.value) })
                        }
                    >
                        {ratings.map((rating, i) => (
                            <FormControlLabel
                                key={i}
                                value={rating}
                                control={<Radio />}
                                label={rating}
                            />
                        ))}
                    </RadioGroup>
                </FormControl>
            </Box>
            <Box sx={{ m: 1 }}>
                <TextField
                    fullWidth
                    label="Review"
                    onChange={event => setReview({ ...review, reviewText: event.target.value })}
                />
            </Box>
            <Button
                onClick={handleSubmitAnswer}
            >
                Submit your review
            </Button>

        </>
    )
}


export default Review;