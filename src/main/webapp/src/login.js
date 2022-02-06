import React, {useState} from 'react';
import { useNavigate} from 'react-router-dom';  

function Login(){
    let handleSubmit = (event) => {
        event.preventDefault();
        let data = new FormData(event.target)
        sendLogin(data)
      };

      let navigate = useNavigate();

      let [error, setError] = useState();

      function registerRedirect(){
          navigate("/register", {replace: true})
      }

      if(error == 1){
          var errorMsg = <h5 style={{colour: "red"}}>Invalid username or password</h5>
          console.log(errorMsg)
      }

    const renderForm = (
        
            <div className="loginForm">
                <h5>Please Log in. If you don't have an account yet, press the register button.</h5>
              <form onSubmit={handleSubmit} className='innerLoginForm'>
                  <label>Username: </label>
                  <input type="text" name="username" required />
                <br/>
                  <label>Password: </label>
                  <input type="password" name="password" required />
                <br/>
                <div className="button-container">          
                  <button type="submit" className='standardButton buttonLogin'>Login</button>
                  <button className='standardButton buttonRegister' onClick={registerRedirect}>Register</button>
                </div>
                
              </form>
              {errorMsg}
              </div>
     );

     //console.log(errorMsg)

     function sendLogin(data){
        fetch("http://localhost:8080/api/login-process",{
            method: "POST",
            body: new URLSearchParams(data)
        })
        .then(v => {
            if(v.redirected) window.location = v.url
            console.log(v.url)
            if(v.url.includes("error")){
                setError(1)
                console.log(error)
            }
        })
        .catch(e => console.warn(e))
    };
        return renderForm

}
export default Login;