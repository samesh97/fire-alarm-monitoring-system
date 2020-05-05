const mongoose = require('mongoose')
const Schema = mongoose.Schema;

//this is the user schema of a user which contains all the required attributes
const UserSchema = new Schema({
    username  : String,
    password : String,
});
const User = mongoose.model('User',UserSchema);
module.exports = User;