import DateTime from 'react-datetime';
import React, {useRef} from 'react';
import NavHeader from './navHeader';
import { ToastContainer, toast } from 'react-toastify';
import "react-datetime/css/react-datetime.css";

function CreateTournament(){
    const moment = require('moment');
    moment.locale('en');

    const dateTime = useRef(0);

    let handleCreate =(event) =>{
        event.preventDefault();

        let input =[event.target[0].value, event.target[1].value, event.target[2].value,];

        let jsonBody = 
        {
            "tournamentName": input[0],
            "startTime": dateTime.current,
            "regionId": input[1]
        };

        fetch('/api/tournament/create',{
            method:'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body:JSON.stringify(jsonBody),
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
            })
        })
    }

    const handleChange = (e) =>{
        const valueOfInput = moment(e.toDate()).format('YYYY-MM-DD HH:mm');
     
        dateTime.current = valueOfInput;

        return valueOfInput;
    }

    const tournamentCreater = (
        <div className="loginForm adderForm">
            Please use this form to add additional regional accounts to your account.
            <form onSubmit={handleCreate} className='innerLoginForm'>
                <label>Name: </label>
                <input type="text" name="name" required />
                <br/>
                <label>Region: </label>
                <input type="text" name="region" required />
                <br/>
                <DateTime name= 'dateTimePicker' locale='en' dateFormat='YYYY-MM-DD' timeFormat='HH:mm' onChange={(changedVal) => handleChange(changedVal)}/>
                <br/>
                <div className="button-container">          
                    <button type="submit" className='standardButton'>Create new tournament.</button>
                </div>
            
            </form>
        </div>
    )
    
    return(
        <div>
            <NavHeader/>
            <ToastContainer/>
            {tournamentCreater}
        </div>
    )

}

export default CreateTournament