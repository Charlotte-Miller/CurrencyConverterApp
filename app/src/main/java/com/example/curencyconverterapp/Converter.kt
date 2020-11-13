package com.example.curencyconverterapp

class Converter(private val from: EnumCurrency, val to: EnumCurrency)
{
    val rate: Double = from.exchange_rate_to_USD / to.exchange_rate_to_USD

    fun convert(amount: Double): Double
    {
        return amount * rate
    }

    fun get_from_currency(): EnumCurrency = from

    fun get_to_currency(): EnumCurrency = to
}