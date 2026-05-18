package br.com.savedra.challengecrm.model

/**
 * Modelo de Promoção para o Android.
 */
data class Promotion(
  val id: String? = null,
  val title: String? = "",
  val description: String? = "",
  val originalValue: String? = "",
  val promotionValue: String? = "",
  val dateExpiresIn: String? = "",
  val hoursExpiresIn: String? = "",
  val segment: String? = ""
)
