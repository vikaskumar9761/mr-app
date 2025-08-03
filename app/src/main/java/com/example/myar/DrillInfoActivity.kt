package com.example.myar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DrillInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drill_info)

        val drillName = intent.getStringExtra("drillName") ?: "Unknown Drill"
        findViewById<TextView>(R.id.tvDrillName).text = drillName

        findViewById<Button>(R.id.btnOpenAR).setOnClickListener {
            startActivity(Intent(this, ARActivity::class.java))
        }
    }
}
