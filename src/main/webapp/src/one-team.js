import NavHeader from './navHeader';
import React, {useState, useEffect, useRef} from 'react';
import { useNavigate, useParams} from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import _ from "lodash";

function OneTeam(){

    let {id} = useParams();
    var [rerender, setRerender] = useState(0);
    //console.log(id);
    const tournaments = useRef(0);
    const teamData = useRef(0);
    const toastMessage = useRef(0);
    

    useEffect(() => {
        fetch('/api/teams/tournaments/' + id,{
            method:'GET',
            headers: {
                'Accept': 'application/json'
            },
        }).then(response =>{
            if(response.ok){
                response.json().then(json =>{
                    //console.log(json);
                    

                    let tempTournament = [];
                    json.forEach(element =>{
                        tempTournament.push(element.tournament)
                    })

                   if ( !(_.isEqual(tournaments.current, tempTournament)) || !(_.isEqual(json[0].team, teamData.current))){
                        tournaments.current = tempTournament;
                        teamData.current = json[0].team;
                        setRerender(!rerender);
                   }
                })
            }
        })
      });

      let changeTeamName = (event) =>{
        event.preventDefault();

        let jsonBody = {
            "teamNameOld": teamData.current.teamName,
            "newName": event.target[0].value
        };

        fetch('/api/teams/changeName',{
            method:'GET',
            headers: {
                'Accept': 'application/json'
            },
            body: JSON.stringify(jsonBody),
        }).then(response =>{
            if (response.ok){
                setRerender(!rerender);
            }else{
                response.json().then(json =>{
                    toast(json, {
                        position: "top-right",
                        autoClose: 5000,
                        hideProgressBar: true,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                        });
                })
                
            }
        })

      }
      let teamInfoMenu = 0

      if (teamData.current !== 0){
      teamInfoMenu =(
        <div className='userInfo loginForm adderForm'>
            <p className='greeting'>TeamId: {teamData.current.id}</p>
            <p className='id'>TeamName: {teamData.current.teamName}</p>
            <p className='id'>CreatorId: {teamData.current.creatorId}</p>
            <form onSubmit={changeTeamName} className='delete_account_form'>
                <label>New name: </label>
                <input type='text' name='name' required></input>
                <button type='submit' className='standardButton'>Rename team</button>
            </form>
        </div>
      )
      }


      return(
      <div>
        <NavHeader/>
        <ToastContainer/>
        <div className='floatparent'>
                    <div className='floatchild'>{teamInfoMenu}</div>
                
                    <div className='floatchild'>0</div>
                </div>
        <h1>placeholder</h1>
      </div>
      )
}


export default OneTeam;