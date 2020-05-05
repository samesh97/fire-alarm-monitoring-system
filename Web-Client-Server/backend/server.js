const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');


require('dotenv').config();

const app = express();
const port = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());

const uri = process.env.ATLAS_URI;
mongoose.connect(uri, {useNewUrlParser : true, useCreateIndex : true});
const connection = mongoose.connection;
connection.once('open', () => {
   console.log("MongoDb database connection established sucessfully!");
});


const sensorRouter = require('./routes/SensorRoute');
app.use('/api/sensors',sensorRouter);

const userRouter = require('./routes/UserRoute');
app.use('/api/users',userRouter);

app.listen(port, () => {
   console.log("server is running on port " + port);
});