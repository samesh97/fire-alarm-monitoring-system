import React from 'react'
import axios from 'axios';
//importing the sensorList component
import SensorList from './list';
class App extends React.Component
{
    //constructor of the class
    constructor(props)
    {
        super(props);

        //keeping a list in the states
        this.state = {
            list : []
        }
        //getting sensors from the api
        this.getCurrentSensors();


    }
    //getting all the sensors by sending a request to the api using axios
    getCurrentSensors()
    {
        //for first time loading sensors
        axios.get("http://localhost:5000/api/sensors/")
            .then(response => {
                if(response.status == 200)
                {
                    //setting the sensor list with the response we got
                    var li = response.data;
                    //reverse it to get the newly created sensors to the front
                    li.reverse();
                    //setting the list in states
                    this.setState({list :li});
                }
            })
            .catch(error => alert(error));


        //for update the sensors every 40 seconds
        const timePeriod = 40000;
        setInterval(() =>{
            axios.get("http://localhost:5000/api/sensors/")
                .then(response => {
                    if(response.status === 200)
                    {
                        //this method will be called every 40 seconds and update the list
                        var li = response.data;
                        li.reverse();
                        this.setState({list :li});
                    }
                })
                .catch(error => alert(error));
        }, timePeriod);

    }
    render() {
        return (
            //stting the sensor list by passing the sensors we got by the API
            <div>
                <SensorList sensorList ={this.state.list}></SensorList>
            </div>
        )

    }
}
export default App;