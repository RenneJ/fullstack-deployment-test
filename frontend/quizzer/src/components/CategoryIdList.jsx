import { useState, useEffect } from "react";
import { AgGridReact } from 'ag-grid-react';
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import Typography from '@mui/material/Typography';
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { getCategoryByID } from "../services/fetches";

function CategoryIdList() {
    const [quizzes, setQuizzes] = useState([]);
    const [categoryDetails, setCategoryDetails] = useState([]);
    const { categoryId } = useParams();
    const navigate = useNavigate();

    const gridOptions = {
        autoSizeStrategy: {
            type: 'fitGridWidth',
        },
        columnDefs: [
            { field: "name", headerName: "Quiz Name" },
            { field: "description", headerName: "Description" },
            {
                field: "created",
                headerName: "Created Date",
                valueFormatter: (params) => {
                    const date = new Date(params.value);
                    return date.toLocaleDateString('de-DE');
                },
            },
            {
                cellRenderer: (params) => {
                    const quizId = params.data.quizId;
                    return (
                        <button
                            style={{
                                backgroundColor: "transparent",
                                color: "blue",
                                border: "none",
                                textDecoration: "underline",
                                cursor: "pointer"
                            }}
                            onClick={() => navigate(`/seeresults/${quizId}`)}
                        >
                            See results
                        </button>
                    );
                },
                width: 100,
            }
        ]
    }

    async function fetchQuizData() {
        const data = await getCategoryByID(categoryId);
        setQuizzes(data);
        setCategoryDetails({
            name: data[0].category.name,
            description: data[0].category.description
        })
    }



    useEffect(() => {
        fetchQuizData();
    }, [categoryId]);

    return (
        <>
            <Typography sx={{ pt: 1.5, pl: 1 }} variant="h4">{categoryDetails.name}</Typography>
            <Typography sx={{ pt: 0.5, pl: 1.5 }}>{categoryDetails.description}</Typography>
            <div className='ag-theme-material' style={{ height: 500 }}>
                <AgGridReact
                    rowData={quizzes}
                    gridOptions={gridOptions}
                    suppressCellFocus={true}
                />
            </div>
        </>
    )
}

export default CategoryIdList;
