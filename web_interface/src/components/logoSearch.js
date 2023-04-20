import logo from './web-crawler256.png'
import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { ThemeProvider, createTheme } from '@mui/material';
import InputAdornment from '@mui/material/InputAdornment';
import SearchIcon from '@mui/icons-material/Search'; 
import { hover } from '@testing-library/user-event/dist/hover';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
  components: {
    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          borderRadius: '80px',
        },
      }
    },
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: '80px',
          width: '130px',
          fontSize: '15px',
          fontFamily: 'Poppins, sans-serif',
        },
      }
    }
  }
})
const lightTheme=createTheme({
  components:{
  MuiOutlinedInput:{
    styleOverrides:{
      root:{
        borderRadius: '80px',
      },
      notchedOutline:{
        borderColor: '#1976d2',
      },
    }
  },
  MuiButton:{
    styleOverrides: {
      root: {
        borderRadius: '80px',
        width:'130px',
        fontSize:'15px',
        fontFamily:'Poppins, sans-serif',
      },
    }
  }
  }
});
const Search = () => {
  return ( 
  <div className="Home_content">
    <div className="logo">
      <img src={logo} alt="" />
    </div>
    <div className="search_bar">      
        <TextField 
        id="outlined-basic" 
        fullWidth
        sx={{
          "& .MuiOutlinedInput-root:hover": {
            "& > fieldset": {
              borderColor: "#1976d2"
            }
          }
        }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon color='primary' />
            </InputAdornment>
          ),
        }}
          
        placeholder='Search' variant="outlined" />
    </div>
    <div className="search_btn">
          <Button variant="contained">Search</Button>
    </div>
  </div>);
}

export default Search;