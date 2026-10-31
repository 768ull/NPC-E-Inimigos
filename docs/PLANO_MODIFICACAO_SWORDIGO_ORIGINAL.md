# Plano: controlar NPCs e criaturas no Swordigo original

## Objetivo correto

A entrega é um **mod/clone instalável do Swordigo original**, não uma reimplementação independente. O jogo original continua sendo o jogo executado. A modificação acrescenta um seletor de personagem e associa a entidade escolhida ao controle do jogador, preservando o conteúdo e os sistemas originais sempre que possível.

## Arquivos originais já validados

O arquivo `Swordigo.apk` fornecido pelo usuário foi lido e validado localmente. Ele contém:

- `assets/resources/monsters.scl`, `npc.scl`, `npc_new.scl`, `hiro.scl`, `weapons.scl`;
- cenas originais como `hero.scene` e demais arquivos `.scene`;
- modelos/recursos em arquivos `.pod`, texturas `.pvr`, sons `.wav`, fontes, atlas e outros recursos;
- três DEX com o código Android do jogo.

O teste ZIP do APK passou. Foram encontrados 928 arquivos em `assets/resources/`: 47 SCL, 116 SCENE, 394 POD, 256 PVR, 71 WAV e outros formatos. Esses dados foram extraídos localmente para desenvolvimento, sem copiar os recursos comerciais para o repositório público.

O APKM enviado também foi inspecionado como pacote de splits (base, ARM64, ARMv7, idioma e densidade), mas o arquivo recebido apresentou erro de descompressão ao ler uma entrada. Por isso, a fonte primária desta etapa é o APK `Swordigo.apk` íntegro; o APKM precisa ser validado novamente antes de usá-lo para reconstruir todos os splits.

## Como a modificação deve funcionar

1. **Carregar a origem:** usar o APK original como base, mantendo cenas, modelos, texturas, sons, scripts e código do jogo.
2. **Entender o runtime:** mapear o fluxo real de criação do herói, entrada, câmera, saúde, mana, dano e animações no código Android/Dex e nos arquivos SCL.
3. **Converter uma entidade-piloto:** testar `skelly_dw`, que já contém `CharController` e `CharAnimController`, substituindo sua IA `SkellyMonsterController` por controle do jogador sem perder colisão/animação.
4. **Criar seletor dentro do jogo:** abrir a seleção no contexto do jogo original e trocar o objeto controlado sem substituir as fases nem a mecânica central.
5. **Adaptar por família:** walkers, inimigos armados, magos, atiradores, criaturas de investida, saltadores, voadores, armadilhas e chefes.
6. **Empacotar o clone:** preservar os arquivos originais não modificados, aplicar apenas os patches verificados, alterar o identificador do pacote para coexistir com o original e assinar todos os APKs/splits com a mesma chave de modificação.
7. **Validar em Android:** instalar sem remover o Swordigo original e testar menu, fases, câmera, controles, habilidades, colisões, salvamento e retorno ao jogo.

## Por que os recursos originais não ficam no GitHub público

O APK contém recursos proprietários do jogo. O repositório armazena scripts e diferenças de modificação, não uma cópia pública do jogo completo. O arquivo original fornecido fica como entrada local do processo de build. Assim, é possível reproduzir a modificação sem republicar os binários comerciais.

## Estado atual / bloqueios

- [x] Localizar o APK original fornecido.
- [x] Validar integridade ZIP do APK original.
- [x] Confirmar os recursos reais e os arquivos SCL/cenas.
- [x] Extrair localmente os 928 recursos para análise.
- [ ] Mapear as classes de runtime responsáveis pelo herói e pelo input no APK original.
- [ ] Implementar um primeiro personagem jogável dentro do runtime original.
- [ ] Testar câmera, física, animações, combate e habilidades.
- [ ] Implementar o seletor de personagem.
- [ ] Ajustar o identificador do pacote e assinar o clone.
- [ ] Gerar e validar APK/APKM final em aparelho Android.

**Não existe ainda APK final jogável.** Um catálogo separado não cumpre a proposta e não será tratado como entrega do projeto.
