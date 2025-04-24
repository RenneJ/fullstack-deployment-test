# ROBOT FRAMEWORK TESTS

*** Settings ***
Documentation	Test to check RF environment w/ SeleniumLibrary & ChromeDriver.
Resource	keywords.resource
Library         SeleniumLibrary

*** Test Cases ***
Sites open correctly
    Open Frontend Site
    Open Backend Site
    Close Browser

# Backend (teacher dashboard)
Adding new quiz works
	Open Backend Site
	Go To Add Quiz Page
	Add Quiz Info
	Add First Question
	Add Answer Options To First Question
	Add Second Question
	Add Answer Options To Second Question And Save
	Close Browser

Adding new category works
	Open Backend Site
	Go To Categories Page
	Go To Add Category Page
	Add Category Name And Description Then Save
	Close Browser

Editing new quiz works
	Open Backend Site
#	Click Edit Quiz Button
	Close Browser

# Frontend (students' interface)
Answering a quiz works
	Open Frontend Site
	Select A Quiz
	Answer First Question Right
	Answer Second Question Wrong
	Go Back To Quiz List
	Close Browser
