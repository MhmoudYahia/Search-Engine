import { useEffect, useState } from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';


const darkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#eae6e1',
    },
  },
  components: {
    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          borderRadius: '80px',
        },
        notchedOutline: {
          borderColor: '#eae6e1',
        },
      }
    },
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: '80px',
          width: '130px',
          fontSize: '15px',
          backgroundColor:'#eae6e1',
          fontFamily: 'Poppins, sans-serif',
        },
      }
    }
  }
})
const lightTheme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#937d69',
    },
  },
  components: {
    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundColor: 'white !important',
        },
      }
    },
    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          borderRadius: '80px',
        },
        notchedOutline: {
          borderColor: '#937d69',
        },
      }
    },
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: '80px',
          width: '130px',
          fontSize: '15px',
          backgroundColor:'#937d69',
          fontFamily: 'Poppins, sans-serif',
        },
      }
    }
  }
});

export const useDarkMode = () => {
  const [theme, setTheme] = useState('dark');
  const [themeMode,setThemeMode]=useState(darkTheme);

  const setMode = mode => {
    window.localStorage.setItem('theme', mode)
    setTheme(mode)
    mode === 'light' ? setThemeMode(lightTheme) : setThemeMode(darkTheme);
  };

  const themeToggler = () => {
    if(theme === 'light')
      {      
        setMode('dark');
        setThemeMode(darkTheme);
      }else{      
        setMode('light');
        setThemeMode(lightTheme);
      }  
};

  useEffect(() => {
    const localTheme = window.localStorage.getItem('theme');
    if(localTheme){ 
      setTheme(localTheme);
      localTheme === 'light' ? setThemeMode(lightTheme) : setThemeMode(darkTheme);
    }
  }, []);
  return [theme, themeToggler,themeMode]
};