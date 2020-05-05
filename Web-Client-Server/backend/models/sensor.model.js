const mongoose = require('mongoose')
const Schema = mongoose.Schema;


//this is the sensor schema of a sensor which contains all the required attributes
const SensorSchema = new Schema({
    isActive : Boolean,
    floorNo  : String,
    roomNo : String,
    smokeLevel : Number,
    CO2Level : Number,
});
const Sensor = mongoose.model('Sensor',SensorSchema);
module.exports = Sensor;