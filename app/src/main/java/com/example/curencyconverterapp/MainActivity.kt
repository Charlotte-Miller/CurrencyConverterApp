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
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity()
{
    private val client = OkHttpClient()

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
        val arraySpinner = arrayOf(
            "1", "2", "3", "4", "5", "6", "7"
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

        val from_to: String = "${from}_${to}"
        val api_url =
            "https://free.currconv.com/api/v7/convert?q=${from_to}&compact=ultra&apiKey=${API_KEY}"

        try
        {
            rate = response_from_API_call(api_url, from_to).toDouble()
        }
        catch (e: Exception)
        {
            println(e)
        }

        return rate
    }

    private fun response_from_API_call(url: String, key: String): String
    {
        var result: String = "No response"

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
                val json_object = JSONObject(response.body?.string())
                try
                {
                    result = json_object.getString(key)
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
