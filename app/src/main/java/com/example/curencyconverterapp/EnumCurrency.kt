package com.example.curencyconverterapp

import kotlin.collections.ArrayList

enum class EnumCurrency(val exchange_rate_to_USD: Double = 1.0)
{
    USD(1.0),
    VND(0.0000430597),
    EUR(1.18082),
    GBP(1.31362),
    INR(0.0133905);

    companion object
    {
        fun get_last_updated(): String = "2020-11-13 06:44 UTC"

        fun get_currency_list(): ArrayList<String>
        {
            val currencies = ArrayList<String>()
            for (currency in EnumCurrency.values())
            {
                currencies.add(currency.name)
            }
            return currencies
        }
    }
}
