import React from "react";
import parse from 'html-react-parser'
import './ResultCard.css'

const Result = (props) => {
  var searchText = props.target;
  const Result = props.result;
  searchText = searchText.replaceAll("\"", "");
  const targetWords = searchText.split(" ");
  var displayText =Result.paragraph?Result.paragraph:Result.description;
  var parser = document.createElement('a');
  parser.href = Result.url;
  var siteName=parser.hostname;
  const ORIGIN=parser.origin;

  targetWords.forEach(Word => {
  Word="\\b"+Word+"\\b";
  var regEx=new RegExp(Word,"ig");
    displayText = displayText.replace(regEx,(match)=>"<strong>"+match+"</strong>")
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
              <p className="Result_URL" style={{ overflow: 'hidden', maxWidth: "500px", textOverflow: 'ellipsis' }}>{Result.url.substring(0, 70)}{Result.url.length>70?"...":""}</p>
              </div>
          </div>
        <h4 className="Result_title">{Result.title}</h4>
        </a>
      </div>
      <p className="Result_description" style={{ overflow: 'hidden', textOverflow: 'ellipsis' }} >{parse(displayText.substring(0, 320))} {displayText.length>320?"...":""}</p>
  </div>
  );
}
 
export default Result;