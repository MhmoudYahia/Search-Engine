import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Container from '@mui/material/Container';
import './Home.css'
import { useNavigate } from "react-router-dom"
import logo from './web-crawler64.png'
import Searchbar from "./Searchbar";


const Navbar = (props) => {
  const navigate = useNavigate();
  return ( 
    <AppBar sx={{zIndex:'0'}}  position="fixed">
      <Container sx={{width:'100%',padding:'5px'}} maxWidth="xl">
        <Toolbar sx={{display:'flex',alignItems:'center',justifyContent:'center',gap:'12px',width:'100%'}} >
          <div style={{ display:'flex',flexDirection:'row',cursor: 'pointer' }} onClick={() => navigate('/')}  className="logo">
            <img src={logo} style={{width:'45px'}} alt="" />
            <h4 style={{ display: 'flex', fontFamily: 'Comfortaa', fontSize: '27px', fontWeight: '700', color: '#7d8b95' }}>Quester</h4>
          </div>
          <Searchbar target={props.target} />
        </Toolbar>
      </Container>
    </AppBar>
  );
}
export default Navbar;