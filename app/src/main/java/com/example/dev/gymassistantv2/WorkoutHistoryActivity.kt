package com.example.dev.gymassistantv2

import android.app.Activity
import android.os.Bundle
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase
import android.content.Intent
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.widget.*
import java.sql.Date
import java.text.SimpleDateFormat


class WorkoutHistoryActivity : Activity() {

    private var userId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generic_history_layout)

        processIntent()
        generateWorkoutList()
    }

    private fun processIntent() {
        this.userId = this.intent.getLongExtra("userId", 0)
    }

    private fun generateWorkoutList() {
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val header = findViewById<TextView>(R.id.textViewHeader)
        header.text = "Twoje treningi"

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val logicalDensity = metrics.density

        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(Math.ceil((10 * logicalDensity).toDouble()).toInt(), 0,
                Math.ceil((10 * logicalDensity).toDouble()).toInt(), 0)
        scrollView.addView(layout)

        val dbContext = GymAssistantDatabase.getInstance(this)
        var workouts = if (userId != 0.toLong()) {
            dbContext!!.workoutDao().getForUser(userId)
        } else {
            dbContext!!.workoutDao().getAll()
        }

        if(workouts.isEmpty()) {
            Toast.makeText(this, "Brak zapisanych treningów", Toast.LENGTH_LONG).show()
        }

        workouts.forEach {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
            val date = dateFormat.format(Date(it.date!!))
            var buttonText = date
            val button = Button(applicationContext)
            button.height = Math.ceil((60 * logicalDensity).toDouble()).toInt()
            button.background = applicationContext.getDrawable(R.drawable.bottom_border)
            button.text = buttonText
            val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
            button.typeface = typeface
            val workoutId = it.id

            button.setOnClickListener {
                val intent = Intent(applicationContext, SegmentHistoryActivity::class.java)
                intent.putExtra("workoutId", workoutId)
                startActivity(intent)
            }
            layout.addView(button)
        }
    }
}