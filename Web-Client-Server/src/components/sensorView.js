import React from 'react'
import axios from 'axios';
import SensorList from './list';
class App extends React.Component
{
    constructor(props)
    {
        super(props);
        this.state = {
            list : []
        }
        this.getCurrentSensors();


    }
    getCurrentSensors()
    {
        //for first time loading
        axios.get("http://localhost:5000/api/sensors/")
            .then(response => {
                if(response.status == 200)
                {
                    var li = response.data;
                    li.reverse();
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
            <div>
                <SensorList sensorList ={this.state.list}></SensorList>
            </div>
        )

    }
}
export default App;