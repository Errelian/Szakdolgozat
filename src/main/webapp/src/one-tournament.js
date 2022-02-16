import NavHeader from './navHeader';
import React, {useState, useEffect, useRef} from 'react';
import { useNavigate, useParams} from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import testArray from './tester';


function OneTournament(){

    let {id} = useParams();
    var [rerender, setRerender] = useState(false);
    const currentStanding = useRef(0);
    const currentTournament = useRef(0);
    const toastMessage = useRef(0);

    useEffect(() => {
        console.log('/api/tournament/get/teams/' + id);
        fetch('/api/tournament/get/teams/' + id,{
            method:'GET',
            headers: {
                'Accept': 'application/json'
            },
        }).then(response => {
            if (response.ok){
                response.json().then(json =>{
                    currentStanding.current = json;
                    //console.log(currentStanding.current);
                })
            }
        })
        fetch('/api/tournament/get/' + id,{
            method:'GET',
            headers:{
                'Accept': 'application/json'
            },
        }).then(response => {
            if(response.ok){
                response.json().then(json =>{
                    currentTournament.current = json;
                    //console.log(currentTournament.current);
                })
            }
        })

    })


    currentTournament.current = {
          "id": 44,
          "tournamentName": "algoTestTournament",
          "startTime": "2022-02-23 00:00",
          "victorId": null,
          "regionId": "EUN",
          "creatorId": 20,
          "updating": false
    };
    console.log(currentTournament.current);

    currentStanding.current = testArray();
    console.log(currentStanding.current);

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
    };

    function eliminateTeam(teamId, eliminationRound){

        let jsonBody ={
            "eliminationRound": eliminationRound,
            "teamId": teamId,
            "tournamentId": id
        }

        fetch('/api/tournament/modify/team',{
            method:'PUT',
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

    function declareVictor(victorId){

        let jsonBody ={
            "victorId": victorId,
            "tournamentId": id
        }

        fetch('/api/tournament/change/victor',{
            method:'PUT',
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

    function deleteTournament(){
        let jsonBody ={
            "tournamentId": id
        }

        fetch('/api/tournament/delete',{
            method:'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonBody),
        })
    }

    function seedTournament(){
        fetch('/api/seeding/'+ id,{
            method:'PUT',
            headers: {
                'Accept': 'application/json',
            },
        })
    }

    function updateRanking(){
        fetch('/api/update/rankings'+ id,{
            method:'PUT',
            headers: {
                'Accept': 'application/json',
            },
        })
    }

    let tournamentInfo = 0;
    if (currentTournament.current !== 0){
        tournamentInfo =(
        <div className='userInfo loginForm adderForm'>
            <p className='greeting'>TournamentID: {currentTournament.current.id}</p>
            <p className='id'>TournamentName: {currentTournament.current.tournamentName}</p>
            <p className='id'>CreatorID: {currentTournament.current.creatorId}</p>
            <p className='id'>StartTime: {currentTournament.current.startTime}</p>
            <p className='id'>RegionID: {currentTournament.current.regionId}</p>
        </div>
        )
    }

    let tournamentMenu = (
        <div className = 'loginForm adderForm'>
            <button className='standardButton' onClick={() => deleteTournament()}>Delete</button>
            <button className='standardButton' onClick={() => seedTournament()}>Seed</button>
            <button className='standardButton' onClick={() => updateRanking()}>Update Ranks</button>
        </div>
    )

    return (
    <div>
        <NavHeader/>
        <ToastContainer/>
        <div className='floatparent'>
            <div className='floatchild'>{tournamentInfo}</div>
                
            <div className='floatchild'>{tournamentMenu}</div>
        </div>
        
    </div>
    )

}

export default OneTournament