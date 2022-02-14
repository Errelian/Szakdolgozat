import React, {useState, useEffect} from 'react';
import _ from "lodash";
import NavHeader from './navHeader';
import { useNavigate} from 'react-router-dom';

function AllTournaments(){

    var [tournaments, setTournaments] = useState(0);
    let navigate = useNavigate();

    useEffect(() => {
        fetch('/api/tournament/get/all',{
            method:'GET',
            headers: {
                'Accept': 'application/json'
            },
        }).then(response =>{
            if(response.ok){
                response.json().then(json =>{

                    console.log(json);
                    if ( !(_.isEqual(json, tournaments)) ){
                        setTournaments(json);
                        console.log("Rerendered.");
                    }
                })
            }
        })
    });

    function enterTournament(tournamentId){
        navigate('/one-tournament/'+tournamentId, {replace: true});
    }

    let tournamentList = 0;
    if(tournaments !== 0){
        tournamentList = tournaments.map(tournament =>{
            return(
            <tr key={tournament.id}>
                <td style={{whiteSpace: 'nowrap'}}>{tournament.id}</td>
                <td>{tournament.tournamentName}</td>
                <td>{tournament.startTime}</td>
                <td>{tournament.victorId}</td>
                <td>{tournament.regionId}</td>
                <td>{tournament.creatorId}</td>
                
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
          <hr/>
          <table className='tournamentList'>
              <thead>
                  <tr>
                      <th width="15%">ID</th>
                      <th width="15%">Name</th>
                      <th width="15%">StartTime</th>
                      <th width="15%">VictorId</th>
                      <th width="10%">RegionId</th>
                      <th width="15%">CreatorId</th>
                      <th width="15%">Navigate</th>
                  </tr>
              </thead>
              <tbody>
                  {tournamentList}
              </tbody>
          </table>
        </div>
        )
}

export default AllTournaments