package com.example.unitconverter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import android.view.animation.AnimationUtils

class UnitConverterFragment : Fragment() {

    private lateinit var inputEditText: EditText
    private lateinit var outputTextView: TextView
    private lateinit var convertButton: Button
    private lateinit var conversionTypeSpinner: Spinner
    private lateinit var unitSpinner: Spinner
    private lateinit var radioGroup: RadioGroup

    private lateinit var toMilesRadioButton: RadioButton
    private lateinit var toKilometersRadioButton: RadioButton
    private lateinit var toCelsiusRadioButton: RadioButton
    private lateinit var toFahrenheitRadioButton: RadioButton
    private lateinit var toPoundsRadioButton: RadioButton
    private lateinit var toKilogramsRadioButton: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_unit_converter, container, false)

        inputEditText = view.findViewById(R.id.inputEditText)
        outputTextView = view.findViewById(R.id.outputTextView)
        convertButton = view.findViewById(R.id.convertButton)
        conversionTypeSpinner = view.findViewById(R.id.conversionTypeSpinner)
        unitSpinner = view.findViewById(R.id.unitSpinner)
        radioGroup = view.findViewById(R.id.radioGroup)

        toMilesRadioButton = view.findViewById(R.id.toMilesRadioButton)
        toKilometersRadioButton = view.findViewById(R.id.toKilometersRadioButton)
        toCelsiusRadioButton = view.findViewById(R.id.toCelsiusRadioButton)
        toFahrenheitRadioButton = view.findViewById(R.id.toFahrenheitRadioButton)
        toPoundsRadioButton = view.findViewById(R.id.toPoundsRadioButton)
        toKilogramsRadioButton = view.findViewById(R.id.toKilogramsRadioButton)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.conversion_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            conversionTypeSpinner.adapter = adapter
        }

        conversionTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                updateUnits(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        convertButton.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click))
            val input = inputEditText.text.toString().toDoubleOrNull()
            if (input != null) {
                val result = when (conversionTypeSpinner.selectedItemPosition) {
                    0 -> if (toMilesRadioButton.isChecked) convertToMiles(input) else convertToKilometers(input)
                    1 -> if (toCelsiusRadioButton.isChecked) convertToCelsius(input) else convertToFahrenheit(input)
                    2 -> if (toPoundsRadioButton.isChecked) convertToPounds(input) else convertToKilograms(input)
                    else -> null
                }
                outputTextView.text = result?.toString() ?: getString(R.string.invalid_input)
            } else {
                outputTextView.text = getString(R.string.invalid_input)
            }
        }

        return view
    }

    private fun updateUnits(position: Int) {
        val unitsArray = when (position) {
            0 -> R.array.length_units
            1 -> R.array.temperature_units
            2 -> R.array.weight_units
            else -> R.array.units_array
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            unitsArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            unitSpinner.adapter = adapter
        }
        updateRadioButtons(position)
    }

    private fun updateRadioButtons(position: Int) {
        // Hide all radio buttons first
        toMilesRadioButton.visibility = View.GONE
        toKilometersRadioButton.visibility = View.GONE
        toCelsiusRadioButton.visibility = View.GONE
        toFahrenheitRadioButton.visibility = View.GONE
        toPoundsRadioButton.visibility = View.GONE
        toKilogramsRadioButton.visibility = View.GONE

        // Show relevant radio buttons based on conversion type
        when (position) {
            0 -> {
                toMilesRadioButton.visibility = View.VISIBLE
                toKilometersRadioButton.visibility = View.VISIBLE
            }
            1 -> {
                toCelsiusRadioButton.visibility = View.VISIBLE
                toFahrenheitRadioButton.visibility = View.VISIBLE
            }
            2 -> {
                toPoundsRadioButton.visibility = View.VISIBLE
                toKilogramsRadioButton.visibility = View.VISIBLE
            }
        }
    }

    private fun convertToMiles(kilometers: Double): Double = kilometers * 0.621371
    private fun convertToKilometers(miles: Double): Double = miles / 0.621371
    private fun convertToCelsius(fahrenheit: Double): Double = (fahrenheit - 32) * 5 / 9
    private fun convertToFahrenheit(celsius: Double): Double = celsius * 9 / 5 + 32
    private fun convertToPounds(kilograms: Double): Double = kilograms * 2.20462
    private fun convertToKilograms(pounds: Double): Double = pounds / 2.20462
}
