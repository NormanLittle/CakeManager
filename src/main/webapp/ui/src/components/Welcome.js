import React, {Component} from 'react';
import {Jumbotron} from 'react-bootstrap';

export default class Welcome extends Component {
    render() {
        return (
            <Jumbotron className="bg-dark text-white">
              <h1>Welcome to Cake Manager</h1>
              <blockquote className="blockquote mb-0">
                  <p>
                    “A lot of movies are about life, mine are like a slice of cake.”
                  </p>
                  <footer className="blockquote-footer">
                    Alfred Hitchcock
                  </footer>
              </blockquote>
            </Jumbotron>
        );
    }
}