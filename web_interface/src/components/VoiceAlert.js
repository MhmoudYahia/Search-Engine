import Alert from '@mui/material/Alert';
import MicNoneIcon from '@mui/icons-material/MicNone';
import { useState, useEffect, useRef } from "react";
import { AlertTitle } from '@mui/material';
import SpeechRecognition, { useSpeechRecognition } from 'react-speech-recognition';

import Button from '@mui/material/Button';
const VoiceAlert = (props) => {

    const handleStopClick=(e)=>{
    props.SpeechRecognition.stopListening();
    props.setSearchText(props.transcript);
    props.close(false);
  }
  return (<>
    <div className='Voice' >
      <Alert severity="info" icon={<MicNoneIcon sx={{ fontSize: '56px', }} />}>
        <AlertTitle sx={{ fontSize: '24px' }}>Speak Now...</AlertTitle>
        <p style={{padding:'5px'}}>{props.transcript}..</p>
        <Button onClick={handleStopClick} variant="outlined">Stop!</Button>
      </Alert>
    </div> 
  </>
  
);
}

export default VoiceAlert;


// const {
//   transcript,
//   listening,
//   resetTranscript,
//   browserSupportsSpeechRecognition
// } = useSpeechRecognition();

// if (!browserSupportsSpeechRecognition) {
//   return <span>Browser doesn't support speech recognition.</span>;
// }

// return (
//   <div>
//     <p>Microphone: {listening ? 'on' : 'off'}</p>
//     <button onClick={SpeechRecognition.startListening}>Start</button>
//     <button onClick={SpeechRecognition.stopListening}>Stop</button>
//     <button onClick={resetTranscript}>Reset</button>
//     <p>{transcript}</p>
//   </div>
// );