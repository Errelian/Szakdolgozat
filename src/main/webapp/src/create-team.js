import React from 'react';

import NavHeader from './navHeader';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


function CreateTeam(){


    let handleAdd = (event) =>{
        event.preventDefault();
        let jsonBody ={
            "teamName": event.target[0].value,
        }

        fetch('http://localhost:8080/api/teams/create',{
            method:'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body:JSON.stringify(jsonBody),
        }).then(response=>{
            response.json().then( json=>{
                toast(json, {
                    position: "top-right",
                    autoClose: 5000,
                    hideProgressBar: true,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    })
                }
            )
    })
}


    return (
        <div>
            <NavHeader/>
            <div className="loginForm">
                    <h5>You can use the field below to create a new team.</h5>
                  <form onSubmit={handleAdd} className='innerLoginForm'>
                      <label>Team name: </label>
                      <input type="text" name="teamName" required />
                    <br/>
                    <div className="button-container">          
                      <button type="submit" className='standardButton buttonLogin'>Sign up</button>
                    </div>

                  </form>
                  <ToastContainer
                    position="top-right"
                    autoClose={5000}
                    hideProgressBar
                    newestOnTop={false}
                    closeOnClick
                    rtl={false}
                    pauseOnFocusLoss
                    draggable
                    pauseOnHover
                    />
                  </div>
        </div>
    )

}

export default CreateTeam;