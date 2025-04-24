**This is a web app made for Haaga-Helia UAS course [Ohjelmistoprojekti 1 (Software Development Project 1)](https://software-development-project-1.github.io/).**

[Backlog](https://github.com/orgs/FrontBackStart/projects/1/views/1)


# Quizzer

- **Create Quizzes**: Teachers can easily create quizzes with multiple choice, true/false, and short-answer questions.
- **Save Quizzes**: Save quizzes for future use, making it easy to reuse quizzes without having to recreate them.
- **Student Assignments**: Assign quizzes to individual students or groups for assessment.
- **Quiz Review**: Review students' answers and provide feedback.
- **Analytics**: Track student performance and identify areas where they might need additional support.

## How It Works

1. **Sign Up / Log In**: Teachers sign up or log in to their account.
2. **Create a Quiz**: Use the simple editor to create a quiz with questions of different types.
3. **Assign the Quiz**: Select the students or groups who will take the quiz.
4. **Track Progress**: After students take the quiz, teachers can review their answers and see performance metrics.
5. **Reuse Quizzes**: Save and reuse quizzes for future classes or assignments.

## Getting Started

### Prerequisites

- A modern web browser (Google Chrome, Firefox, Safari, etc.)
- An active internet connection

## Deployment

Deployment is done using [render](https://render.com/) free plan. It can take several minutes for your browser to access the sites below.

### Backend (teacher dashboard)

https://quizzer-zxp3.onrender.com

### Frontend

https://quizzer-front.onrender.com/

### REST api documentation

https://quizzer-zxp3.onrender.com/swagger-ui/index.html

# Developer Guide

### Prerequisites

- Java 17 or higher
- npm
- Git
- Internet connection
- Python3 (for UI testing)

**Clone Repository.** Navigate in terminal/command line to a directory or a folder where you wish to place the source code. Use git to clone the repository

    $ git clone https://github.com/FrontBackStart/quizzer.git

### Backend Development Guide

This development project uses an in-memory H2 database.

**Start Development Server.** Navigate to a directory/folder named `quizzer` (the one with files `mvnw` and `pom.xml`). Compile and run the application using Maven.

    $ ./mvnw spring-boot:run

**Open In Browser.** Type the below url to access app.

    http://localhost:8080

## Frontend Development Guide

The frontend project lives in the same repository as Java/Springboot backend.

Navigate from `quizzer` to `frontend/quizzer` (the right directory is the one with file `package.json`).

**Install dependencies.**

    $ npm install

**Start Development Server.**

    $ npm run dev

**Open in browser.** Type the below url to access app.

    http://localhost:5173

## Test Development

### Integration tests

Integration tests are done using JUnit framework. Develop tests in `quizzer/src/test/java/com/frontbackstart/quizzer/`.

**Run tests.** Navigate to a directory/folder named `quizzer` (the one with files `mvnw` and `pom.xml`).

    $ ./mvnw test

### UI / E2E tests

UI / E2E testing is done using Robot framework.

**Create virtual environment.** Navigate to a directory/folder named `quizzer` (the one with files `mvnw` and `pom.xml`).

    $ python3 -m venv ./venv
    $ source venv/bin/activate
    $ cd robot
    $ pip install robotframework-seleniumlibrary

**Install browser drivers.** Depending on your OS installing browser drivers varies. [Here](https://katekuehl.medium.com/installation-guide-for-google-chrome-chromedriver-and-selenium-in-a-python-virtual-environment-e1875220be2f) seems to be an ok guide to installing browser drivers for Windows, Mac and Linux (not tested, but read through).
