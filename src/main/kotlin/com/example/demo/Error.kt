package com.example.demo

import com.google.gson.Gson

data class Error(var codigo:Int, var motivo:String) {

    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}