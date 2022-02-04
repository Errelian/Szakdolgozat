import './App.css';
import Login from "./login";
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import React, { Component } from 'react';

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
              </Routes>
        </BrowserRouter>
    );
}

export default App;
