import React, {useState, useEffect} from 'react';
import _ from "lodash";
import NavHeader from './navHeader';
import { useNavigate} from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';

function AllTeams(){

    var [teams, setTeams] = useState(0);
    let navigate = useNavigate();


    useEffect(() => {
        fetch('/api/teams/all/',{
            method:'GET',
            headers: {
                'Accept': 'application/json'
            },
        }).then(response =>{
            if(response.ok){
                response.json().then(json =>{

                    console.log(json);
                    if ( !(_.isEqual(json, teams)) ){
                        setTeams(json);
                        console.log("Rerendered.");
                    }
                })
            }
        })
    });

    function enterTeam(teamId){
        navigate('/one-team/'+teamId, {replace: true});
    }

    function joinTeam(teamId){
        let jsonBody =
        {
            teamId
        }

        fetch('/api/teams/add',{
            method:'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonBody),
        }).then(response =>{
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
            }
            )
        })
    }

    let teamList = 0;
    if(teams !== 0){
        teamList = teams.map(team =>{
            return(
            <tr key={team.id}>
                <td style={{whiteSpace: 'nowrap'}}>{team.id}</td>
                <td>{team.teamName}</td>
                <td>{team.creatorId}</td>

                <td>
                <div className='button-container'>
                    <button className='standardButton delete_specific_account_button' onClick={() => joinTeam(team.id)}>Join</button>
                </div>
                
            </td>
            <td>
                <div className='button-container'>
                    <button className='standardButton delete_specific_account_button' onClick={() => enterTeam(team.id)}>Navigate</button>
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
          <hr/>
          <table className='tournamentList'>
              <thead>
                  <tr>
                      <th width="20%">ID</th>
                      <th width="20%">Name</th>
                      <th width="20%">CreatorId</th>
                      <th width="20%">Join</th>
                      <th width="20%">Navigate</th>
                  </tr>
              </thead>
              <tbody>
                  {teamList}
              </tbody>
          </table>
        </div>
        )
}

export default AllTeams;