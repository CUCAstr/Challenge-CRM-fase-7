package br.com.savedra.challengecrm.model

/**
 * Modelo de Banner para o Android.
 */
data class Banner(
  val id: String? = null,
  val title: String? = "",
  val description: String? = "",
  val imageUrl: String? = "",
  val segment: String? = ""
)
