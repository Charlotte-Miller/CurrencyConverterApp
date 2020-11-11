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
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity()
{
    private val client = OkHttpClient()
    private val parser: Parser = Parser.default()

    val API_KEY = "b77f68582182c969fccf"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner_from = findViewById<Spinner>(R.id.spinner_from)
        val spinner_to = findViewById<Spinner>(R.id.spinner_to)

        val EditText_amount = findViewById<EditText>(R.id.edit_text_amount)
        val EditText_result = findViewById<EditText>(R.id.edit_text_result)

        add_data_to_spinner(spinner_from)

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

                val rate: Double = get_rate(from, to)
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

    private fun add_data_to_spinner(spinner: Spinner)
    {
        val currencies_url = "https://free.currconv.com/api/v7/currencies?apiKey=${API_KEY}"
        println(currencies_url)
        val data = response_from_API_call(currencies_url) as JsonArray<JsonObject>
        println(data.string("id"))

        val arraySpinner = arrayOf(
            "USD", "2", "3", "4", "5", "6", "7"
        )
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun get_rate(from: String, to: String): Double
    {
        var rate: Double = 0.0

        val key: String = "${from}_${to}"
        val convert_url =
            "https://free.currconv.com/api/v7/convert?q=${key}&compact=ultra&apiKey=${API_KEY}"

        try
        {
            val data = response_from_API_call(convert_url) as JsonObject
            rate = data.get(key).toString().toDouble()
        }
        catch (e: Exception)
        {
            println(e)
        }
        return rate
    }

    private fun response_from_API_call(url: String): Any?
    {
        var result: Any? = null

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback
        {

            override fun onFailure(call: Call, e: IOException)
            {
                println(e)
            }

            override fun onResponse(call: Call, response: Response)
            {
                try
                {
                    val api_response = response.body?.string()
                    val stringBuilder: StringBuilder =
                        StringBuilder(api_response)
                    val json = parser.parse(stringBuilder)

                    result = json
                }
                catch (e: Exception)
                {
                    println(e)
                }
            }
        })
        return result
    }
}
