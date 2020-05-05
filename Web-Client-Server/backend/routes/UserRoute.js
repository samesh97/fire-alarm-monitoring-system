const express = require('express');
const router = express.Router();
let UserModel = require('../models/user.model');


router.route('/login').get((req,res) => {

    const username = req.query.username;
    const password = req.query.password;

    if(username != null && password != null)
    {
        UserModel.find(
            {"username" : username,"password" : password}
        )
            .then(users => res.json(users).status(200))
            .catch(err => res.status(400).json('Error: ' + err));
    }
    else
    {
        res.status(400).json('Error: Parameters are null');
    }


});
router.route('/register').get((req,res) => {


    const username = req.query.username;
    const password = req.query.password;
    const user = new UserModel();
    user.username = username;
    user.password = password;

    if(username != null && password != null)
    {
        user.save()
            .then(()=> res.json('User registered!').status(200))
            .catch(err => res.status(400).json('Error: ' + err));
    }
    else
    {
        res.status(400).json('Error: Parameters are null')
    }


});
module.exports = router;
