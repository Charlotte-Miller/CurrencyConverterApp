package com.example.curencyconverterapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner_from = findViewById<Spinner>(R.id.spinner_from)
        val spinner_to = findViewById<Spinner>(R.id.spinner_to)

        val EditText_amount = findViewById<EditText>(R.id.edit_text_amount)
        val EditText_result = findViewById<EditText>(R.id.edit_text_result)

        add_currencies_to_spinners(spinner_from)
        add_currencies_to_spinners(spinner_to)

        // Set up default currency for both Spinners
        spinner_from.setSelection(0)
        spinner_to.setSelection(1)

        spinner_from.onItemSelectedListener = object : OnItemSelectedListener
        {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            )
            {
                //Get current selected currencies
                val from = spinner_from.selectedItem.toString()
                val to = spinner_to.selectedItem.toString()

//                val rate: Double = get_rate(from, to)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?)
            {
                // your code here
            }
        }

        // Repeatedly convert when Amount's text changed
        EditText_amount.addTextChangedListener(object : TextWatcher
        {
            var result: Double = 0.0
            override fun afterTextChanged(current_text: Editable?)
            {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            )
            {
            }
        })
    }

    private fun add_currencies_to_spinners(spinner: Spinner)
    {
        // Get all currencies from enum Currency
        val currencies = Currency.get_currency_list()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, currencies
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
