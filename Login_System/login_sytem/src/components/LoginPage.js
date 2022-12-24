import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './LoginPage.css';
import Cookies from 'js-cookie';
import { checkLogin } from "./checkLogin";

/**
 * checks to see if the user is logged in or not and sets the state
 * if the user is logged in, redirects the user to the home page
 * 
 * @returns 
 */
function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const checkLoginStatus = async () => {
      const loggedIn = await checkLogin();
      setIsLoggedIn(loggedIn)
    };
    checkLoginStatus(); 
  }, [location]);

  useEffect(() => {
    if (isLoggedIn) {
      navigate("/")
    }
  }, [isLoggedIn]);

  function handleEmailChange(event) {
    setEmail(event.target.value);
  }

  function handlePasswordChange(event) {
    setPassword(event.target.value);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    try {
      const response = await axios.post('https://localhost:8443/user/login', {
        email,
        password,
      });
      // If the login is successful, navigate to the home page
      const jwt = response.data.jwt;
      Cookies.set("jwtToken", jwt, { path: '/' });
      navigate('/');
    } catch (e) {
      setError(e.response.data.error);
      alert("Incorrect email or password");
      setEmail('');
      setPassword('');
    }
  }

  return (
    <div className='LoginPage'>
      <form onSubmit={handleSubmit}>
        <h1 className='page-title'>Login Page</h1>
        <div className="row mb-3">
          <div className="col-sm-10">
            <input type="email" className="form-control" id="inputEmail3" value={email} onChange={handleEmailChange} 
            placeholder='Email' />
          </div>
        </div>
        <div className="row mb-3">
          <div className="col-sm-10">
            <input type="password" className="form-control" id="inputPassword3" value={password} onChange={handlePasswordChange}
            placeholder='Password' />
          </div>
        </div>
        <div className="col-sm-10">
          <div className="hyperlinks">
          <Link className="hlink" to="/createaccount">Create Account</Link>
          <br />
          <Link className="hlink" to="/">Forgot Password</Link>
          </div>
        </div>
        <button type="submit" className="btn btn-primary" id='loginButton'>Sign in</button>
      </form>
    </div>
  );
}

export default LoginPage;
