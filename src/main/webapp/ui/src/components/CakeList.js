import React, {Component} from 'react';

import {Card, Table, Image, ButtonGroup, Button} from 'react-bootstrap';
import {Link} from 'react-router-dom'
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faList, faEdit, faTrash} from '@fortawesome/free-solid-svg-icons';
import CakeToast from './CakeToast'
import axios from 'axios';

export default class CakeList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            cakes : []
        };
    }

    componentDidMount() {
        this.findAllCakes();
    }

    findAllCakes() {
        axios.get("http://localhost:8080/cakes")
            .then(response => response.data)
            .then((data) => {
                this.setState({cakes: data});
            });
    };

    deleteCake = (cakeId) => {
        axios.delete("http://localhost:8080/cakes/" + cakeId)
            .then(response =>  {
                if (response.status === 204) {
                    this.setState({"show": true});
                    setTimeout(() => this.setState({"show":false}), 3000);
                    this.setState({
                        cakes: this.state.cakes.filter(cake => cake.id !== cakeId)
                    });
                } else {
                    this.setState({"show": false});
                }
            });
    };

    render() {
        return (
            <div>
                <div style={{"display":this.state.show ? "block" : "none"}}>
                    <CakeToast show = {this.state.show} message = {"Cake Deleted Successfully"} type = {"danger"}/>
                </div>
                <Card className={"border border-dark bg-dark text-white"}>
                    <Card.Header>
                        <FontAwesomeIcon icon={faList}/> Cake List
                    </Card.Header>
                    <Card.Body>
                      <Table bordered hover striped variant="dark">
                        <thead>
                            <tr>
                              <th>Title</th>
                              <th>Description</th>
                              <th>Actions</th>
                            </tr>
                          </thead>
                          <tbody>
                            {
                                this.state.cakes.length === 0 ?
                                <tr align="center">
                                  <td colSpan="3">No Cakes Available.</td>
                                </tr> :
                                this.state.cakes.map((cake) => (
                                    <tr key={cake.id}>
                                      <td>
                                        <Image src={cake.image} roundedCircle width="50" height="50"/> {cake.title}
                                      </td>
                                      <td style={{ verticalAlign: 'middle' }}>{cake.description}</td>
                                      <td>
                                          <ButtonGroup>
                                            <Link to={"edit/" + cake.id} className="btn btn-sm btn-outline-primary"><FontAwesomeIcon icon={faEdit} /></Link>{' '}
                                            <Button size="sm" variant="outline-danger" onClick={this.deleteCake.bind(this, cake.id)}><FontAwesomeIcon icon={faTrash} /></Button>
                                          </ButtonGroup>
                                      </td>
                                    </tr>
                                ))
                            }
                          </tbody>
                      </Table>
                    </Card.Body>
                </Card>
            </div>
        );
    }
}