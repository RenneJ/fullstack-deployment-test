export function getAllQuizzes() {
  return fetch(`${import.meta.env.VITE_BACKEND_URL}/api/quizzes`).then((response) =>
    response.json()
  );
}

export function getCategoryByID(categoryId) {
  return fetch(`${import.meta.env.VITE_BACKEND_URL}/api/categories/${categoryId}`).then((response) =>
    response.json()
  );
}

export function getQuizByID(quizId) {
  return fetch(`${import.meta.env.VITE_BACKEND_URL}/api/quizzes/${quizId}`).then((response) =>
    response.json()
  );
}

export function getAllCategories() {
  return fetch(`${import.meta.env.VITE_BACKEND_URL}/api/categories`).then((response) =>
    response.json()
  );
}

export function getResultsByQuizID(quizId) {
  return fetch(`${import.meta.env.VITE_BACKEND_URL}/api/seeresults/${quizId}`).then((response) =>
    response.json()
  );
}

export function saveReviewForQuiz(quizId, review) {
  return fetch(`${import.meta.env.VITE_BACKEND_URL}/api/quizzes/${quizId}/reviews`,{
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(review),
  }).then((response) =>
    response.json()
  );
}

export function getAnswersByQuestionID(questionId, selectedAnswerId) {
  return fetch(`${import.meta.env.VITE_BACKEND_URL}/api/answers/${questionId}`,{
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ answerId: parseInt(selectedAnswerId) }),
  }).then((response) =>
    response.json()
  );

  
}

export function getReviewsByQuizID(quizId) {
  return fetch(`${import.meta.env.VITE_BACKEND_URL}/api/quizzes/${quizId}/reviews`).then((response) =>
    response.json()
  );
}

export function editReviewById(reviewId, updatedReview) {
  return fetch(`${import.meta.env.VITE_BACKEND_URL}/api/reviews/${reviewId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(updatedReview),
  }).then((response) =>
    response.json()
  );
}