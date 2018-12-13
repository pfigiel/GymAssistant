package com.example.dev.gymassistantv2.DAOs

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.dev.gymassistantv2.Entities.Invitation
import com.example.dev.gymassistantv2.Entities.User

@Dao
interface InvitationDAO {
    @Query("SELECT * FROM Invitation")
    fun getAll(): List<Invitation>

    @Query("SELECT * FROM Invitation inv WHERE inv.senderId == :senderId")
    fun getForSender(senderId: Long): Invitation

    @Query("SELECT * FROM Invitation inv WHERE inv.recipientId == :recipientId")
    fun getForRecipient(recipientId: Long): Invitation

    @Insert(onConflict = REPLACE)
    fun insert(invitation: Invitation)

    @Update
    fun update(invitation: Invitation)

    @Delete
    fun delete(invitation: Invitation)
}