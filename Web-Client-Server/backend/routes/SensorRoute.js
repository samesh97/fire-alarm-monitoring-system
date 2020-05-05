const express = require('express');
const router = express.Router();
let SensorModel = require('../models/sensor.model');

router.route('/').get((req, res) => {
    SensorModel.find()
       .then(sensors => res.status(200).json(sensors))
       .catch(err => res.status(400).json('Error: ' + err));
});
router.route('/createNewSensor').post((req,res) => {


  const isActive = req.query.isActive;
  const floorNo = req.query.floorNo;
  const roomNo = req.query.roomNo;
  const smokeLevel = req.query.smokeLevel;
  const CO2Level = req.query.CO2Level;



  if(isActive != null && floorNo != null && roomNo != null && smokeLevel != null && CO2Level != null)
  {
      const newSensor = new SensorModel();
      newSensor.isActive = isActive;
      newSensor.floorNo = floorNo;
      newSensor.roomNo = roomNo;
      newSensor.smokeLevel = smokeLevel;
      newSensor.CO2Level = CO2Level;

      newSensor.save()
          .then(()=> res.json('New sensor was successfully added!'))
          .catch(err => res.status(400).json('Error: ' + err));
  }
  else
  {
      res.status(400).json('Error: Parameters are empty!' );
  }
});
router.route('/updateSensor').post((req,res) => {

    const id = req.query.id;
    const floorNo = req.query.floorNo;
    const roomNo = req.query.roomNo;

    if(id != null && floorNo != null && roomNo != null)
    {
        // SensorModel.update({_id: id}, {
        //     floorNo: floorNo,
        //     roomNo: roomNo
        // })
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
router.route('/findSensorById').get((req,res) => {

    const id = req.query.id;
    if(id != null )
    {
        SensorModel.find({"_id" : id})
            .then(sensor => res.json(sensor).status(200))
            .catch(err => res.status(400).json('Error: ' + err));
    }
    else
    {
        res.status(400);
    }

});
router.route('/updateSensorBySensorApp').post((req,res) => {

    const id = req.query.id;
    const co2Level = req.query.co2Level;
    const smokeLevel = req.query.smokeLevel;
    const isActive = req.query.isActive;

    if(id != null && co2Level != null && smokeLevel != null && isActive != null)
    {
        SensorModel.update({_id: id}, {
            isActive: isActive,
            smokeLevel: smokeLevel,
            CO2Level : co2Level
        }).then(()=> res.json('Sensor Updated!').status(200))
            .catch(err => res.status(400).json('Error: ' + err));
    }
    else
    {
        res.status(400);
    }

});
module.exports = router;
