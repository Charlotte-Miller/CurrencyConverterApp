package com.example.curencyconverterapp

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.*


class MainActivity : AppCompatActivity()
{
    // Initialize
    lateinit var spinner_from: Spinner
    lateinit var spinner_to: Spinner
    lateinit var edit_text_amount: EditText
    lateinit var edit_text_result: EditText
    lateinit var text_view_last_updated: TextView
    lateinit var text_view_result: TextView
    lateinit var text_view_amount: TextView

    // Select USD, VND as default for from, to currency
    var converter = Converter(EnumCurrency.USD, EnumCurrency.VND)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hide_status_bar()

        spinner_from = findViewById<Spinner>(R.id.spinner_from)
        spinner_to = findViewById<Spinner>(R.id.spinner_to)

        edit_text_amount = findViewById<EditText>(R.id.edit_text_amount)
        edit_text_result = findViewById<EditText>(R.id.edit_text_result)

        text_view_last_updated = findViewById<TextView>(R.id.last_updated)
        text_view_result = findViewById<TextView>(R.id.text_view_result)
        text_view_amount = findViewById<TextView>(R.id.text_view_amount)

        text_view_last_updated.text = "Last updated: ${EnumCurrency.get_last_updated()}"

        // Set up items for both Spinners
        set_up_spinners(spinner_from, spinner_to)

        add_listeners()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hide_status_bar()
    {
        // Hide the status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // Hide the action bar
        actionBar?.hide()
    }

    private fun add_listeners()
    {
        spinner_from.onItemSelectedListener = object : OnItemSelectedListener
        {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            )
            {
                render_exchanged_money()
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
                render_exchanged_money()
            }


            override fun onNothingSelected(parentView: AdapterView<*>?)
            {
                // your code here
            }
        }

        // Repeatedly convert when Amount's text changed
        edit_text_amount.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(current_text: Editable?)
            {
                if (!current_text.isNullOrBlank())
                {
                    val amount: Double = current_text.toString().toDouble()
                    convert_then_render(amount, edit_text_result)
                } else
                {
                    edit_text_result.setText("")
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

    private fun render_exchanged_money()
    {
        //Get current selected currencies
        val (from, to) = update_currency_label()

        // Set converter for current currencies
        converter = Converter(EnumCurrency.valueOf(from), EnumCurrency.valueOf(to))

        // Re-render the exchanged money if select another currency
        if (!edit_text_amount.text.isNullOrEmpty())
        {
            val amount = edit_text_amount.text.toString().toDouble()
            convert_then_render(amount, edit_text_result)
        }
    }

    private fun update_currency_label(): Pair<String, String>
    {
        //Get current selected currencies
        val from = spinner_from.selectedItem.toString()
        val to = spinner_to.selectedItem.toString()

        // Render labels by selected currency
        text_view_result.text = EnumCurrency.valueOf(to).get_name()
        text_view_amount.text = EnumCurrency.valueOf(from).get_name()

        return Pair(from, to)
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
