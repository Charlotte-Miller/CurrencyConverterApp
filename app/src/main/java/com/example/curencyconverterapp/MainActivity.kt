package com.example.curencyconverterapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.*


class MainActivity : AppCompatActivity()
{
    var converter = Converter(EnumCurrency.USD, EnumCurrency.VND)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize
        val spinner_from = findViewById<Spinner>(R.id.spinner_from)
        val spinner_to = findViewById<Spinner>(R.id.spinner_to)

        val EditText_amount = findViewById<EditText>(R.id.edit_text_amount)
        val EditText_result = findViewById<EditText>(R.id.edit_text_result)

        val TextView_last: TextView = findViewById<TextView>(R.id.last_updated)

        TextView_last.setText("Last updated: ${EnumCurrency.get_last_updated()}")

        // Set up items for both Spinners
        set_up_spinners(spinner_from, spinner_to)

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

                // Set converter for current currencies
                converter = Converter(EnumCurrency.valueOf(from), EnumCurrency.valueOf(to))

                // Re-render the exchanged money if select another currency
                if (!EditText_amount.text.isNullOrEmpty())
                {
                    val amount = EditText_amount.text.toString().toDouble()
                    convert_then_render(amount, EditText_result)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?)
            {
                // your code here
            }
        }

        spinner_to.onItemSelectedListener = object : OnItemSelectedListener
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

                // Set converter for current currencies
                converter = Converter(EnumCurrency.valueOf(from), EnumCurrency.valueOf(to))

                // Re-render the exchanged money if select another currency
                if (!EditText_amount.text.isNullOrEmpty())
                {
                    val amount = EditText_amount.text.toString().toDouble()
                    convert_then_render(amount, EditText_result)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?)
            {
                // your code here
            }
        }

        // Repeatedly convert when Amount's text changed
        EditText_amount.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(current_text: Editable?)
            {
                if (!current_text.isNullOrBlank())
                {
                    val amount: Double = current_text.toString().toDouble()
                    convert_then_render(amount, EditText_result)
                }
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

    private fun convert_then_render(
        amount: Double,
        rendered_EditText: EditText
    )
    {
        try
        {
            val result: Double = converter.convert(amount)

            // Format the Results text to Currency format
            rendered_EditText.setText(
                formatted_currency(
                    result,
                    converter.get_to_currency().name
                )
            )

        }
        catch (e: Exception)
        {
            rendered_EditText.setText("Error")
        }
    }

    private fun formatted_currency(amount: Double, currency: String): String
    {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 2
        format.currency = Currency.getInstance(currency)

        return format.format(amount)
    }

    private fun set_up_spinners(
        spinner_from: Spinner,
        spinner_to: Spinner
    )
    {
        add_currencies_to_spinners(spinner_from)
        add_currencies_to_spinners(spinner_to)

        // Set USD and VND as default currency for both Spinners
        spinner_from.setSelection(EnumCurrency.USD.ordinal)
        spinner_to.setSelection(EnumCurrency.VND.ordinal)
    }

    // Add all currencies from enum Currency to Spinner
    private fun add_currencies_to_spinners(spinner: Spinner)
    {
        // Get all currencies from enum Currency
        val currencies = EnumCurrency.get_currency_list()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, currencies
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
