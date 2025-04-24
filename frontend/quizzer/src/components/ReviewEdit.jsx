import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { getReviewsByQuizID, editReviewById } from "../services/fetches";
import { TextField, Button, Typography, Container, Paper, Rating } from "@mui/material";
import { useNavigate } from "react-router-dom";

const ReviewEdit = () => {
  const { quizId } = useParams(); 
  const { reviewId } = useParams(); 
  const [review, setReview] = useState(null);
  const [quizName, setQuizName] = useState("");
  const navigate = useNavigate();
  const [editedReview, setEditedReview] = useState({
    nickname: "",
    rating: 0,
    reviewText: "",
  });

  const fetchQuizData = async () => {
    try {
      const data = await getReviewsByQuizID(quizId); // Pass the quizId as needed (here it's hardcoded to 1)
      const foundReview = data.reviews.find((r) => r.reviewId === parseInt(reviewId, 10));
      if (foundReview) {
        setReview(foundReview);
        setQuizName(data.quizName);
        setEditedReview({
          nickname: foundReview.nickname,
          rating: foundReview.rating,
          reviewText: foundReview.reviewText,
        });
      } else {
        console.error("Review not found");
      }
    } catch (error) {
      console.error("Error fetching reviews:", error);
    }
  };

  useEffect(() => {
    fetchQuizData();
  }, [reviewId]);

  const handleUpdate = (updatedReview) => {
    editReviewById(reviewId, updatedReview)
      .then(() => {
        setReview((prevReview) => ({ ...prevReview, ...updatedReview }));
        setEditedReview(updatedReview);
      })
      .catch((error) => console.error("Error updating review:", error));
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedReview({
      ...editedReview,
      [name]: name === "rating" ? parseInt(value, 10) : value,
    });
  };

  const handleSave = () => {
    if (!editedReview.nickname.trim() || !editedReview.reviewText.trim()) {
      alert("Nickname and review text cannot be empty.");
      return;
    }
    if (editedReview.rating < 1 || editedReview.rating > 5 || isNaN(editedReview.rating)) {
      alert("Rating must be a number between 1 and 5.");
      return;
    }
    handleUpdate(editedReview);
    navigate(`/quizzes/${quizId}/reviews`)
  };

  if (!review) {
    return <div>Loading review...</div>;
  }

  return (
    <Container style={{ padding: "20px" }}>
      <Typography variant="h4" gutterBottom>
        {quizName}
      </Typography>
      <Paper style={{ padding: "20px", marginBottom: "10px" }}>
        <TextField
          label="Nickname"
          name="nickname"
          value={editedReview.nickname}
          onChange={handleChange}
          fullWidth
          margin="normal"
        />
        <div style={{ marginBottom: "20px" }}>
          <Typography component="legend">Rating</Typography>
          <Rating
            name="rating"
            value={editedReview.rating}
            onChange={(event, newValue) => {
              setEditedReview({
                ...editedReview,
                rating: newValue,
              });
            }}
            precision={1} // You can adjust precision if needed
          />
        </div>
        <TextField
          label="Review Text"
          name="reviewText"
          value={editedReview.reviewText}
          onChange={handleChange}
          multiline
          rows={4}
          fullWidth
          margin="normal"
        />
        <div style={{ marginTop: "20px" }}>
          <Button
            variant="contained"
            color="primary"
            onClick={handleSave}
            style={{ marginRight: "10px" }}
          >
            Save
          </Button>
        </div>
      </Paper>
    </Container>
  );
};

export default ReviewEdit;
