package br.com.savedra.challengecrm.model

/**
 * Modelo de Convite para o Android.
 */
data class Invite(
  val id: String? = null,
  val title: String? = "",
  val description: String? = "",
  val date: String? = "",
  val time: String? = "",
  val location: String? = "",
  val segment: String? = ""
)
