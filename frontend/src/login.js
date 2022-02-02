import React, { Component } from 'react';

class Login extends Component{
    constructor(props){
        super(props);
    }

    handleSubmit = (event) => {
        //this.sendLogin;
        event.preventDefault();
        console.log(event.target[0].value)
        console.log(event.target[1].value)
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
        </div>
     );

     //TODO TOMORROW ASSEMBLE FORMDATA
     //SEND IT TO SPRING
     //MAKE SURE IT WORKS
     //IF YES, CELEBRATE
     //ALSO, ADD BOOTSTRAP TO THE PROJECT OR SOMETHING TO STYLE IT
    //data = new FormData(this.login)

    sendLogin(data){
        fetch("http://localhost:8080/login-process",{
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