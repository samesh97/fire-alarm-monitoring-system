const mongoose = require('mongoose')
const Schema = mongoose.Schema;

const SensorSchema = new Schema({
    isActive : Boolean,
    floorNo  : String,
    roomNo : String,
    smokeLevel : Number,
    CO2Level : Number,
});
const Sensor = mongoose.model('Sensor',SensorSchema);
module.exports = Sensor;