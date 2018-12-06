package com.alex.atm.UIkit

import java.util.ArrayList

data class Contact(var id: Int = 0, var name: String = "", var phones: List<String> = ArrayList()) {
    constructor(id: Int, name: String) : this(){
        this.id=id
        this.name=name
    }
}

