import Result from "./result";
import './ResultsPage.css'
const ResultsPage = (props) => {
  const results=props.Results;
  return (
  <div className="Results_Container">
    {
    results.map((result,idx) => (
    <Result key={idx} result={result}></Result>))
    }
  </div> 
   );
}
 
export default ResultsPage;