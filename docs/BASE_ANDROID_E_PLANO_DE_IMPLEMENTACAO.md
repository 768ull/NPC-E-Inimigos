# Base Android e plano de implementação

## O que esta base entrega

- Projeto Android nativo em Java, sem dependências de UI de terceiros.
- Identificador de pacote independente: `com.creatures.npclab` (não substitui nem atualiza `com.touchfoo.swordigo`).
- Catálogo local em `app/src/main/assets/creature_catalog.json` com os 81 identificadores de template inventariados. A análise de controladores, comportamentos e IDs de Skill permanece na documentação de viabilidade.
- Tela inicial com pesquisa por nome de template.
- Pipeline GitHub Actions para compilar `assembleDebug`, verificar que o APK foi produzido e publicar o arquivo como artefato do workflow.

## Limites desta versão

Esta é a fundação do aplicativo e do processo de build. A tela de catálogo não executa o jogo original, não renderiza modelos 3D e não torna os NPCs controláveis. Não há ainda runtime de Swordigo integrado, conversão de IA para entrada do jogador, animações jogáveis, física/colisão, combate, câmera de jogo, áudio ou fases carregáveis. Não há APK compilado e validado neste ambiente.

## Arquitetura de integração planejada

1. **Runtime e carregamento:** identificar um runtime Android compatível e uma forma reproduzível de carregar cenas, modelos, animações e recursos sem depender da instalação do jogo original.
2. **Personagem-piloto:** validar `skelly_dw` como candidato técnico inicial porque o template auditado já apresenta `CharController` e `CharAnimController`; é necessário desativar/substituir sua IA (`SkellyMonsterController`) com segurança.
3. **Contrato de controle:** camada comum para movimento, salto, direção, ataque, skill, vida, morte/respawn e câmera; cada família de criatura fornece adaptadores para suas diferenças.
4. **Famílias de comportamento:** implementar e validar separadamente patrulha, investida, salto/ricochete, voo, projéteis, armadilhas, armas corpo a corpo e conjuração.
5. **Validação Android:** testar em emulador e aparelho ARM64, incluindo toque, desempenho, ciclo de vida, áudio, colisões e persistência.
6. **Expansão:** só depois do piloto estável, habilitar o restante do catálogo por família e registrar quais ações cada template suporta.

## Build

O build automático é executado em push para `main`, pull request ou manualmente em **Actions → Android APK → Run workflow**. O arquivo resultante é `app/build/outputs/apk/debug/app-debug.apk` e também fica disponível no artefato `NPC-E-Inimigos-debug-apk` do workflow. É um APK `debug`, não uma versão de distribuição assinada para Play Store.

## Identificador do pacote

`applicationId` e `namespace`: `com.creatures.npclab`.