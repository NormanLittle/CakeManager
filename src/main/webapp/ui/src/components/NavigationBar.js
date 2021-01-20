import React, {Component} from 'react';
import {Navbar, Nav} from 'react-bootstrap';
import {Link} from 'react-router-dom'

export default class NavigationBar extends Component {
    render() {
        return (
            <Navbar bg="dark" variant="dark">
                <Link to={""} className="navbar-brand">
                    <img src="https://upload.wikimedia.org/wikipedia/commons/7/7f/Cake-icon.png" width="25" height="25" alt="brand"/> Cake Manager
                </Link>
                <Nav className="mr-auto">
                  <Link to={"add"} className="nav-link">Add Cake</Link>
                  <Link to={"list"} className="nav-link">Cake List</Link>
                </Nav>
             </Navbar>
        )
    }
}