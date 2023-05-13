import './Home.css'
import * as React from 'react';
import logo from './Photos/goat (2).png'
import Searchbar from "./Searchbar";
import VoiceAlert from './VoiceAlert';
import Q from './Photos/q.png'
const Home = () => {
  return ( 
  <div className="Home_Container">
    <div className="Home_content">
        <div className="logo">
        <img src={logo} alt="" />
          <h1 style={{ display: 'flex', fontFamily: 'Comfortaa', fontSize: '62px', fontWeight: '700', }}>GOAT</h1>
      </div>
      <Searchbar />
    </div>
  </div> ); 
}
export default Home;