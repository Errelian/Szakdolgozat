import React, {useState} from 'react';
import { useNavigate} from 'react-router-dom';  

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function Register(){
    let navigate = useNavigate();

    let handleSubmit = (event) => {
        event.preventDefault();

        const input = [event.target[0].value, event.target[1].value, event.target[2].value]
        let jsonBody = {
            "username": input[0],
            "password":input[1],
            "eMail": input[2]
        }
        


        fetch('http://localhost:8080/api/users/register',{
            method:'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body:JSON.stringify(jsonBody),
        })
        .then(response =>{
            if(!response.ok){
                response.json().then(json =>{
                    console.log("asd")
                    toast(json, {
                        position: "top-right",
                        autoClose: 5000,
                        hideProgressBar: true,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                        })
                })
            }else{
                navigate("../login", {replace: true});
            }

        })
    }


    const registerForm = (
        <div className="loginForm">
                <h5>Please use the below fields to create an account if you dont have one yet.</h5>
              <form onSubmit={handleSubmit} className='innerLoginForm'>
                  <label>Username: </label>
                  <input type="text" name="username" required />
                <br/>
                  <label>Password: </label>
                  <input type="password" name="password" required />
                <br/>
                <label>E-mail: </label>
                  <input type="email" name="eMail" required />
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
    )

    

        return (registerForm);  
}

    
      
export default Register;