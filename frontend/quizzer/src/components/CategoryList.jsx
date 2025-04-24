import { useState, useEffect } from "react";
import Typography from '@mui/material/Typography';
import { AgGridReact } from 'ag-grid-react';
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { useNavigate } from "react-router-dom";
import { getAllCategories } from "../services/fetches";

function CategoryList() {
	const [categories, setCategories] = useState([]);
	const navigate = useNavigate();


	const gridOptions = {
		autoSizeStrategy: {
			type: 'fitGridWidth',
		},
		columnDefs: [
			{
				field: "name",
				headerName: "Category Name",
				width: 100,
				cellRenderer: (params) => {
					const categoryId = params.data.categoryId;
					return (
						<button
							style={{
								backgroundColor: "transparent",
								color: "blue",
								border: "none",
								textDecoration: "underline",
								cursor: "pointer"
							}}
							onClick={() => navigate(`/categories/${categoryId}`)}
						>
							{params.value}
						</button>
					);
				},
			},
			{ field: "description", headerName: "Description" }
		]
	}

	async function getCategories() {
		const response = await getAllCategories();
		setCategories(response);
	};

	useEffect(() => {
		getCategories();
	}, []);

	return (
		<>
			<Typography sx={{ pt: 1.5, pl: 1 }} variant="h4">Categories</Typography>
			<div className='ag-theme-material' style={{ height: 500 }}>
				<AgGridReact
					rowData={categories}
					gridOptions={gridOptions}
					suppressCellFocus={true}
				/>
			</div>
		</>
	)
}

export default CategoryList;
