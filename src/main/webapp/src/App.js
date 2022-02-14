import './App.css';
import Login from "./login";
import Register from "./register";
import NavHeader from './navHeader';
import SelfUser from './self-user';
import CreateTeam from './create-team';
import SelfTeams from './self-teams';
import OneTeam from './one-team';
import AllTeams from './all-teams';
import CreateTournament from './create-tournament';
import AllTournaments from './all-tournaments';
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
                <Route path='' element={<SelfUser/>}/>
                <Route path='/new-team' element={<CreateTeam/>}/>
                <Route path='/self-teams' element={<SelfTeams/>}/>
                <Route path='/one-team/:id' element={<OneTeam/>}/>
                <Route path='/teams' element={<AllTeams/>}/>
                <Route path='/create-tournament' element={<CreateTournament/>}/>
                <Route path='/all-tournaments' element ={<AllTournaments/>}/>
              </Routes>
        </BrowserRouter>
    );
}

export default App;
