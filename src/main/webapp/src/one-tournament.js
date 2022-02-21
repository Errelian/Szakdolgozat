import NavHeader from './navHeader';
import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import testArray from './tester';
import _ from "lodash";


function OneTournament() {

    let { id } = useParams();
    var [rerender, setRerender] = useState(false);
    const [standing, setStanding] = useState([]);
    const currentTournament = useRef(0);
    const toastMessage = useRef(0);

    useEffect(() => {
        //console.log('/api/tournament/get/teams/' + id);
        fetch('/api/tournament/get/teams/' + id, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            },
        }).then(response => {
            if (response.ok) {
                response.json().then(json => {
                    if (!(_.isEqual(json, standing))){
                        setStanding(json);
                    }
                    //console.log(currentStanding.current);
                })
            }
        })
        fetch('/api/tournament/get/' + id, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            },
        }).then(response => {
            if (response.ok) {
                response.json().then(json => {
                    if(currentTournament.current === 0){
                        currentTournament.current = json;
                        setRerender(!rerender);
                    }
                    //console.log(currentTournament.current);
                })
            }
        })

    })

    //console.log(currentTournament.current);
    console.log(standing)

    if (toastMessage.current !== 0) {
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

    function eliminateTeam(teamId, eliminationRound) {

        let jsonBody = {
            "eliminationRound": eliminationRound,
            "teamId": teamId,
            "tournamentId": id
        }

        fetch('/api/tournament/modify/team', {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonBody),
        }).then(response => {
            response.json().then(json => {
                toastMessage.current = json;
                setRerender(!rerender);
            })
        })
    }

    function declareVictor(victorId) {

        let jsonBody = {
            "victorId": victorId,
            "tournamentId": id
        }

        fetch('/api/tournament/change/victor', {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonBody),
        }).then(response => {
            response.json().then(json => {
                toastMessage.current = json;
                setRerender(!rerender);
            })
        })
    }

    function deleteTournament() {
        let jsonBody = {
            "tournamentId": id
        }

        fetch('/api/tournament/delete', {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonBody),
        })
    }

    function seedTournament() {
        fetch('/api/seeding/' + id, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
            },
        })
    }

    function updateRanking() {
        fetch('/api/update/rankings/' + id, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
            },
        })
    }

    let tournamentInfo = 0;
    if (currentTournament.current !== 0) {
        tournamentInfo = (
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
        <div className='loginForm adderForm'>
            <button className='standardButton' onClick={() => deleteTournament()}>Delete</button>
            <button className='standardButton' onClick={() => seedTournament()}>Seed</button>
            <button className='standardButton' onClick={() => updateRanking()}>Update Ranks</button>
        </div>
    )

    console.log(standing)

    let standings_array = [];
    if (standing !== []) {
        standings_array = standing;
    }

    console.log(standing)

    function col_content(round, stand_arr) {
        let arrayIndex = round - 1;
        let col_content = '';
        let output = '';

        if (stand_arr.length > arrayIndex) {

            col_content = stand_arr[arrayIndex];
            output = col_content.map(team => {
                if (team.team !== null){
                    let name = team.team.teamName;
                    let id = team.team.id;
                    
                    return (
                        <div className="team_wrapper" key={id}>
                            <div className='team_name'>{name}</div>
                            <div className='team_id'>{id}</div>
                            <button onClick={() => eliminateTeam(id, round)}>Declare Loss</button>
                            <button onClick={() => declareVictor(id.id)}>Declare Win</button>
                        </div>
                    )
                }
            })
        }
        return output;
    }


    return (
        <div>
            <NavHeader />
            <ToastContainer />
            <div className='floatparent'>
                <div className='floatchild'>{tournamentInfo}</div>
                <div className='floatchild'>{tournamentMenu}</div>
            </div>
            <div className='standings_wrapper'>
                <div className='column column_1'>
                    {col_content(1, standings_array)}
                </div>
                <div className='column column_2'>
                    {col_content(2, standings_array)}
                </div>
                <div className='column column_3'>
                    {col_content(3, standings_array)}
                </div>
                <div className='column column_4'>
                    {col_content(4, standings_array)}
                </div>
                <div className='column column_5'>
                    {col_content(5, standings_array)}
                </div>
            </div>

        </div>
    )
}

export default OneTournament