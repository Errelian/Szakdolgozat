import NavHeader from './navHeader';
import React, {useState, useEffect} from 'react';
import { ToastContainer, toast } from 'react-toastify';


function SelfUser(){

    var [info, setInfo] = useState(0);

    var [updated, setUpdated] = useState(false);

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
        event.preventDefault();
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
        event.preventDefault();
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
                    setUpdated(!updated)
                }
                
            )
        })
    }

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

    let userGreeter = (
        <div className='userInfo loginForm adderForm'>
            <p className='greeting'>Hello, {info.username}!</p>
            <p className='id'>Your id: {info.id}</p>
            <form onSubmit={handleDelete} className='delete_account_form'>
                <label>Password: </label>
                <input type='password' name='password' required></input>
            
                <button type='submit' className='standardButton'>Delete account</button>
            </form>
        </div>
        )
        
    const regionAccountAdder = (
        <div className="loginForm adderForm">
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

    let regionalAccountList = 0
    if (info !== 0){
        regionalAccountList = info.regionalAccounts.map(regionalAccount =>{
            return  <tr key={regionalAccount.inGameName}>
            <td style={{whiteSpace: 'nowrap'}}>{regionalAccount.inGameName}</td>
            <td>{regionalAccount.regionId}</td>
            <td>
                <div className='button-container'>
                    <button className='standardButton delete_specific_account_button' onClick={() => removeRegionalAcccount(regionalAccount.inGameName, regionalAccount.regionId)}>Delete</button>
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
                <div className='floatparent'>
                    <div className='floatchild'>{userGreeter}</div>
                
                    <div className='floatchild'>{regionAccountAdder}</div>
                </div>
                <table className='regionalList'>
                    <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="20%">Region</th>
                            <th width="40%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {regionalAccountList}
                    </tbody>
                </table>
        </div>
    )

}

export default SelfUser;