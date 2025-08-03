package com.example.myar

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var drillDropdown: Spinner
    private lateinit var startButton: Button
    private val drills = listOf("Drill 1", "Drill 2", "Drill 3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drillDropdown = findViewById(R.id.spinnerDrill)
        startButton = findViewById(R.id.btnStartDrill)

        drillDropdown.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, drills)

        startButton.setOnClickListener {
            val selectedDrill = drillDropdown.selectedItem.toString()
            val intent = Intent(this, DrillInfoActivity::class.java)
            intent.putExtra("drillName", selectedDrill)
            startActivity(intent)
        }
    }
}
