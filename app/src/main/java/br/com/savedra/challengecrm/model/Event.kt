package br.com.savedra.challengecrm.model

data class Event(
    val eventName: String = "",
    val eventDate: String = "",
    val segment: String = "",
    val numberOfPeople: String = "",
    val eventSetup: String = "",
    val audienceProfile: String = "",
    val knowHotel: Boolean? = null,
    val firstTimeEvent: Boolean? = null,
    val eventPeriodicity: String = "",
    val competingWith: String = "",
    val budget: String = "",
    val dateFlexibility: Boolean? = null,
    val accommodationDemand: Boolean? = null,
    val numberOfApartments: String = "",
    val decisiveFactor: String = "",
    val necessaryDifferential: String = "",
    val internalPartnersContact: Boolean? = null
)