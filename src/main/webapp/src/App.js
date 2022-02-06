import './App.css';
import Login from "./login";
import Register from "./register";
import NavHeader from './navHeader';
import SelfUser from './self-user';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';


function App() {
    return (
        <BrowserRouter>
              <Routes>
                <Route path='/login*' element={<Login/>}/>
                <Route path='/register' element={<Register/>}/>
                <Route path='/headertest' element={<NavHeader/>}/>
                <Route path='/self-user' element={<SelfUser/>}/>
              </Routes>
        </BrowserRouter>
    );
}

export default App;
