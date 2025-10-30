package br.com.savedra.challengecrm.model

import com.google.firebase.firestore.DocumentId

data class BusinessClub(
  @DocumentId
  val id: String = "",
  val userId: String = "",
  val reason: String = "",
  val internalPartnersContact: Boolean? = null
)
