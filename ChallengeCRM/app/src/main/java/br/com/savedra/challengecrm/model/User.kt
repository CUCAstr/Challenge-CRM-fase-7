package br.com.savedra.challengecrm.model

import java.util.Date

/**
 * Modelo de Usuário utilizado no Aplicativo Android.
 * 
 * CORREÇÃO: Todos os campos agora permitem valores NULOS (String?).
 * Isso evita crashes de "non-null is null" quando o backend retorna campos vazios
 * (especialmente ao usar a função .copy() no Kotlin).
 */
data class User(
  val id: String? = "",
  val name: String? = "",
  val company: String? = "",
  val email: String? = "",
  val role: String? = "",
  val segment: String? = "",
  val score: Int? = 0,
  val status: String? = "",
  val memberSince: Date? = null,
  val notes: String? = "",
  val gender: String? = "",
  val phone: String? = "",
  val category: String? = ""
)
