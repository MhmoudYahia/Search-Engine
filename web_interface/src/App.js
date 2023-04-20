import './App.css';
import Home from './components/Home';
  import Result from './components/result';
  import ResultsPage from './components/ResultsPage';
function App() {
  const Results=[] ;
  for(let i=0; i<15; i++){
    Results[i] = {
      url: "https://geeksforgeeks.org/",
      title: "How to create a multithreaded search engine?",
      siteName: "GeeksforGeeks",
      description: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Eos enim exercitationem saepe quisquam id nam vel totam minima iure nemo vitae omnis eaque, numquam assumenda quasi molestias adipisci tempora placeat."
    }
  }
  return (
    <div className="App" style={{height:"100vh"}}>
      <Home></Home>   
    </div>
  );
}

export default App;
