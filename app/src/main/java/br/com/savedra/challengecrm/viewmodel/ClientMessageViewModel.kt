package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.model.Message
import br.com.savedra.challengecrm.model.MessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class ClientMessageViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _selectedFilter = MutableStateFlow(MessageFilter.ALL)
    val selectedFilter: StateFlow<MessageFilter> = _selectedFilter.asStateFlow()

    private val _filteredMessages = MutableStateFlow<List<Message>>(emptyList())
    val filteredMessages: StateFlow<List<Message>> = _filteredMessages.asStateFlow()

    init {
        loadMockMessages()
    }

    private fun loadMockMessages() {
        val mockMessages = listOf(
            Message(
                id = "1",
                content = "Olá! Como posso ajudar?",
                sender = "Operador Silva",
                subject = "Re: Dúvida sobre fatura",
                timestamp = Date(System.currentTimeMillis()),
                type = MessageType.MESSAGE,
                isRead = false
            ),
            Message(
                id = "2",
                content = "Nova Promoção de Aniversário! Aproveite nossos descontos especiais.",
                sender = "Equipe de Marketing",
                subject = "Nova Promoção de Aniversário!",
                timestamp = Date(System.currentTimeMillis() - 86400000), // Ontem
                type = MessageType.CAMPAIGN,
                isRead = true
            ),
            Message(
                id = "3",
                content = "Seu chamado foi atualizado e está sendo processado.",
                sender = "Suporte Técnico",
                subject = "Seu chamado foi atualizado",
                timestamp = Date(System.currentTimeMillis() - 172800000), // 2 dias atrás
                type = MessageType.MESSAGE,
                isRead = true
            )
        )
        _messages.value = mockMessages
        filterMessages()
    }

    fun setFilter(filter: MessageFilter) {
        _selectedFilter.value = filter
        filterMessages()
    }

    private fun filterMessages() {
        val filter = _selectedFilter.value
        val filtered = when (filter) {
            MessageFilter.ALL -> _messages.value
            MessageFilter.MESSAGES -> _messages.value.filter { it.type == MessageType.MESSAGE }
            MessageFilter.CAMPAIGNS -> _messages.value.filter { it.type == MessageType.CAMPAIGN }
        }
        _filteredMessages.value = filtered.sortedByDescending { it.timestamp }
    }

    fun markAsRead(messageId: String) {
        viewModelScope.launch {
            val updatedMessages = _messages.value.map { message ->
                if (message.id == messageId) {
                    message.copy(isRead = true)
                } else {
                    message
                }
            }
            _messages.value = updatedMessages
            filterMessages()
        }
    }

    fun sendMessage(content: String, sender: String = "Cliente") {
        viewModelScope.launch {
            val newMessage = Message(
                id = UUID.randomUUID().toString(),
                content = content,
                sender = sender,
                subject = "Nova mensagem",
                timestamp = Date(),
                type = MessageType.MESSAGE,
                isRead = true
            )
            _messages.value = _messages.value + newMessage
            filterMessages()
        }
    }
}

enum class MessageFilter {
    ALL,
    MESSAGES,
    CAMPAIGNS
}
