# Bug Tracker - Challenge CRM (Sprint 2)

## ✅ Bugs Consertados (Aprovados pelo Luca)
| ID | Descrição | Observação |
| :--- | :--- | :--- |
| **B12** | Data Término deve ser maior que Início | Validado pelo Luca. |
| **B13** | Histórico de Chat Sumindo | **FIXADO:** Unificação de IDs de sala (A-Z) e correção de persistência MongoDB. |
| **B14** | Inbox (Destaques/Campanhas) | **FIXADO:** Modais de detalhes funcionando. |
| **B09** | Notas do Operador não salvam | **FIXADO:** Saneamento de estado no Viewmodel e persistência validada. |
| **B16** | Chat Flickering (Operador) | **FIXADO:** Estado limpo ao trocar de sala. |
| **B22** | Tela Branca / Carregamento Chat | **FIXADO:** Lógica de espera por dados de usuários na navegação. |
| **G01** | Governança: Criação de Operador | **IMPLEMENTADO:** FAB exclusivo para equipe logada. |

## 🛠️ Bugs em Verificação (Aguardando Teste Final)
| ID | Descrição | Status | O que testar? |
| :--- | :--- | :--- | :--- |
| **B24** | Sincronia Real-Time (Polling) | 🛠️ AGUARDANDO | Recebimento de mensagens em < 5s sem refresh manual. |
| **P360** | Perfil 360 do Cliente | 🛠️ AGUARDANDO | Visualização de histórico recente no modal. |

## ⏳ Bugs Restantes (Ainda não atacados)
- **B15:** Notificação Push (Aviso sonoro/visual no topo do Android).


---

## 🎯 Objetivos Sprint 2 (Checklist de Verificação)
- [x] Login e Registro via REST API (Admin e Cliente).
- [x] CRUD completo (Banners, Campanhas, Convites, Promoções).
- [x] Chat 1:1 e Segmento com persistência real.
- [x] Layout adaptativo (Sem overlapping).
- [x] UX Fluida (Tab navigation e Atalhos de Login).
