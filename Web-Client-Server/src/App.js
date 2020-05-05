import React from 'react';
import './App.css';
//importing the sensorView component
import SensorView from './components/sensorView';
//importing the css
import css from './css/app.css';

function App() {
  return (
      //setting the sensorView
      <div className="container">
        <SensorView/>
      </div>

  );
}

export default App;
