package br.com.savedra.challengecrm

import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.savedra.challengecrm.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateClientTest {

    private val authRepository = AuthRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    @Test
    fun createClientsForSegments() {
        runBlocking {
            val segments = listOf(
                "ED", "IT", "Retail & Financial", "GRC", "HR", "Smart Spends",
                "Health", "CSC", "Field Marketing", "Finance", "ESG", "CX"
            )

            segments.forEach { segment ->
                val email = "${segment.lowercase().replace(" & ", "_").replace(" ", "_")}@cliente.com"
                val password = "123456"
                val name = "Cliente $segment"
                val company = "Empresa $segment"
                val role = "Cliente"
                val gender = "Outro"
                val phone = "123456789"
                val category = "Basico"

                authRepository.register(email, password, name, company, role, segment, gender, phone, category)
            }
        }
    }
}