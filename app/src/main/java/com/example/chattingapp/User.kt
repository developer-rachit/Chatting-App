package com.example.chattingapp

class User {

    var name: String? = null
    var email: String? = null
    var uid: String? = null
    var password: String? = null

    constructor()

    constructor(
        name: String?,
        email: String?,
        uid: String?,
        password: String?
    ) {
        this.name = name
        this.email = email
        this.uid = uid
        this.password = password
    }
}