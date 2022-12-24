import React, { useEffect } from "react";
import axios from 'axios';
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';

function LogoutPage () {
    const navigate = useNavigate();
    async function signOut() {
        try {
            const jwtToken = Cookies.get("jwtToken");
            const response = await axios.post('https://localhost:8443/user/logout', jwtToken, {
              headers: {
                'Content-Type': 'text/plain'
              }
            });
            const jwt  = response.data.loggedOut; 
            Cookies.set("jwtToken", jwt, { path: '/' });
            navigate('/login');
        } catch (e) {
    
        }
    }

    useEffect (() => {
        signOut()
    },[])

    return (
        <h1>Logged out</h1>
    )
}

export default LogoutPage; 