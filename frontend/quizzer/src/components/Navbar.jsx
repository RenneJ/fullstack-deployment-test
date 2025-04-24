import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import MenuItem from '@mui/material/MenuItem';
import { Link } from 'react-router-dom';


function Navbar() {
	return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6">
          QUIZZER
          </Typography>
          <MenuItem>
	          <Typography
		          variant="h6"
		          component={Link}
							to="/quizzes"
		          sx={{ flexGrow: 1, textDecoration: 'none', color: 'inherit' }}
						>
							Quizzes
	          </Typography>
          </MenuItem>
          <MenuItem>
	          <Typography
		          variant="h6"
		          component={Link}
							to="/categories"
		          sx={{ flexGrow: 1, textDecoration: 'none', color: 'inherit' }}
						>
	          Categories
	          </Typography>
          </MenuItem>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
export default Navbar;
