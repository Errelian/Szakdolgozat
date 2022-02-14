import NavHeader from './navHeader';
import React, {useState, useEffect, useRef} from 'react';
import { useNavigate, useParams} from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';


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
                    console.log(currentStanding.current);
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
                })
            }
        })

    })

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

    return (<div>hihi haha</div>)

}

export default OneTournament