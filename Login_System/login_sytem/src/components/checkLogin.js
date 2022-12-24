import Cookies from 'js-cookie';
import axios from 'axios';

/**
 * checks to see if the user is logged in
 * sends a request to the server with a jwt and waites for a resposne 
 * if true, returns true
 * otherwise, send a request to the server to see if the refresh JWT has expired
 * if it hasn't, set a new JWT sent by the server and return true
 * else, return false
 * @returns boolean
 */
export async function checkLogin() {
    try {
        const jwtToken = Cookies.get("jwtToken")
        const response = await axios.post('https://localhost:8443/user/auth', jwtToken, {
            headers: {
              'Content-Type': 'text/plain'
            }
          });
        if (response.data) {
          return response.data
        } else {
            const response2 = await axios.post('https://localhost:8443/user/checkrefreshtoken', jwtToken, {
            headers: {
              'Content-Type': 'text/plain'
            }
          });

          if (response2.data.loggedIn === "true") {
            const jwt = response2.data.jwt;
            Cookies.set("jwtToken", jwt, { path: '/' });
            return true
          } else {
            return false; 
          }
        }
    } catch (e) {
        
    }
}