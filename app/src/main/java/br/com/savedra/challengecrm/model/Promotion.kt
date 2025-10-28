package br.com.savedra.challengecrm.model

data class Promotion(
  val title: String = "",
  val description: String = "",
  val originalValue: String = "",
  val promotionValue: String = "",
  val dateExpiresIn: String = "",
  val hoursExpiresIn: String = ""
)
