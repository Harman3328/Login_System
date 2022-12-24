import React, {useEffect, useState} from "react";
import { Link } from "react-router-dom";
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import { checkLogin } from "./checkLogin";
import { useLocation } from 'react-router-dom';

/**
 * checks to see if user is logged in or not and sets the state
 * if the user is logged in, changes the login to logout on the vav bar
 * @returns nav bar
 */
function Header() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const location = useLocation();
  useEffect(() => {
    const checkLoginStatus = async () => {
      const loggedIn = await checkLogin();
      setIsLoggedIn(loggedIn)
    };
    checkLoginStatus(); 
  }, [location])

  return (
    <Navbar bg="dark" variant="dark">
      <Container>
        <Navbar.Brand as={Link} to={"/"}>Navbar</Navbar.Brand>
        <Nav className="me-auto">
          <Nav.Link as={Link} to={"/"}>Home</Nav.Link>
          {isLoggedIn ? (
            <Nav.Link as={Link} to={"/logout"}>Logout</Nav.Link>
          ) : (
            <Nav.Link as={Link} to={"/login"}>Login</Nav.Link>
          )}
        </Nav>
      </Container>
    </Navbar>
    );
}

export default Header; 