package com.example.dev.gymassistantv2.Database

import com.example.dev.gymassistantv2.Entities.Exercise
import com.example.dev.gymassistantv2.Entities.MuscleGroup
import com.example.dev.gymassistantv2.Entities.User
import java.util.*

class DBInitializer(private val dbContext : GymAssistantDatabase?) {

    private val williamsFbId = 109931923389001
    private val williamsId = 1.toLong()

    fun populateMuscleGroup() {
        if(dbContext!!.muscleGroupDao().getAll().isEmpty()) {
            var muscleGroups = mutableListOf<MuscleGroup>()
            var muscleGroupNames = listOf("Barki", "Biceps", "Triceps", "Klatka piersiowa",
                    "Grzbiet", "Brzuch", "Uda", "Łydki")
            muscleGroupNames.forEach {
                muscleGroups.add(MuscleGroup(null, it))
            }
            dbContext.muscleGroupDao().insert(muscleGroups)
        }
    }

    fun populateExercise() {
        if(dbContext!!.exerciseDao().getDefault().isEmpty()) {
            var exercises = mutableListOf<Exercise>()

            var exercise1 = Exercise()
            exercise1.name = "Wyciskanie sztangi nad głową"
            exercise1.defaultExercise = 1
            var muscleGroup = dbContext!!.muscleGroupDao().getByName("Barki")
            exercise1.muscleGroupId = muscleGroup.id
            exercises.add(exercise1)

            var exercise2 = Exercise()
            exercise2.name = "Podnoszenie hantli w uchwycie młotkowym"
            exercise2.defaultExercise = 1
            muscleGroup = dbContext!!.muscleGroupDao().getByName("Biceps")
            exercise2.muscleGroupId = muscleGroup.id
            exercises.add(exercise2)

            dbContext.exerciseDao().insert(exercises)
        }
    }

    fun populateTrainers() {
        if(dbContext!!.userDao().getAllTrainers().isEmpty()) {
            for (i in 1..5) {
                dbContext.userDao().insert(User(Random().nextLong(), true))
            }
        }
    }

    fun populateChargesForWilliam() {
        for (i in 1..5) {
            dbContext!!.userDao().insert(User(Random().nextLong(), false, williamsId))
        }
    }
}