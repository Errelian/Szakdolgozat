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
                    console.log(json);
                    setInfo(json);
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
        
    const regionAccountAdder = (
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

    function removeRegionalAcccount(name, regionId){

        let jsonBody ={
            "regionId": regionId,
            "inGameName": name
        }

        fetch('http://localhost:8080/api/regionalAccounts/delete',{
            method:'DELETE',
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

                    if(response.ok && info !== 0){
                        let updatedRegionalAccounts = [...info.regionalAccounts].filter(i => i.regionId !== regionId);
                        let infoTemp = info;
                        infoTemp.regionalAccounts = updatedRegionalAccounts;

                        setInfo(infoTemp);
                    }
                }
            )
        })

    }
    let regionalAccountList = 0
    if (info !== 0){
        regionalAccountList = info.regionalAccounts.map(regionalAccount =>{
            return  <tr key={regionalAccount.inGameName}>
            <td style={{whiteSpace: 'nowrap'}}>{regionalAccount.inGameName}</td>
            <td>{regionalAccount.regionId}</td>
            <td>
                <div classname='button-container'>
                    <button className='standardButton' onClick={() => removeRegionalAcccount(regionalAccount.inGameName, regionalAccount.regionId)}>Delete</button>
                </div>
            </td>
        </tr>
        })
    }
        

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
                {regionalAccountList}
        </div>
    )

}

export default SelfUser;