import './App.css';
import Login from "./login";
import Register from "./register";
import NavHeader from './navHeader';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import React, { Component } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

class Test extends React.Component{
    render(){
        return(<h1>ezaz</h1>)
    }
}


function App() {
    return (
        <BrowserRouter>
              <Routes>
                <Route path='/ui/self-user' element={<Test/>}/>
                <Route path='/login*' element={<Login/>}/>
                <Route path='/register' element={<Register/>}/>
                <Route path='/headertest' element={<NavHeader/>}/>
              </Routes>
        </BrowserRouter>
    );
}

export default App;
