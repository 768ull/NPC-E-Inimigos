# NPC-E-Inimigos — mod do Swordigo original

**Este projeto NÃO é um jogo novo.** O objetivo é modificar o próprio Swordigo 1.4.13 para que o jogador possa selecionar e controlar NPCs, inimigos e criaturas dentro das fases, cenários e sistemas originais do jogo.

O APK final deverá preservar o conteúdo e o runtime originais do Swordigo (modelos, cenas, animações, sons, colisões, armas, habilidades e progressão), adicionando a camada de seleção/controle de personagens. Para coexistir com o Swordigo instalado, a versão modificada será empacotada com identificador Android separado, após a alteração do manifesto ser validada.

## Fonte original do jogo

O arquivo-fonte fornecido pelo usuário é `Swordigo.apk`. Ele foi validado localmente nesta etapa:

- SHA-256: `f2dc26944f61a3bea73ca00bb93ea20f6abac0c0c9215c4b63b48b37ed6afe3e2`
- Tamanho: 72.340.412 bytes
- Integridade ZIP: aprovada
- Recursos em `assets/resources/`: 928 arquivos
- Conteúdo localizado: 47 `.scl`, 116 `.scene`, 394 `.pod`, 256 `.pvr`, 71 `.wav`, além de outros formatos.

Os binários e recursos proprietários do jogo **não são incluídos neste repositório público**. Eles permanecem como entrada local do processo de modificação; não se deve redistribuir o APK original no GitHub público.

## Preparar os arquivos originais

Coloque o APK fornecido em `original/Swordigo.apk` e execute:

```bash
python3 scripts/prepare_original_game.py --input original/Swordigo.apk --work-dir build/original
```

O script valida os arquivos essenciais e extrai os 928 recursos originais para `build/original/assets/resources/`, gerando `build/original/original_game_inventory.json` com hashes e inventário.

Também aceita um APKM que contenha `base.apk`; nesse caso, a integridade do arquivo APKM e da entrada extraída é verificada antes de continuar.

## Análise dos personagens

- [Análise de comportamento e viabilidade dos 81 templates](docs/ANALISE_COMPORTAMENTO_E_VIABILIDADE.md)
- [Plano de modificação do jogo original](docs/PLANO_MODIFICACAO_SWORDIGO_ORIGINAL.md)
- [Pasta de patches](patches/README.md)

## Estado real do desenvolvimento

A base Android independente que existia anteriormente foi removida porque não atendia ao objetivo. O repositório agora está orientado a trabalhar sobre os arquivos do jogo original.

**Ainda não há um patch funcional que converta os 81 templates em personagens controláveis, nem APK modificado final validado.** O próximo bloqueio técnico é implementar e testar no runtime original a substituição da IA por controle do jogador, começando pelo template `skelly_dw` e depois expandindo por famílias de controladores. O pacote final só deve ser montado depois de validar entrada, câmera, animações, colisão, ataque, habilidades e assinatura Android.
