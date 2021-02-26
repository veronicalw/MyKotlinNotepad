package com.example.mykotlinnotepad.models
data class Notes(
    var titles: String = "",
    var contents: String = ""
) {
    constructor() : this("","")
}