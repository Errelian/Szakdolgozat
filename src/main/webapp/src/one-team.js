import NavHeader from './navHeader';
import React, {useState, useEffect, useRef} from 'react';
import { useNavigate, useParams} from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import _ from "lodash";

function OneTeam(){

    let {id} = useParams();
    var [rerender, setRerender] = useState(0);

    const tournaments = useRef(0);
    const teamData = useRef(0);
    const toastMessage = useRef(0);
    let navigate = useNavigate();
    

    useEffect(() => {
        console.log('/api/teams/tournaments/' + id);
        fetch('/api/teams/tournaments/' + id,{
            method:'GET',
            headers: {
                'Accept': 'application/json'
            },
        }).then(response =>{
            if(response.ok){
                response.json().then(json =>{
                    
                    let tempTournament = [];
                    json.forEach(element =>{
                        tempTournament.push(element.tournament)
                    })

                    if (tempTournament.length !== 0){

                        if ( !(_.isEqual(tournaments.current, tempTournament)) || !(_.isEqual(json[0].team, teamData.current))){
                            tournaments.current = tempTournament;
                            console.log(tournaments.current);
                            teamData.current = json[0].team;
                            setRerender(!rerender);
                        }
                    }
                    else{
                        fetch('/api/teams/' +id, {
                            method:'GET',
                            headers: {
                            'Accept': 'application/json'
                            },
                        }).then(response =>{
                            if(response.ok){
                                response.json().then(json=>{
                                    if ( !_.isEqual(json, teamData.current)){
                                        teamData.current = json;
                                        setRerender(!rerender);
                                    }
                                })
                            }
                        })
                    }
                })
            }
        })
    });
    
    if (toastMessage.current !== 0){
        toast(toastMessage.current, {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            });
        toastMessage.current = 0;
    }

    let changeTeamName = (event) =>{
        event.preventDefault();

        let jsonBody = {
            "teamNameOld": teamData.current.teamName,
            "newName": event.target[0].value
        };

        fetch('/api/teams/changeName',{
            method:'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
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

    let joinTournament =(event) =>{
        event.preventDefault();
        let jsonBody = {
            "teamId": String(teamData.current.id),
            "tournamentId": String(event.target[0].value)
        };

        fetch('/api/tournament/add/team',{
            method:'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonBody),
        }).then(response =>{
            response.json().then(json =>{
                toastMessage.current = json;
                setRerender(!rerender);
            })
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

    const tournamentHandler = (
        <div className="loginForm adderForm">
            Please use this form to join tournaments.
          <form onSubmit={joinTournament} className='innerLoginForm'>
              <label>Id: </label>
              <input type="text" name="id" required />
                <br/>
                <div className="button-container">          
                    <button type="submit" className='standardButton'>Join</button>
                </div>
            
            </form>
        </div>
    )

    let userList = 0
    if (teamData.current !== 0){
        userList = teamData.current.teamMembers.map(user =>{
            return(
            <tr key={user.id}>
                <td style={{whiteSpace: 'nowrap'}}>{user.id}</td>
                <td>{user.username}</td>
            </tr>
            )
        })
    } 

    function leaveTournament(teamId, tournamentId){

        let jsonBody = {
            "teamId": String(teamId),
            "tournamentId": String(tournamentId)
        };

        fetch('/api/tournament/remove/team',{
            method:'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonBody),
        }).then(response =>{
            response.json().then(json =>{
                toastMessage.current = json;
                setRerender(!rerender);
            })
        })

    }

    function enterTournament(tournamentId){
        navigate('/one-tournament/'+tournamentId, {replace: true});
    }

    let tournamentList = 0
    if (tournaments.current !== 0){
        tournamentList = tournaments.current.map(tournament =>{
            return(
            <tr key={tournament.id}>
                <td style={{whiteSpace: 'nowrap'}}>{tournament.id}</td>
                <td>{tournament.tournamentName}</td>
                <td>{tournament.startTime}</td>

                <td>
                <div className='button-container'>
                    <button className='standardButton delete_specific_account_button' onClick={() => leaveTournament(id, tournament.id)}>Leave</button>
                </div>
                
            </td>
            <td>
                <div className='button-container'>
                    <button className='standardButton delete_specific_account_button' onClick={() => enterTournament(tournament.id)}>Navigate</button>
                </div>
            </td>
            <hr/>
            </tr>
            )
        })
    } 



      return(
      <div>
        <NavHeader/>
        <ToastContainer/>
        <div className='floatparent'>
            <div className='floatchild'>{teamInfoMenu}</div>
                
            <div className='floatchild'>{tournamentHandler}</div>
        </div>
        <table className='regionalList'>
            <thead>
                <tr>
                    <th width="50%">ID</th>
                    <th width="50">Username</th>
                </tr>
            </thead>
            <tbody>
                {userList}
            </tbody>
        </table>
        <hr/>
        <table className='tournamentList'>
            <thead>
                <tr>
                    <th width="20%">ID</th>
                    <th width="20%">Name</th>
                    <th width="20%">StartDateTime</th>
                    <th width="20%">Leave</th>
                    <th width="20%">Navigate</th>
                </tr>
            </thead>
            <tbody>
                {tournamentList}
            </tbody>
        </table>
      </div>
      )
}


export default OneTeam;