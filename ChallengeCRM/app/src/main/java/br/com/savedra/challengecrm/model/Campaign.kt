package br.com.savedra.challengecrm.model

/**
 * Modelo de Campanha para o Android.
 */
data class Campaign(
  val id: String? = null,
  val title: String? = "",
  val description: String? = "",
  val startDate: String? = "",
  val endDate: String? = "",
  val segment: String? = ""
)
