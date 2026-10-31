# NPC-E-Inimigos

Aplicativo Android independente para catalogar e, nas próximas etapas, permitir controlar NPCs e criaturas inspirados nos templates de Swordigo. Este projeto usa o pacote `com.creatures.npclab`, separado do aplicativo original.

## Base Android adicionada

- Projeto Android nativo Java (Android Gradle Plugin 8.7.3, Gradle 8.9, Java 17, SDK 35).
- Tela de catálogo com pesquisa por nome de template.
- Inventário local com 81 templates analisados.
- GitHub Actions para compilar e publicar automaticamente um APK `debug` como artefato.

## Gerar APK

O workflow **Android APK** inicia em cada push para `main`, em pull requests ou manualmente pela aba **Actions**. Baixe o artefato `NPC-E-Inimigos-debug-apk` na execução concluída. Localmente, com Android SDK 35 e Gradle 8.9 instalados, execute:

```bash
gradle assembleDebug
```

Saída: `app/build/outputs/apk/debug/app-debug.apk`.

## Estado funcional

**Esta entrega é uma base Android e um catálogo, não o jogo jogável completo.** Ainda não há runtime 3D integrado nem conversão dos 81 templates de IA em personagens controláveis. A implementação e validação do primeiro personagem vêm antes da expansão para as demais famílias.

Veja [a análise de comportamento e viabilidade](docs/ANALISE_COMPORTAMENTO_E_VIABILIDADE.md) e o [plano da base Android](docs/BASE_ANDROID_E_PLANO_DE_IMPLEMENTACAO.md).
