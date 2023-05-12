import { useParams } from "react-router-dom";
import Result from "./result";
import * as React from 'react';
import './ResultsPage.css'
import { ThemeContext } from "../App";
import { useContext } from "react";
import Navbar from "./Navbar";
import { Link, useLocation, } from 'react-router-dom';
import Pagination from '@mui/material/Pagination';
import PaginationItem from '@mui/material/PaginationItem';
import Skeleton from '@mui/material/Skeleton';
import useMediaQuery from '@mui/material/useMediaQuery';
import useFetch from "../useFetch";


const ResultsPage = (props) => {

  const location = useLocation();
  const {target}=useParams();
  const [results,setResults]=React.useState(null);
  const query = new URLSearchParams(location.search);
  const page = parseInt(query.get('page') || '1', 10);
  const [toSearch, setToSearch] = React.useState('http://localhost:8080/api?text=' + target);

  React.useEffect(()=>{
    setToSearch('http://localhost:8080/api?text=' + target);
  },[target]);
  
  const { data, isPending, error } = useFetch(toSearch);
  
  const [myResults,setMyResults]=React.useState(null);
  React.useEffect((() => {
      setResults(data);
      setMyResults(data&&data.slice((page - 1) * 12, (page * 12)));
    }),[data,page]);

React.useEffect(() => {
  document.querySelector(".Results_page").scrollIntoView({ behavior: 'smooth' });
},[page])

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
            isPending &&
            <>
            <Skeleton variant="rounded" width={650} height={170} />
            <Skeleton variant="rounded" width={650} height={170} />
            <Skeleton variant="rounded" width={650} height={170} />
            </>
          }
      {
      !error && myResults && myResults.map((result,idx) => (
      <Result key={idx} target={target} result={result}></Result >))
      }
      {error && <div className="no-results">No Results found for "{target}"</div> }
      </div> 
      {!error && myResults && <Content />}
      
  </div>
  );
}

export default ResultsPage;