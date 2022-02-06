import NavHeader from './navHeader';
import React, {useState, useEffect} from 'react';
import { ToastContainer, toast } from 'react-toastify';


function SelfUser(){

    var [info, setInfo] = useState(0);

    useEffect(() => {

        fetch('http://localhost:8080/api/users/self',{
            method:'GET',
            headers: {
                'Accept': 'application/json'
            },
        }).then(response =>{
            if(response.ok){
                response.json().then(json =>{
                    setInfo(json)
                })
            }
        })
      }, []);

    let handleDelete = (event) =>{
        const input = event.target[0].value
        let jsonBody = {"password": input}


        fetch('http://localhost:8080/api/users/delete',{
            method:'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonBody),
        }).then(response =>{
            if(response.ok){
                toast("Successfully deleted account.", {
                    position: "top-right",
                    autoClose: 5000,
                    hideProgressBar: true,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    })
            }
            
                response.json().then(json=>{
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
            
        })

    }

    let handleAdd = (event) =>{
        const input = [event.target[0].value, event.target[1].value]

        let jsonBody = {
            "regionId": input[0],
            "inGameName":input[1]
        }

        fetch('http://localhost:8080/api/regionalAccounts/update',{
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

    let userGreeter = (
        <div classname='userInfo'>
            Hello, {info.username}!
            <br/>
            Your id: {info.id}
            <br/>
            <form onSubmit={handleDelete}>
                <label>Password: </label>
                <input type='password' name='password'></input>
                <button type='submit' className='standardButton'>Delete account</button>
            </form>
        </div>
        )
        
    let regionAccountAdder = (
        <div className="loginForm">
            Please use this form to add additional regional accounts to your account.
          <form onSubmit={handleAdd} className='innerLoginForm'>
              <label>Region </label>
              <input type="text" name="region" required />
            <br/>
              <label>Name: </label>
              <input type="text" name="name" required />
            <br/>
            <div className="button-container">          
              <button type="submit" className='standardButton'>Add new Account.</button>
            </div>
            
            </form>
        </div>
    )

    return(
        <div>
            <NavHeader/>
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

                {userGreeter}
                {regionAccountAdder}
        </div>
    )

}

export default SelfUser;