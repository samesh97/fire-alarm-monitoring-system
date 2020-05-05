const express = require('express');
const router = express.Router();
let SensorModel = require('../models/sensor.model');


//this route is to get all the sesnors in the database
router.route('/').get((req, res) => {
    SensorModel.find()
       .then(sensors => res.status(200).json(sensors))
       .catch(err => res.status(400).json('Error: ' + err));
});
//this route is to create a new sensor
router.route('/createNewSensor').post((req,res) => {


    //all the required attributes is taking from the request variable
  const isActive = req.query.isActive;
  const floorNo = req.query.floorNo;
  const roomNo = req.query.roomNo;
  const smokeLevel = req.query.smokeLevel;
  const CO2Level = req.query.CO2Level;


  //checking whether any of required attributes are not null

  if(isActive != null && floorNo != null && roomNo != null && smokeLevel != null && CO2Level != null)
  {
      const newSensor = new SensorModel();
      newSensor.isActive = isActive;
      newSensor.floorNo = floorNo;
      newSensor.roomNo = roomNo;
      newSensor.smokeLevel = smokeLevel;
      newSensor.CO2Level = CO2Level;


      //saving the sensor in the database
      newSensor.save()
          .then(()=> res.json('New sensor was successfully added!'))
          .catch(err => res.status(400).json('Error: ' + err));
  }
  else
  {
      //sending an error code of 400
      res.status(400).json('Error: Parameters are empty!' );
  }
});
//this route is to update a sensor
router.route('/updateSensor').post((req,res) => {

    //these are the reuired paramaters that is needed to update a sensor
    const id = req.query.id;
    const floorNo = req.query.floorNo;
    const roomNo = req.query.roomNo;

    //checking whether none of the are not equal to null
    if(id != null && floorNo != null && roomNo != null)
    {

        //updating the sensor with its id
        SensorModel.update({_id: id}, {
            floorNo: floorNo,
            roomNo: roomNo
        }).then(()=> res.json('Sensor Updated!').status(200))
            .catch(err => res.status(400).json('Error: ' + err));
    }
    else
    {
        res.status(400);
    }

});
//this route is to find a sensor
router.route('/findSensorById').get((req,res) => {

    //getting the id as a paramater
    const id = req.query.id;
    if(id != null )
    {
        //finding in the database
        SensorModel.find({"_id" : id})
            .then(sensor => res.json(sensor).status(200))
            .catch(err => res.status(400).json('Error: ' + err));
    }
    else
    {
        //sending error coe of 400
        res.status(400);
    }

});
//this route is to update a sensor by the sensor App
router.route('/updateSensorBySensorApp').post((req,res) => {

    //gathering all the reuired paramaters
    const id = req.query.id;
    const co2Level = req.query.co2Level;
    const smokeLevel = req.query.smokeLevel;
    const isActive = req.query.isActive;

    //cheking whether they are null or not
    if(id != null && co2Level != null && smokeLevel != null && isActive != null)
    {
        //update the sensor with attributes
        SensorModel.update({_id: id}, {
            isActive: isActive,
            smokeLevel: smokeLevel,
            CO2Level : co2Level
        }).then(()=> res.json('Sensor Updated!').status(200))
            .catch(err => res.status(400).json('Error: ' + err));
    }
    else
    {
        //sennding an error code
        res.status(400);
    }

});
module.exports = router;
