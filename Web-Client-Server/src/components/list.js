import React from 'react'
import css from '../css/list.css';
import warningImage from '../images/warning.png';

function App(props)
{
    var sensors = [];
    sensors = props.sensorList;
    const sensorList = sensors.map(item => {

        return (


            <div className="parentSensorRow" key={item._id}>
                <div>
                    {
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
                    {(item.smokeLevel <= 5 && item.CO2Level <= 5) &&
                        <h2>
                             No warning!
                        </h2>
                    }
                </div>

            </div>
        )
    })

    return(
        <div className="sensorList">
            {sensorList}
         </div>
    )

}
export default App;
