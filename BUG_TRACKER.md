# Bug Tracker - Challenge CRM (Sprint 2)

## ✅ Bugs Consertados (Aprovados pelo Luca)
*Nenhum ainda. Aguardando confirmação dos testes atuais.*

## 🛠️ Bugs em Verificação (Tentativa de Fix aplicada - Teste na próxima sessão)
| ID | Descrição | Status | O que testar? |
| :--- | :--- | :--- | :--- |
| **B00** | Crash ao abrir o App (ConnectException) | 🛠️ Fix Aplicado | Verificar se o app abre normalmente mesmo se o servidor demorar. |
| **B01** | Botão "Salvar" vertical no Detalhe do Cliente | 🛠️ Fix Aplicado | Abrir detalhe do cliente e ver se os botões estão lado a lado. |
| **B02** | Histórico de Chat some ao sair/voltar | 🛠️ Fix Aplicado | Entrar no chat, enviar msg, sair da tela e voltar. |
| **B03** | Falta ícone de calendário/relógio nos Convites | 🛠️ Fix Aplicado | Abrir "Novo Convite" e ver se os ícones aparecem. |
| **B04** | Campo de data não deixa digitar (só clicar) | 🛠️ Fix Aplicado | Tentar escrever números da data manualmente. |
| **B05** | Tecla TAB física insere caractere | 🛠️ Fix Aplicado | Usar Tab no teclado físico para pular campos. |
| **B06** | Contraste ruim nos botões "Solicitar..." | 🛠️ Fix Aplicado | Ver se o texto nos botões de Eventos/Business está legível. |
| **B07** | Campanhas criadas não aparecem na lista | 🛠️ Fix Aplicado | Criar uma campanha e verificar se ela é listada imediatamente. |
| **B08** | Botão de Criar Operador sumiu | 🛠️ Fix Aplicado | Logado como operador, ir na lista de usuários e ver o "+". |
| **B09** | Notas do cliente não salvam no banco | 🛠️ Fix Aplicado | Escrever uma nota, salvar e reabrir os detalhes. |
| **B10** | Overlapping (Câmera/Botões Android) | 🛠️ Fix Aplicado | Verificar títulos atrás da câmera ou input atrás dos botões. |
| **B11** | Botão de Voltar faltando no Chat | 🛠️ Fix Aplicado | Verificar se o chat 1:1 e de grupo têm a setinha de voltar. |


## ⏳ Bugs Restantes (Ainda não atacados)
- **B12:** Validação em tempo real (Data Término deve ser maior que Início).
- **B13:** Cliente não vê histórico de mensagens (pode ser problema de permissão no backend).
- **B14:** Cliente não vê Banners/Campanhas (listas aparecem vazias).
- **B15:** Notificação Push (Aviso sonoro/visual no topo do Android).

---

## 🎯 Objetivos Sprint 2 (Checklist de Verificação)
- [ ] Login e Registro via REST API (Admin e Cliente).
- [ ] CRUD completo (Banners, Campanhas, Convites, Promoções).
- [ ] Chat 1:1 e Segmento com persistência.
- [ ] Layout adaptativo (Sem overlapping).
- [ ] UX Fluida (Tab navigation e Máscaras de entrada).
