package com.example.curencyconverterapp

import java.text.NumberFormat
import java.util.*


fun main()
{
    println(formatted_currency(1.2,EnumCurrency.EUR.name))
}

private fun formatted_currency(amount: Double, currency: String): String
{
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance(currency)

    return format.format(amount)
}