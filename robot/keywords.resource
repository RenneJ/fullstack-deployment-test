*** Settings ***
Library         SeleniumLibrary

*** Variables ***
${Browser}      Chrome
${QuizName}		European Capital Cities
${QuizDescription}		3rd Grade Geography Quiz
${Question1}	What is the capital of Bulgaria?
${Question2}	What was the first capital city of Finland?
${SubmitAnswerButton} 	xpath://button[contains(text(), "SUBMIT YOUR ANSWER")]

*** Keywords ***
Open Frontend Site
	Open Browser    http://localhost:5173       ${BROWSER}
	Page Should Contain     Quizzes

Open Backend Site
    Open Browser    http://localhost:8080       ${BROWSER}
    Page Should Contain     Quizzes

Go To Add Quiz Page
	Click Link		 link:Add a quiz
	Page Should Contain Element		xpath://h1[@class="mb-3"][contains(text(), "Add a quiz")]

Add Quiz Info
	Input Text		name:name	${QuizName}
	Input Text		name:description	${QuizDescription}
	Click Element	xpath://input[@value="Save"]
	Page Should Contain		${QuizName}

Add First Question
	Input Text		name:questionText	${Question1}
	Click Element	xpath://select[@name="difficulty"]
	Click Element	xpath://option[@value="Hard"]
	Click Element	xpath://input[@value="Save"]
	Page Should Contain		${Question1}

Add Second Question
	Click Link		link:Add a question
	Input Text		name:questionText	${Question2}
	Click Element	xpath://select[@name="difficulty"]
	Click Element	xpath://option[@value="Medium"]
	Click Element	xpath://input[@value="Save"]
	Page Should Contain		${Question2}

Add Answer Options To First Question
	Input Text		name:answerText		Budapest
	Click Element	xpath://select[@name="isRight"]
	Click Element	xpath://option[@value="false"]
	Click Element	xpath://input[@value="Save"]
	Input Text		name:answerText		Sofia
	Click Element	xpath://select[@name="isRight"]
	Click Element	xpath://option[@value="true"]
	Click Element	xpath://input[@value="Save"]
	Page Should Contain		Budapest
	Page Should Contain		Sofia

Add Answer Options To Second Question And Save
	Input Text		name:answerText		Turku
	Click Element	xpath://select[@name="isRight"]
	Click Element	xpath://option[@value="true"]
	Click Element	xpath://input[@value="Save"]
	Input Text		name:answerText		Helsinki
	Click Element	xpath://select[@name="isRight"]
	Click Element	xpath://option[@value="false"]
	Click Element	xpath://input[@value="Save"]
	Page Should Contain		Turku
	Page Should Contain		Helsinki
	Click Link		link:Cancel
	Page Should Contain		Quizzes

Go To Categories Page
	Click Link		link:See Categories
	Page Should Contain Element		link:Add a Category

Go To Add Category Page
	Click Link		link:Add a Category
	Page Should Contain Element		xpath://h1[@class="mb-3"][contains(text(), "Add a Category")]

Add Category Name And Description Then Save
	Input Text		name:name	History 6
	Input Text		name:description	History quizzes for 6th graders
	Click Element	xpath://input[@value="Save"]
	Page Should Contain		Categories		# On category list page
	Page Should Contain		History quizzes for 6th graders

Click Edit Quiz Button

Select A Quiz
	Click Element	xpath://button[contains(text(), "Capital cities")]
	Wait Until Page Contains Element		xpath://h4[@class="MuiTypography-root MuiTypography-h4 css-f84ov1-MuiTypography-root"][contains(text(), "Capital cities")]

Answer First Question Right
	Select Radio Button		:r1:		2
	Click Element	xpath:(//button[contains(text(), "SUBMIT YOUR ANSWER")])[1]		# Blasphemous! First index not 0????
	Alert Should Be Present		Correct! Great job!

Answer Second Question Wrong
	Select Radio Button		:r3:		6
	Sleep	2
	Click Element	xpath:(//button[contains(text(), "SUBMIT YOUR ANSWER")])[2]		# Disgusting! 2nd index 2???
	Alert Should Be Present		Wrong answer. Try again!

Go Back To Quiz List
	Click Link		link:Quizzes
	Page Should Contain Element		xpath://h4[@class="MuiTypography-root MuiTypography-h4 css-1g4r6ka-MuiTypography-root"][contains(text(), "Quizzes")]
