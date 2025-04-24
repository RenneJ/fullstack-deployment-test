import { useState, useEffect } from "react";
import { AgGridReact } from 'ag-grid-react';
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import Typography from '@mui/material/Typography';
import { useParams } from "react-router-dom";
import { getResultsByQuizID } from "../services/fetches";

function ResultsList() {
    const [results, setResults] = useState([]);
    const [questions, setQuestions] = useState([]);
    const { quizId } = useParams();


    const gridOptions = {
		autoSizeStrategy: {
			type: 'fitGridWidth',
		},
        columnDefs: [
            { field: "questionText", headerName: "Question" },
            { field: "difficulty", headerName: "Difficulty" },
            { field: "totalAnswers", headerName: "Total answers" },
            { 
                headerName: "Correct answer %",
                valueGetter: function (params) {
                    const percent = Math.round((params.data.totalRightAnswers / params.data.totalAnswers) * 100)
                    return percent + "%"
                }
            },
            { field: "totalRightAnswers", headerName: "Correct answers" },
            { 
                headerName: "Wrong answers",
                valueGetter: (p) => p.data.totalAnswers - p.data.totalRightAnswers
            },
        ]
    }

    async function getResults() {
        const data = await getResultsByQuizID(quizId);

        setResults({
            totalAnswers: data.totalAnswers,
            totalRightAnswers: data.totalRightAnswers,
            quizId: data.quizId,
            questionCount: data.questionCount,
            quizName: data.quizName
        });
        setQuestions(data.questions);
    }

    useEffect(() => {
        getResults(); 
    }, [quizId]);

    return (
    <>
    <Typography variant="h4">Results of "{results.quizName}"</Typography>
    <div>{results.totalAnswers} answers to {results.questionCount} questions</div>
        <div className='ag-theme-material' style={{ height: 500 }}>
            <AgGridReact
                rowData={questions}
                gridOptions={gridOptions}
                suppressCellFocus={true}
            />
        </div>
    </>
    )
}

export default ResultsList