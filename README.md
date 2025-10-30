# WTC Connection

WTC Connection é um aplicativo de gerenciamento de relacionamento com o cliente (CRM) desenvolvido para a plataforma Android. Ele permite que os operadores gerenciem clientes, enviem campanhas de marketing e se comuniquem por meio de um sistema de chat integrado. O aplicativo foi desenvolvido com Kotlin e Jetpack Compose, utilizando Firebase para serviços de backend.

## Funcionalidades

### Para Operadores
- **Autenticação de Operador:** Login seguro para operadores.
- **Dashboard de Clientes:** Visualize e gerencie uma lista de todos os clientes cadastrados.
- **Chat Individual:** Inicie conversas e preste suporte individual a cada cliente.
- **Chat em Grupo por Segmento:** Crie e participe de chats com grupos de clientes segmentados (por exemplo, "clientes VIP", "novos clientes").
- **Gerenciamento de Campanhas:** Crie, edite e envie campanhas de marketing para todos os clientes ou para segmentos específicos.
- **Gerenciamento de Convites:** Crie e envie convites para eventos ou promoções especiais.
- **Gerenciamento de Promoções:** Divulgue promoções exclusivas para os clientes.
- **Gerenciamento de Banners:** Adicione ou atualize banners promocionais na tela inicial do cliente.

### Para Clientes
- **Autenticação de Cliente:** Login e registro para clientes.
- **Tela Inicial Dinâmica:** Visualize banners, promoções e outras informações relevantes.
- **Chat com Operadores:** Entre em contato com os operadores para suporte ou dúvidas.
- **Acesso a Conteúdos Exclusivos:**
  - **Centro de Eventos:** Peça para marcar um evento no Events Center da WTC SP.
  - **Business Club:** Solicite para se tornar um C-Level.
  - **Sheraton Hotel:** Solicite uma hospedagem em nosso hotel filiado.
- **Notificações Push:** Alertas instantâneos para novas mensagens de chat, campanhas e convites, mantendo os usuários sempre atualizados.
- **Pop-ups In-App:** Mensagens e alertas exibidos dentro do aplicativo para garantir que informações importantes sejam vistas imediatamente.

## Arquitetura

O projeto segue a arquitetura **MVVM (Model-View-ViewModel)**, que promove uma separação clara de responsabilidades e facilita a manutenção e testabilidade do código.

- **Model:** Representa os dados e a lógica de negócios. As classes de dados (como `User`, `Campaign`, `Message`) estão localizadas em `app/src/main/java/br/com/savedra/challengecrm/model/`.
- **View:** A camada de interface do usuário, construída com **Jetpack Compose**. As telas (views) estão em `app/src/main/java/br/com/savedra/challengecrm/ui/view/`.
- **ViewModel:** Atua como uma ponte entre o Model e a View, expondo os dados para a UI e tratando as interações do usuário. Os ViewModels estão em `app/src/main/java/br/com/savedra/challengecrm/viewmodel/`.

## Estrutura de Diretórios
\---br
    \---com
        \---savedra
            \---challengecrm
                |   MainActivity.kt
                |   
                +---data
                |   \---repository
                |           AuthRepository.kt
                |           BannerRepository.kt
                |           BusinessClubRepository.kt
                |           CampaignRepository.kt
                |           ChatRepository.kt
                |           EventRepository.kt
                |           InviteRepository.kt
                |           PromotionRepository.kt
                |           SheratonHotelRepository.kt
                |           
                +---di
                +---model
                |       Banner.kt
                |       BusinessClub.kt
                |       Campaign.kt
                |       Chat.kt
                |       EventsCenter.kt
                |       Invite.kt
                |       Promotion.kt
                |       SheratonHotel.kt
                |       User.kt
                |       
                +---navigation
                |       AppNavigation.kt
                |       
                +---ui
                |   +---theme
                |   |       Color.kt
                |   |       TextSnippets.kt
                |   |       Theme.kt
                |   |       Type.kt
                |   |       
                |   \---view
                |       |   BannersScreen.kt
                |       |   BusinessClubScreen.kt
                |       |   CampaignsScreen.kt
                |       |   ChatListScreen.kt
                |       |   ChatScreen.kt
                |       |   ClientHomeScreen.kt
                |       |   EventsCenterScreen.kt
                |       |   GroupChatScreen.kt
                |       |   InvitesScreen.kt
                |       |   LandingScreen.kt
                |       |   LoginScreen.kt
                |       |   OperatorHomeScreen.kt
                |       |   OperatorListScreen.kt
                |       |   PromotionsScreen.kt
                |       |   RegisterScreen.kt
                |       |   SegmentChatsScreen.kt
                |       |   SheratonHotelScreen.kt
                |       |   SplashScreen.kt
                |       |   
                |       +---dialogs
                |       |       DataPickerDialog.kt
                |       |       FilteredClientsDialog.kt
                |       |       TimePickerDialog.kt
                |       |       
                |       \---modals
                |               CreateBannerModal.kt
                |               CreateCampaignModal.kt
                |               CreateInviteModal.kt
                |               CreatePromotionModal.kt
                |               CustomerDetailsModal.kt
                |               InviteDetailsModal.kt
                |               
                \---viewmodel
                    AuthViewModel.kt
                    BannerViewModel.kt
                    BusinessViewModel.kt
                    CampaignViewModel.kt
                    ChatViewModel.kt
                    EventsCenterViewModel.kt
                    InviteViewModel.kt
                    OperatorViewModel.kt
                    PromotionViewModel.kt
                    SheratonHotelViewModel.kt
                    UsersViewModel.kt

## Tecnologias Utilizadas

- **Linguagem:** [Kotlin](https://kotlinlang.org/)
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Arquitetura:** MVVM
- **Backend (BaaS):** [Firebase](https://firebase.google.com/)
  - **Autenticação:** Firebase Authentication
  - **Banco de Dados:** Cloud Firestore
  - **Armazenamento:** Firebase Storage
  - **Notificações:** Firebase Cloud Messaging
- **Navegação:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

## Como Começar

### Pré-requisitos
- Android Studio (versão mais recente recomendada)
- JDK 11 ou superior
- Uma conta Firebase

### Configuração
1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/ChallengeCRM.git
   ```
2. **Configuração do Firebase:**
   - Crie um projeto no [console do Firebase](https://console.firebase.google.com/).
   - Adicione um aplicativo Android ao seu projeto Firebase com o nome do pacote `br.com.savedra.challengecrm`.
   - Faça o download do arquivo `google-services.json` e coloque-o no diretório `app/`.
   - Ative os seguintes serviços no console do Firebase:
     - **Authentication:** Ative o método de login por E-mail/Senha.
     - **Cloud Firestore:** Crie um banco de dados no modo de produção.
     - **Cloud Messaging:** Crie um sistema de notificações.
     - **Storage:** Configure o Cloud Storage.

### Credenciais para Teste
Para facilitar os testes, as seguintes credenciais podem ser usadas:

- **Acesso de Operador:**
  - **Email:** `teste@operador.com`
  - **Senha:** `123456`

- **Acesso de Cliente:**
  - Qualquer usuário listado na tela de operador terá a senha `123456`
  - Você pode criar um a partir da tela de registro com as informações que você deseja.

### Instalação.

### Build e Execução
1. Abra o projeto no Android Studio.
2. Aguarde o Gradle sincronizar as dependências.
3. Execute o aplicativo em um emulador ou dispositivo físico.

Você também pode compilar e instalar via linha de comando:

- **Compilar:**
  ```bash
  ./gradlew build
  ```
- **Instalar (Debug):**
  ```bash
  ./gradlew installDebug
  ```