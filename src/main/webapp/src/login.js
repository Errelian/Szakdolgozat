import React, { Component } from 'react';
import { Link } from 'react-router-dom';

class Login extends Component{
    constructor(props){
        super(props);
    }

    handleSubmit = (event) => {
        //this.sendLogin;
        event.preventDefault();
        let data = new FormData(event.target)
        console.log(data.get("username"))
        console.log(data.get("password"))
        this.sendLogin(data)
      };


    renderForm = (
        <div className="form">
          <form onSubmit={this.handleSubmit}>
            <div className="input-container">
              <label>Username: </label>
              <input type="text" name="username" required />
            </div>
            <div className="input-container">
              <label>Password: </label>
              <input type="password" name="password" required />
            </div>
            <div className="button-container">          
              <button type="submit">Login</button>
            </div>
          </form>
          <div className='button-container'>
            <button color="green"><Link to="/self-user">Register</Link></button>
          </div>
        </div>
     );

     //TODO TOMORROW ASSEMBLE FORMDATA <
     //ADD BUTTON THAT REDIRECTS TO REGISTER PAGE <
     //SEND IT TO SPRING
     //MAKE SURE IT WORKS
     //IF YES, CELEBRATE
     //ALSO, ADD BOOTSTRAP TO THE PROJECT OR SOMETHING TO STYLE IT
     //ADD ERROR HANDLING TO IT
    //data = new FormData(this.login)

    sendLogin(data){
        fetch("http://localhost:8080/api/login-process",{
            method: "POST",
            body: new URLSearchParams(data)
        })
        .then(v => {
            if(v.redirected) window.location = v.url
        })
        .catch(e => console.warn(e))
    }

    render(){
        return (this.renderForm)
    }
}
export default Login;