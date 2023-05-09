import { useParams } from "react-router-dom";
import Result from "./result";
import * as React from 'react';
import './ResultsPage.css'
import { ThemeContext } from "../App";
import { useContext } from "react";
import Navbar from "./Navbar";
import { Link, MemoryRouter, Route, Routes, useLocation,useNavigate } from 'react-router-dom';
import Pagination from '@mui/material/Pagination';
import PaginationItem from '@mui/material/PaginationItem';
import Skeleton from '@mui/material/Skeleton';
import useMediaQuery from '@mui/material/useMediaQuery';
import useFetch from "../useFetch";


const ResultsPage = (props) => {
  const screen750 = useMediaQuery('(max-width:750px)');
  const screen400 = useMediaQuery('(max-width:400px)');

  const theme=useContext(ThemeContext);
  const location = useLocation();
  const navigate=useNavigate();
  const query = new URLSearchParams(location.search);
  const page = parseInt(query.get('page') || '1', 10);
  const [loading,setLoading]=React.useState(false);
  const {target}=useParams();
  const results=[];
  const { data, isPending, error } = useFetch('http://localhost:8080/?q="Yousef"');
  ///to be removed
  console.log(data);
  ////////////////
  for (let i = 0; i < 27; i++) {
    results[i] = {
      url: "https://geeksforgeeks.org/",
      title: "How to create a multithreaded search engine? "+(i+1),
      siteName: "GeeksforGeeks",
      description: "Lorem ipsum dolor sit amet mahmoud adipisicing elit. Eos enim sobhy saepe quisquam id nam vel totam mahmoud sobhy iure nemo vitae omnis eaque, numquam assumenda quasi molestias adipisci tempora placeat."
    }
  }
  const myResults = results.slice((page - 1) * 12, (page * 12));
  React.useEffect(() => {
    const mode = theme;
    document.documentElement.setAttribute("data-theme", mode);
    Array.from(document.getElementsByTagName("p")).map((element) => element.setAttribute("data-theme", mode));
    Array.from(document.getElementsByTagName("div")).map((element) => element.setAttribute("data-theme", mode));
  }, [theme,page,loading]);

React.useEffect(() => {
  document.querySelector(".Results_page").scrollIntoView({ behavior: 'smooth' });
},[page]);

  function Content() {
    const location = useLocation();
    const query = new URLSearchParams(location.search);
    const page = parseInt(query.get('page') || '1', 10);
    return (
      <Pagination
        page={page}
        sx={{paddingBottom:"15px",
        margin:'auto'}}
        count={Math.ceil(results.length / 12)}
        renderItem={(item) => (
          <PaginationItem
            component={Link}
            to={`./${item.page === 1 ? '' : `?page=${item.page}`}`}
            {...item}
          />
        )}
      />
    );
  }

  return (
    <div className="Results_page">
    
    <Navbar target={target} />
      <div className="Results_Container">
          {
            loading &&
            <>
            <Skeleton variant="rounded" width={screen750&screen400?300:screen750?500:650} height={170} />
            <Skeleton variant="rounded" width={screen750 & screen400 ? 300 : screen750 ? 500 : 650} height={170} />
            <Skeleton variant="rounded" width={screen750 & screen400 ? 300 : screen750 ? 500 : 650} height={170} />
            </>
          }
      {
      !loading && myResults.map((result,idx) => (
      <Result key={idx} target={target} result={result}></Result >))
      }
      </div> 
      {!loading && <Content />}
      
  </div>
  );
}

export default ResultsPage;