package com.example.demo

import com.google.gson.Gson
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Usuario(var nombre:String, var pass:String) {

    @Id
    @GeneratedValue
    var id = 0
    var clave:String=""

    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}