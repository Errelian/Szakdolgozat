
import React, {useEffect, useState, useRef} from 'react';
import { useNavigate} from 'react-router-dom';
import NavHeader from './navHeader';
import { ToastContainer, toast } from 'react-toastify';

function SelfTeams(){

    var [teams, setTeams] = useState(0);
    let navigate = useNavigate();

    const toastMessage = useRef(0);


    useEffect(() => {

        fetch('/api/users/self/teams',{
            method:'GET',
            headers: {
                'Accept': 'application/json'
            },
        }).then(response =>{
            if(response.ok){
                response.json().then(json =>{
                    console.log(json);
                    setTeams(json);
                })
            }
        })
      }, []);


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

    function leaveTeam(id){
        let jsonBody = {"teamId": id};

        fetch('/api/teams/remove',{
            method:'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonBody),
        }).then(response =>{
            response.json().then( json =>{
                if(response.ok){
                    let updatedTeams = [...teams].filter(i => i.id !== id);
                    let teamsTemp = teams;
                    teamsTemp.regionalAccounts = updatedTeams;
                    toastMessage.current = json;
                    setTeams(updatedTeams);
            }})
        })
    }

    function teamRedirect(id){
        navigate('/one-team/' + id, {replace: true})
    }

    let teamList = 0;
    if(teams !== 0){
        teamList = teams.map(team =>{
            return <tr key={team.id}>
            <td style={{whiteSpace: 'nowrap'}}>{team.id}</td>
            <td>{team.teamName}</td>
            <td>{team.creatorId}</td>
            <td>
                <div className='button-container'>
                    <button className='standardButton delete_specific_account_button' onClick={() => teamRedirect(team.id)}>Enter</button>
                    <button className='standardButton delete_specific_account_button' onClick={() => leaveTeam(team.id)}>Leave</button>
                </div>
            </td>
        </tr>
        })
    }

    return (
    <div>
        <NavHeader/>
        <ToastContainer/>
        <table className='regionalList'>
            <thead>
                <tr>
                    <th width="20%">Id</th>
                    <th width="20%">Name</th>
                    <th width="20%">CreatorId</th>
                    <th width="40%">Actions</th>
                </tr>
            </thead>
            <tbody>
                {teamList}
            </tbody>
        </table>
    </div>
    )

}

export default SelfTeams;