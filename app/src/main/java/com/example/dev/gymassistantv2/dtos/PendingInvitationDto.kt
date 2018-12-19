package com.example.dev.gymassistantv2.dtos

import com.example.dev.gymassistantv2.entities.Invitation
import java.io.Serializable

class PendingInvitationDto (var id: Long?, var senderId: Long?, var recipientId: Long?, var date: Long?): Serializable
{
    constructor(invitation: Invitation):this(id = invitation.id, senderId = invitation.senderId, recipientId = invitation.recipientId, date = invitation.date)
    constructor():this(id = null, senderId = null, recipientId = null, date = null)
}