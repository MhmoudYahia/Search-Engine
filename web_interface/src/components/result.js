import React from "react";
import parse from 'html-react-parser'
import './ResultCard.css'

const Result = (props) => {
  const searchText = props.target;
  const Result = props.result;
  const targetWords = searchText.split(" ");

  var parser = document.createElement('a');
  parser.href = Result.url;
  var siteName=parser.hostname;
  const ORIGIN=parser.origin;

  targetWords.forEach(Word => {
  var regEx=new RegExp(Word,"ig");
  Result.description=Result.description.replace(regEx,"<strong>"+Word+"</strong>")
});

  return ( 
  <div className="Result_Card" >
      <div className="Result_header">
        <a href={Result.url} target="_black">
          <div className="site_info">
            <div className="site_logo" >
              <img  src={ORIGIN + "/favicon.ico"} alt="" />
            </div>
              <div className="NameURL">
                <p className="site_Name">{siteName}</p>
                <p className="Result_URL">{Result.url}</p>
              </div>
          </div>
        <h4 className="Result_title">{Result.title}</h4>
        </a>
      </div>
    <p className="Result_description">{parse(Result.description)}</p>
  </div>
  );
}
 
export default Result;