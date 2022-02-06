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
            else{
                toast("Password was incorrect.", {
                    position: "top-right",
                    autoClose: 5000,
                    hideProgressBar: true,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    }) 
            }
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
            <h1>Test</h1>
        </div>
    )

}

export default SelfUser;