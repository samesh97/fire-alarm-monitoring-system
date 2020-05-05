import React from 'react'
import css from '../css/list.css';
import warningImage from '../images/warning.png';

function App(props)
{
    //getting the sensor list from props
    var sensors = [];
    sensors = props.sensorList;
    //run a for each loop to create a list
    const sensorList = sensors.map(item => {

        return (


            <div className="parentSensorRow" key={item._id}>
                <div>
                    {
                        //check whether the sensor is active or not
                        (item.isActive == true) &&
                        <p>

                            <p>{"Status : Active"}</p>
                            <p>{"Floor Number : "+ item.floorNo }</p>
                            <p>{"Room Number : "+ item.roomNo }</p>
                            <p>{"Smoke Level : "+ item.smokeLevel }</p>
                            <p>{"CO2 Level : "+ item.CO2Level }</p>
                        </p>
                    }
                    {
                        //checking whether the active status of the sensor is false
                        (item.isActive == false) &&
                        <p>
                            <p>{"Status : Inactive"}</p>
                            <p>{"Floor Number : "+ item.floorNo }</p>
                            <p>{"Room Number : "+ item.roomNo }</p>
                            <p>{"Smoke Level : "+ item.smokeLevel }</p>
                            <p>{"CO2 Level : "+ item.CO2Level }</p>
                        </p>
                    }
                    {(item.smokeLevel > 5 || item.CO2Level > 5) &&
                    <p className="warningText">Warning!</p>
                    }
                    {
                        //checking the smoke level whether its is less than or equal to 5 or not
                        (item.smokeLevel <= 5 && item.CO2Level <= 5) &&
                        <h2>
                             No warning!
                        </h2>
                    }
                </div>

            </div>
        )
    })

    return(

        //showing the final sensor list
        <div className="sensorList">
            {sensorList}
         </div>
    )

}
export default App;
