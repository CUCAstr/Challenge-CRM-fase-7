# Sprint 1: Protótipo Funcional Android com Integração Firebase

## Objetivo Central
Construir um aplicativo Android nativo em Kotlin que seja funcional e validável. Ao final da Sprint, teremos um APK que permite a autenticação de clientes e operadores, a troca de mensagens em tempo real, e a visualização de listas de clientes e campanhas, tudo conectado e persistido na nuvem através do Firebase.

## Tecnologia Principal: Firebase
Utilizaremos os seguintes serviços:
- **Firebase Authentication:** Para gerenciar o login de operadores e clientes.
- **Cloud Firestore:** Como nosso banco de dados NoSQL em tempo real para armazenar clientes, mensagens, campanhas e anotações.
- **Firebase Cloud Messaging (FCM):** Para enviar as notificações push para novas mensagens.

---

## Distribuição de Funções e Plano de Ação Detalhado
Divisão de tarefas pensada para que o time trabalhe em paralelo, minimizando bloqueios. A comunicação constante e o uso de um sistema de controle de versão (Git) são fundamentais.

### Função 1: Juan - Arquiteto de Software e Autenticação
Juan será o responsável por criar a fundação do projeto. O trabalho dele é o ponto de partida para todos os outros.

1. **Extras:**
    - Padronizar a tela de registro (Localidade por extenso).
    - Excluir cliente VIP da tela de registro (deixar o operador selecionar quem é VIP)
    - Todos clientes começarem sendo "Não VIPs"

### Função 2: Thaysa - Visão do Operador (Módulo CRM)
Thaysa focará na principal ferramenta do operador: a lista de clientes. Ela começará a trabalhar assim que Juan tiver a estrutura base do projeto pronta.

1. **Extras:**
    - Selecionar quem é cliente VIP.
    - Enviar os convites e campanhas por segmentação.
    - Excluir o formulário de "mensagens" (deixar pra enviar direto do chat).
    - Arrumar bug do filtro (ao mudar de página a filtragem permanece, deveria voltar ao estado normal aparecendo todos clientes).

### Função 3: Luca - Visão do Cliente (Campanhas e Mensagens)
Luca cuidará da experiência do cliente, garantindo que ele receba e visualize as comunicações enviadas pelo operador.

1. **Extras:**
    - Criar os filtros da caixa de entrada ("Campanha", "Convites" e "Mensagens").
    - Criar um dropdown para filtrar mensagens "Lidas" e "Não lidas".
    - As mensagens lidas devem ficar com uma cor cinza, ao invés de totalmente branca.
    - Redirecionar ao chat com o operador ao clicar na mensagem.
    - Se der tempo, permitir troca de mensagens entre operador e cliente.

### Função 4: Pedro - Chat Integrado e Notificações Push
Pedro tem a missão crítica de fazer a comunicação em tempo real funcionar, incluindo o "alerta" de novas mensagens.

**Tarefas Detalhadas:**
1. **Extras:**
    - Notificações de mensagens, convites ou campanhas novas ao abrir o app (push e pop-up).
    - Chat integrado com cada operador e cliente, servindo como histórico de mensagens.

### Função 5: Alessandro - Usabilidade Avançada e Campanhas Express
Alessandro ficará com as funcionalidades que dão o "toque especial" ao app, focando na experiência do usuário e na funcionalidade de envio rápido.

**Tarefas Detalhadas:**
1.  **Gestos Inteligentes (Dia 5-7):**
    - Trabalhando em conjunto com Pedro e Luca, Alessandro implementará gestos na lista de mensagens/chat.
    - Usando o modificador `pointerInput` do Jetpack Compose, ele adicionará a funcionalidade de deslizar um item da lista para o lado para acionar uma ação (ex: "marcar como importante", que alteraria um campo booleano no documento da mensagem no Firestore).
2.  **Comandos Rápidos "/" (Dia 6-8):**
    - Na tela de chat de Pedro, Alessandro adicionará uma lógica no `TextField` de envio. Se o texto começar com "/", o app pode exibir uma pequena lista de comandos (`/promo`, `/agradecer`).
    - Ao selecionar um comando, ele pode preencher o campo de texto com uma mensagem-padrão, que será enviada normalmente.
3.  **Deeplinks Internos (Dia 7-9):**
    - Configurar deeplinks usando o Navigation Component.
    - O objetivo é que uma notificação push (enviada por Pedro para teste) possa conter um link que, ao ser clicado, não apenas abra o app, mas navegue diretamente para uma tela específica, como o perfil de um cliente ou uma conversa.