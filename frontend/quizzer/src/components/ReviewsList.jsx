import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom'; 
import { getReviewsByQuizID } from "../services/fetches";
import { Box, Typography, Card, CardContent, Grid, Rating, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

const ReviewsList = () => {
    const { quizId } = useParams(); 
    const [reviews, setReviews] = useState([]);
    const [quizName, setQuizName] = useState('');
    const [averageRating, setAverageRating] = useState(0);
    const navigate = useNavigate();

    async function fetchReviews () {
        try {
            const data = await getReviewsByQuizID(quizId); // Fetch reviews data
            setQuizName(data.quizName);
            setAverageRating(data.averageRating);
            setReviews(data.reviews);
        } catch (err) {
            setError('Failed to load reviews. Please try again later.');
        }
    }

    useEffect(() => {
        fetchReviews();
    }, [quizId]);

    const handleAddReview = () => {
        navigate(`/quizzes/${quizId}/addReview`);
    };

    return (
        <Box sx={{ p: 3 }}>
            <Typography variant="h4" gutterBottom>
                Reviews for {quizName} Quiz
            </Typography>
            <Typography variant="h6" gutterBottom>
                Average Rating: <Rating value={averageRating} precision={0.1} readOnly /> ({averageRating.toFixed(1)}/5)
            </Typography>
            {reviews.length === 0 ? (
                <Typography>This Quiz doesn't have any reviews yet.</Typography>
            ): 
            (
                <Grid container spacing={2}>
                    {reviews.map((review) => (
                        <Grid item xs={12} sm={6} md={4} key={review.reviewId}>
                            <Card>
                                <CardContent>
                                    <Typography variant="h6">{review.nickname}</Typography>
                                    <Rating value={review.rating} readOnly />
                                    <Typography variant="body2" sx={{ mt: 1 }}>
                                        {review.reviewText}
                                    </Typography>
                                    <Typography variant="caption" color="textSecondary">
                                        Reviewed on: {new Date(review.createdAt).toLocaleDateString('de-DE')}
                                    </Typography>
                                    <Box sx={{ mt: 2 }}>
                                        <Button
                                            variant="contained"
                                            color="primary"
                                            onClick={() => navigate(`/quizzes/${quizId}/review/${review.reviewId}/edit`)}
                                        >
                                            Edit
                                        </Button>
                                    </Box>
                                </CardContent>
                            </Card>
                        </Grid>
                    ))}
                </Grid>
            )}
            <Box sx={{ mt: 4 }}>
                <Button
                    variant="contained"
                    color="secondary"
                    onClick={handleAddReview}
                    sx={{ mb: 3 }}
                >
                    Add Review
                </Button>
            </Box>
        </Box>
    );
};

export default ReviewsList;