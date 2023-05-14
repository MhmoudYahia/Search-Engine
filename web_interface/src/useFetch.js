import { useEffect, useState } from "react"

const useFetch=(url)=>{
  const [isPending, setIsPending] = useState(true);
  const [data,setData]=useState(null);
  const [error,setError]=useState(null);
 
  const headers = { 'Content-Type': 'application/json' }
  let start;
  let end;
  const [time,setTime]=useState('0');
  start = window.performance.now();
  useEffect(()=>{
    fetch(url,{headers})
    .then(res=>{
      if(!res.ok){
        end = window.performance.now();
        setTime((end - start).toFixed(3));
        throw Error("Couldn't Fetch the data from the resource");
      }
      return res.json();
    })
    .then(data=>{
      end = window.performance.now();
      setTime((end - start).toFixed(3));
      setData(data);
      setIsPending(false);
      setError(null);
    })
    .catch(err=>{
      end = window.performance.now();
      setTime((end - start).toFixed(3));
      setIsPending(false);
      setError(err.message);
    }
    )
  },[url]);
    return{data,isPending,error,time,setIsPending};
}

export default useFetch;