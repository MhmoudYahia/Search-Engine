import { useState } from "react";
import './Home.css'
import * as React from 'react';
import logo from './web-crawler256.png'
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';
import SearchIcon from '@mui/icons-material/Search';
import Button from '@mui/material/Button';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useNavigate } from "react-router-dom"
import KeyboardVoiceIcon from '@mui/icons-material/KeyboardVoice';
import VoiceAlert from "./VoiceAlert";
import SpeechRecognition, { useSpeechRecognition } from 'react-speech-recognition';

const Searchbar = (props) => {
  const screen500 = useMediaQuery('(max-width:650px)');
  console.log(screen500);
  const {
  transcript,
  listening,
  resetTranscript,
  browserSupportsSpeechRecognition
} = useSpeechRecognition(); 
  const [isVoiceClicked,setIsVoiceClicked] =useState(false);
  const [searchText, setSearchText] = useState(props.target?props.target:'');
  const [EmptyBarError, setEmptyBarError] = useState(false);
  const navigate = useNavigate();

  const handleSearchClick = () => {
    if (searchText === '')
      setEmptyBarError(true);
    else {
      setEmptyBarError(false);
      navigate('/search/' + searchText);
    }

  }
  const handleKeyDown = (event) => {
    if (event.key === 'Enter') {
      handleSearchClick();
    }
  }
  const handleVoiceClick=(e)=>{
    SpeechRecognition.startListening({ language: ['ar-SA', 'en-US'] });
    setIsVoiceClicked(true);
  }
  return ( <>
    <div className="search_bar" onKeyDown={handleKeyDown}>
      <TextField
        id="outlined-basic"
        error={EmptyBarError}
        value={searchText}
        onChange={(e, val) => {
          setSearchText(e.target.value)
          if (e.target.value !== '') setEmptyBarError(false);
        }}
        sx={{
          width: screen500 ? "300px" : "500px",
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
          endAdornment: (
            <InputAdornment position="end">
              <KeyboardVoiceIcon onClick={handleVoiceClick} sx={{cursor:'pointer'}} color='primary' />
            </InputAdornment>
          ),
        }}
        placeholder='Search' variant="outlined" />
    </div>
    <div className="search_btn">
      <Button sx={{maxWidth:screen500?"100px":"auto"}} onClick={handleSearchClick} variant="contained">Search</Button>
    </div>
    {isVoiceClicked ? <VoiceAlert setSearchText={setSearchText} transcript={transcript} SpeechRecognition={SpeechRecognition} close={setIsVoiceClicked} />:<></>}
  </> );
}
 
export default Searchbar;