import React from "react";
import parse from 'html-react-parser'
import './ResultCard.css'

const Result = (props) => {
  const searchText ="eaque consectetur exercitationem";
  const targetWords=searchText.split(" ");
  const Result = props.result;
  targetWords.forEach(Word => {
  Result.description=Result.description.replace(Word,"<strong>"+Word+"</strong>")
});
  return ( 
  <div className="Result_Card">
      <div className="Result_header">
        <a href={Result.url}>
          <div className="site_info">
            <div className="site_logo" >
              <img  src={Result.url + "/favicon.ico"} alt="" />
            </div>
              <div className="NameURL">
                <span className="site_Name">{Result.siteName}</span>
                <span className="Result_URL">{Result.url}</span>
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