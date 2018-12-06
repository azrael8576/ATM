package com.alex.atm.UIkit

data class Function(var name: String = "", var icon: Int? = 0) {
    constructor(name: String) : this(){
        this.name = name
    }
}
