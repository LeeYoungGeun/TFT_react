import {useEffect, useState} from "react";
import axios from "axios";

import logo from './logo.svg';
import './App.css';

function App() {

  const [hello, setHello] = useState('');

  useEffect(() => {
    axios.get('http://localhost:8090/api/test')
        .then((res) => {
          console.log(res.data);
          setHello(res.data);
        })
  }, []);
  return (
      <div className="App">
        백엔드 데이터 : {hello}
      </div>
  );
}

export default App;
