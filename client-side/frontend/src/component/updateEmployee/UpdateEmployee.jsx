import React, {useEffect, useState} from 'react'
import './updateEmployee.css'
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { SkipBackCircle } from "@phosphor-icons/react";
import { useNavigate } from 'react-router-dom';

const UpdateEmployee = () => {

    const navigate = useNavigate();

    const { id } = useParams();
    const [firstName, setFirstName] = useState(' ');
    const [lastName, setLastName] = useState(' ');
    const [emailId, setEmailId] = useState(' ');

    const changeFirstNameHandler = (event) => {
        setFirstName(event.target.value);
    }
    const changeLastNameHandler = (event) => {
        setLastName(event.target.value);
    }
    const changeEmailIdHandler = (event) => {
        setEmailId(event.target.value);
    }
    const getEmployeeById = async (employeeId) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/v1/employees/${employeeId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    }

    useEffect(() => {
        getEmployeeById(id)
            .then((data) => {
                setFirstName(data.firstName);
                setLastName(data.lastName);
                setEmailId(data.emailId);
            })
            .catch((error) => console.error("Error fetching tasks: " + error));
    }, []);

    const updateEmployee = () => {

        const employeeData = { id, firstName, lastName, emailId };

        // You can use a library like Axios to make the HTTP request
        // Replace the following code with your actual API call
        axios.put(`http://localhost:8080/api/v1/employees/${id}`, employeeData)
            .then(response => {
                alert("updated successfully");
                setFirstName('');
                setLastName('');
                setEmailId('');
            })
            .catch(error => {
                alert("Not updated");
            });
    };
    return (
        <>
            <div className="employee_container add_container">
                <SkipBackCircle onClick={() => navigate(-1)} size={50} style={{ color: "var(--secondary-color)", cursor: 'pointer', margin: "1rem 0 0 0.5rem" }} />
                <div className="employee_title">Update Employee</div>

                <div className="employee_form section__margin">
                    <form className="employee_add_form" action="">

                        <label>First Name:</label>
                        <input placeholder='First Name' name='firstName' value={firstName} onChange={changeFirstNameHandler}></input>
                        <label>Last Name:</label>
                        <input placeholder='First Name' name='lastName' value={lastName} onChange={changeLastNameHandler}></input>
                        <label>Email Id:</label>
                        <input placeholder='Email Id' name='email' value={emailId} onChange={changeEmailIdHandler}></input>

                    </form>

                    <button className="submit_btn" onClick={updateEmployee}>Submit</button>
                </div>
            </div>
        </>
    )
}

export default UpdateEmployee