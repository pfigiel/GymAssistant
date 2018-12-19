package com.example.dev.gymassistantv2

import android.app.Activity
import android.os.Bundle
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.widget.*
import com.example.dev.gymassistantv2.dtos.UserDto
import java.sql.Date
import java.text.SimpleDateFormat


class ManageMeasurementsActivity : Activity() {

    private lateinit var loggedUser: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generic_history_layout)

        processIntent()
        generateMeasurementsList(setLayout())
    }

    private fun setLayout(): LinearLayout {
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val header = findViewById<TextView>(R.id.textViewHeader)
        header.text = "Twoje pomiary"
        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL
        scrollView.addView(layout)
        return layout
    }

    private fun processIntent() {
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    private fun generateMeasurementsList(layout: LinearLayout) {

        layout.removeAllViews()
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val dbContext = GymAssistantDatabase.getInstance(this)
        val measurements = dbContext!!.measurementDao().getForUser(loggedUser.userId!!)

        if (measurements.isEmpty()) {
            Toast.makeText(this, "Brak zapisanych pomiarów", Toast.LENGTH_LONG).show()
        }

        measurements.forEach {
            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL

            val bodyPartName = dbContext!!.muscleGroupDao().getById(it.bodyPartId!!).name
            val measurementValue = it.value.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val measurementDate = dateFormat.format(Date(it.date!!))

            val buttonText = bodyPartName + ": " + measurementValue + "cm" + "\n Data pomiaru: " + measurementDate
            val buttonMain = Button(this)
            buttonMain.layoutParams = LinearLayout.LayoutParams(
                    (metrics.widthPixels * 0.8).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
            buttonMain.background = applicationContext.getDrawable(R.drawable.bottom_border)
            buttonMain.text = buttonText
            val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
            buttonMain.typeface = typeface
            buttonMain.measure(windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)

            val buttonDelete = Button(this)
            buttonDelete.height = buttonMain.measuredHeight
            buttonDelete.width = (metrics.widthPixels * 0.2).toInt()
            buttonDelete.background = applicationContext.getDrawable(R.drawable.bottom_border_red)
            buttonDelete.text = "Usuń"
            buttonDelete.typeface = typeface
            val measurement = it
            buttonDelete.setOnClickListener {
                dbContext.measurementDao().delete(measurement)
                generateMeasurementsList(layout)
            }

            horizontalLayout.addView(buttonMain)
            horizontalLayout.addView(buttonDelete)
            layout.addView(horizontalLayout)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}