import './Home.css'
import * as React from 'react';
import logo from './web-crawler256.png'
import Searchbar from "./Searchbar";
import VoiceAlert from './VoiceAlert';
const Home = () => {
  return ( 
  <div className="Home_Container">
    <div className="Home_content">
        <div className="logo">
        <img  src={logo} alt="" />
      </div>
      <Searchbar />
    </div>
  </div> ); 
}
export default Home;