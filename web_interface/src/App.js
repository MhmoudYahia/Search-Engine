import * as React from 'react';
import { useState} from "react";
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { BrowserRouter,Routes, Route } from "react-router-dom"
import Home from './components/Home';
import ResultsPage from './components/ResultsPage';
import './components/Home.css'
import './App.css';
import { useDarkMode } from './components/useDarkMode';
import { ScrollToTop } from 'react-router-scroll-to-top';
import DarkModeToggle from "react-dark-mode-toggle";


import ReactSwitch from 'react-switch';

export const ThemeContext=React.createContext(null);

function App() {
  const [theme, themeToggler,themeMode] = useDarkMode();

  return (
    <ThemeContext.Provider value={{theme,themeToggler}}>
      <ThemeProvider theme={themeMode}>
        <BrowserRouter>
            <div className="App" id={theme} style={{ height: "100vh" }}>
            <ScrollToTop />
            <Routes>
              <Route path='/' element={<Home />} />
              <Route path="/search/:target?page=:id" element={<ResultsPage />} />
              <Route path='/search/:target' element={<ResultsPage />} />
            </Routes>
          </div>
        </BrowserRouter>
      </ThemeProvider>
      <div className="toggle-btn">
          <DarkModeToggle
            onChange={themeToggler}
            checked={theme === "dark"}
            size={80}
          />    
      </div>
    </ThemeContext.Provider>
  );
}

export default App;
