package com.alex.atm.UIkit

import org.json.JSONException
import org.json.JSONObject

data class Transaction(var account: String = "", var date: String = "", var amount: Int = 0, var type: Int = 0) {

    constructor(`object`: JSONObject) :this() {
        try {
            account = `object`.getString("account")
            date = `object`.getString("date")
            amount = `object`.getInt("amount")
            type = `object`.getInt("type")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}
