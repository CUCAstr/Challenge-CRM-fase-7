# Arquitetura MVVM (Model-View-ViewModel)

Esta organização é fundamental para criar aplicativos robustos, testáveis e fáceis de manter.

### O que significa cada diretório?

*   **`data`**: Camada de Dados.
    *   **Responsabilidade**: É a única fonte de verdade para os dados do seu aplicativo. Toda a lógica de acesso a dados (seja de uma rede, banco de dados local, etc.) fica aqui.
    *   **Conteúdo Típico**: Repositórios (`Repository`) e Fontes de Dados (`DataSource`).

*   **`di`**: Injeção de Dependência (Dependency Injection).
    *   **Responsabilidade**: Gerencia a forma como as classes são criadas e fornecidas umas às outras. Em vez de uma classe criar suas próprias dependências (ex: `ViewModel` criando seu `Repository`), elas são "injetadas" de fora.
    *   **Conteúdo Típico**: Módulos que "ensinam" o framework de injeção (como Hilt ou Koin) a construir os objetos.

*   **`model`**: Camada de Modelo.
    *   **Responsabilidade**: Define a estrutura dos dados do seu aplicativo. São as classes que representam os objetos com os quais você trabalha.
    *   **Conteúdo Típico**: `data class` em Kotlin.

*   **`ui`**: Camada de Interface do Usuário (User Interface).
    *   **Responsabilidade**: Contém tudo relacionado ao que o usuário vê e com o que ele interage. Em Jetpack Compose, são suas funções `@Composable`.
    *   **Subdiretório `view`**: Ajuda a organizar as diferentes telas ou componentes complexos da sua UI.

*   **`viewmodel`**: Camada ViewModel.
    *   **Responsabilidade**: Atua como uma ponte entre a camada de `data` e a `ui`. Prepara e gerencia os dados para a UI, sobrevive a mudanças de configuração (como rotação de tela) e reage a interações do usuário, delegando o trabalho para a camada de dados.

### Por que essa estruturação (MVVM) é a certa?

Este padrão de arquitetura, o **MVVM (Model-View-ViewModel)**, é o recomendado oficialmente pelo Google para desenvolvimento Android moderno. Os principais benefícios são:

1.  **Separação de Responsabilidades**: Cada camada tem um trabalho claro e distinto.
    *   A **View** (`ui`) apenas exibe os dados e notifica o ViewModel sobre as interações do usuário.
    *   O **ViewModel** contém a lógica de apresentação e o estado da UI, mas não sabe *como* os dados são exibidos.
    *   O **Model** (`data`) gerencia os dados e a lógica de negócio, sem se preocupar com quem vai usá-los.

2.  **Testabilidade**: Como as camadas são desacopladas, você pode testá-las de forma independente. É possível testar a lógica no seu `ViewModel` sem precisar de uma UI, ou testar seu `Repository` sem um `ViewModel`.

3.  **Manutenibilidade**: O código se torna muito mais fácil de entender, modificar e dar manutenção. Se você precisa mudar a fonte de dados, só mexe na camada `data`. Se precisa mudar o layout, só mexe na camada `ui`.

### O que significam os arquivos criados?

*   **`Lead.kt` (`model`)**: É a `data class` que define a estrutura de um "Lead" (um potencial cliente), com seus atributos como `id`, `name`, `email`, etc.

*   **`LeadRepository.kt` (`data/repository`)**: Classe responsável por buscar e gerenciar os dados dos `Lead`. Ela abstrai a origem dos dados. O `ViewModel` pede os dados ao `Repository` sem precisar saber se eles vêm de um banco de dados ou da internet.

*   **`LeadViewModel.kt` (`viewmodel`)**: Prepara e fornece os dados de `Lead` para a tela. Ele solicita os leads ao `LeadRepository` e os expõe para a `LeadScreen` observar e exibir.

*   **`LeadScreen.kt` (`ui/view`)**: É a função Composable que desenha a tela para exibir os leads. Ela recebe os dados do `LeadViewModel` e apenas se preocupa em como mostrá-los.

*   **`AppModule.kt` (`di`)**: É um arquivo de configuração para a injeção de dependência. É aqui que você diria a um framework como o Hilt: "Quando alguém precisar de um `LeadRepository`, é assim que você deve criá-lo".

Em resumo, essa estrutura organiza seu projeto de forma lógica, tornando-o mais escalável e profissional.
