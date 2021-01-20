import React, {Component} from 'react';

import {Card, Form, Button, Col} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faPlusSquare, faSave, faUndo, faList, faEdit} from '@fortawesome/free-solid-svg-icons';
import CakeToast from './CakeToast'
import axios from 'axios';

export default class Cake extends Component {

    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.state.show = false;
        this.handleChange = this.handleChange.bind(this);
        this.handleSave = this.handleSave.bind(this);
    }

    initialState = {
        id:'', title:'', description:'', image:''
    };

    componentDidMount() {
        const cakeId = +this.props.match.params.id;
        if (cakeId !== null) {
            this.findCakeById(cakeId);
        }
    }

    findCakeById = (cakeId) => {
        axios.get("http://localhost:8080/cakes/" + cakeId)
            .then(response => {
                if (response.data != null) {
                    this.setState({
                        id: response.data.id,
                        title: response.data.title,
                        description: response.data.description,
                        image: response.data.image
                    })
                }
            }).catch((error) => {
                console.error("Error - " + error);
            })
    };

    handleChange = event => {
        this.setState({
            [event.target.name]:event.target.value
        });
    };

    handleReset = () => {
        this.setState(() => this.initialState);
    };

    handleSave = event => {
        event.preventDefault();

        const cake = {
            title: this.state.title,
            description: this.state.description,
            image: this.state.image
        };

        axios.post("http://localhost:8080/cakes", cake)
            .then(response => {
                if (response.data != null) {
                    this.setState({"show": true, "action": "add"});
                    setTimeout(() => this.setState({"show":false}), 3000);
                } else {
                    this.setState({"show": false});
                }
            });
        this.setState(this.initialState);
    };

    handleUpdate = event => {
        event.preventDefault();

        const cake = {
            title: this.state.title,
            description: this.state.description,
            image: this.state.image
        };

        axios.put("http://localhost:8080/cakes/" + this.state.id, cake)
            .then(response => {
                if (response.data != null) {
                    this.setState({"show": true, "action": "update"});
                    setTimeout(() => this.setState({"show":false}), 3000);
                    setTimeout(() => this.handleCakeList(), 3000);
                } else {
                    this.setState({"show": false});
                }
            });
        this.setState(this.initialState);
    };

    handleCakeList = () => {
        return this.props.history.push("/list");
    };

    render() {
        const {title, description, image} = this.state;
        return (
            <div>
                <div style={{"display":this.state.show ? "block" : "none"}}>
                    <CakeToast show = {this.state.show} message = {this.state.action === "update" ? "Cake Updated Successfully" : "Cake Saved Successfully"} type = {"success"}/>
                </div>
                <Card className={"border border-dark bg-dark text-white"}>
                    <Card.Header>
                        <FontAwesomeIcon icon={this.state.id ? faEdit : faPlusSquare}/> {this.state.id ? "Update Cake": "Add New Cake"}
                    </Card.Header>
                    <Form id="cakeFormId" onReset={this.handleReset} onSubmit={this.state.id ? this.handleUpdate : this.handleSave}>
                        <Card.Body>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formTitle">
                                  <Form.Label>Title</Form.Label>
                                  <Form.Control
                                    type="text"
                                    className={"bg-dark text-white"}
                                    placeholder="Enter title"
                                    name="title"
                                    value={title} onChange={this.handleChange}
                                    required autoComplete="off"/>
                                </Form.Group>
                                <Form.Group as={Col} controlId="formDescription">
                                  <Form.Label>Description</Form.Label>
                                  <Form.Control
                                    type="text"
                                    className={"bg-dark text-white"}
                                    placeholder="Enter description"
                                    name="description"
                                    value={description} onChange={this.handleChange}
                                    required autoComplete="off"/>
                                </Form.Group>
                                <Form.Group as={Col} controlId="formImage">
                                <Form.Label>Image URL</Form.Label>
                                  <Form.Control
                                    type="text"
                                    className={"bg-dark text-white"}
                                    placeholder="Enter image URL"
                                    name="image"
                                    value={image} onChange={this.handleChange}
                                    required autoComplete="off"/>
                                </Form.Group>
                            </Form.Row>
                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button size="sm" variant="success" type="submit">
                              <FontAwesomeIcon icon={faSave}/> {this.state.id ? "Update": "Save"}
                            </Button>{' '}
                            <Button size="sm" variant="info" type="reset">
                              <FontAwesomeIcon icon={faUndo}/> Reset
                            </Button>{' '}
                            <Button size="sm" variant="secondary" type="button" onClick={this.handleCakeList.bind()}>
                                <FontAwesomeIcon icon={faList} /> List
                            </Button>
                        </Card.Footer>
                    </Form>
                </Card>
            </div>
        );
    }
}