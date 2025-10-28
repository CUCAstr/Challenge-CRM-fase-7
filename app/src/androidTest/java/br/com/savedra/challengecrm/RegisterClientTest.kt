package br.com.savedra.challengecrm

import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.savedra.challengecrm.viewmodel.AuthViewModel
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class RegisterClientTest {

    @Test
    fun registerNewClient() {
        val latch = CountDownLatch(1)
        val authViewModel = AuthViewModel()

        val names = listOf(
            "Nicolas Cage", "Bob Marley", "Travis Scott", "Kenan e Kel",
            "Jesus Cristo", "Taylor Swift", "Vinicius 13", "Maria Joaquina",
            "Cirilo Rodrigo", "Alexandre o Grande", "Jorge Caetano",
            "Rodrigo Faro", "Celso Portiolli", "Luciano Hulk", "Nikola Tesla",
            "Albert Einstein", "Oppenheimer"
        )
        val name = names.random()
        val randomNumber = Random.nextInt(1000, 9999)
        val email = "email.teste${randomNumber}@cliente.com"
        val password = "123456"
        val companys = listOf(
            "Acer", "BIC", "ESPM", "Lenovo", "Sem Parar", "Enel", "Sabesp",
            "Google", "IGN", "Forth Jardim", "Cipatex", "Petrobras", "Epic Games",
            "Mihoyo", "Valve", "Adidas", "Puma", "Nike", "Positivo", "Apple",
            "Sony", "Xeon"
        )
        val company = companys.random()
        val segments = listOf(
            "ED",
            "IT",
            "Retail & Financial",
            "GRC",
            "HR",
            "Smart Spends",
            "Health",
            "CSC",
            "Field Marketing",
            "Finance",
            "ESG",
            "CX"
        )
        val segment = segments.random()

        authViewModel.registerTestUser(
            name = name,
            email = email,
            password = password,
            company = company,
            segment = segment
        ) { latch.countDown() }

        latch.await(10, TimeUnit.SECONDS)
    }
}