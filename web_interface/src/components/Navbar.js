import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Container from '@mui/material/Container';
import './Home.css'
import { useNavigate } from "react-router-dom"
import logo from './goat (2).png'
import Searchbar from "./Searchbar";


const Navbar = (props) => {
  const navigate = useNavigate();
  return ( 
    <AppBar sx={{zIndex:'0'}}  position="fixed">
      <Container sx={{width:'100%',padding:'5px'}} maxWidth="xl">
        <Toolbar sx={{display:'flex',alignItems:'center',justifyContent:'center',gap:'12px',width:'100%'}} >
          <div style={{ display:'flex',flexDirection:'row',cursor: 'pointer' }} onClick={() => navigate('/')}  className="logo">
            <img src={logo} style={{ display: 'block', width: '50px', transform: 'scaleX(-1)' }} alt="" />
            <h4 style={{
              display: 'flex', alignItems: 'center', fontFamily: 'Comfortaa', fontSize: '27px', fontWeight: '700', }}>GOAT</h4>
          </div>
          <Searchbar target={props.target} />
        </Toolbar>
      </Container>
    </AppBar>
  );
}
export default Navbar;