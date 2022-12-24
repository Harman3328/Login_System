import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './CreateAccount.css'

/**
 * sends a request to the server with an email and password
 * if no errors occured, the account was created and redirects user to the login page
 * @returns creates the create account page 
 */
function CreateAccount() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setComfirmPassword] = useState('');
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  function handleEmailChange(event) {
    setEmail(event.target.value);
  }

  function handlePasswordChange(event) {
    setPassword(event.target.value);
  }

  function handleComfirmPasswordChange(event) {
    setComfirmPassword(event.target.value);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    if (!email || !password) {
      alert("Invalid Username or Password")
    } else if (password !== confirmPassword) {
        alert("Passwords don't match")
    } else {
      try { 
        const response = await axios.post('https://localhost:8443/user/add', {
          email,
          password,
        });
        // If the login is successful, navigate to the home page
        if (response.data) {
          alert("Account Created")
          navigate('/login');
        } else {
          alert("Email already exists")
          setEmail('');
          setPassword('');
          setComfirmPassword(''); 
        }
      } catch (e) {
        setError(e.response.data.error);
      }
    }
  }

  return (
    <div className='CreateAccountForm'>
    <form onSubmit={handleSubmit}>
      <h1 className='page-title'>Create Account Page</h1>
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
      <div className="row mb-3">
        <div className="col-sm-10">
          <input type="password" className="form-control" id="inputPassword4" value={confirmPassword} onChange={handleComfirmPasswordChange}
           placeholder='Comfirm Password' />
        </div>
      </div>
      <button type="submit" className="btn btn-primary" id='loginButton'>Create Account</button>
    </form>
  </div>
  );
}

export default CreateAccount;
