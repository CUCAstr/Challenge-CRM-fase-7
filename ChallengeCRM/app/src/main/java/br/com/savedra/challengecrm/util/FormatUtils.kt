package br.com.savedra.challengecrm.util

/**
 * Utilitários para formatação de dados na UI.
 */
object FormatUtils {
    /**
     * Formata uma string de data (DDMMYYYY) para DD/MM/AAAA.
     */
    fun formatDate(rawDate: String?): String {
        if (rawDate == null || rawDate.length != 8) return rawDate ?: ""
        return "${rawDate.substring(0, 2)}/${rawDate.substring(2, 4)}/${rawDate.substring(4)}"
    }

    /**
     * Formata uma string de hora (HHMM) para HH:MM.
     */
    fun formatTime(rawTime: String?): String {
        if (rawTime == null || rawTime.length != 4) return rawTime ?: ""
        return "${rawTime.substring(0, 2)}:${rawTime.substring(2)}"
    }
}
