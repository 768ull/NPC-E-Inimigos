# Swordigo — análise de comportamento e viabilidade do modo “Jogar como NPC/criatura”
## Inventário verificado
- **81 templates** de personagens/NPCs/criaturas, excluindo o protagonista.
- **11 templates** no grupo `npc.scl`/`npc_new.scl` (10 humanoides e 1 critter).
- **55 templates** referenciados diretamente em cenas; **490 colocações estáticas** em 81 cenas.
- **9 templates** com componente `Skill`; **4** com `Attack`; **16** com `SwingableWeaponController`.
- 77 templates são `MonsterEntity`; 4 são `Entity` sem `MonsterEntity`.
> As 81 contagens são templates distintos, não o total de instâncias que podem surgir durante uma partida. Scripts Lua podem criar entidades extras em tempo de execução.
## Tipos de comportamento
- **Patrulha/caminhada (4):** walkingmonsteroldman, npc_male_walking_template, grasswalker, woodknight.
- **Investida (8):** bladeblob, corrupt_snort, elemental_ice, rolling_spirit, shadowspinner, snort, sprit, charging_golem.
- **Salto/ricochete (7):** fire, shadowblob, shadowblob_fire_little, shadowblob_little, spikebouncy, spikebouncy_follow, spikebouncy_roam.
- **Voo de morcegos (2):** bat, firebat.
- **Disparo por controller (1):** snart.
- **Mordida/armadilha (1):** carniplant.
Os grupos se sobrepõem: um template pode ter mais de um comportamento.
## Skills nomeadas
- **npc_healer** (`npc.scl`): `npc_healer_cast`.
- **firstboss** (`cinematic_beginning.scl`): `boss1_spellcast`.
- **dragonkin_1seeker** e **dragonkin_melee** (`dragonkin.scl`): `dragonkin_spell_cast`.
- **skeleton_mage** e **skeleton_mage_frost** (`monsters.scl`): `skeleton_spell_cast` e `skeleton_spell_cast_frost`.
- **varimage** e **varimage_red** (`playground.scl`): `varimage_spell_cast`.
- **woodkeep_boss2** (`woodkeep_bos.scl`): `boss1_spellcast`.
As skills são disparadas por `Skill.BeginCasting`, animadas por `AnimationController`/`KeyframeAnimation`, finalizadas por `Skill.FinishCasting` e podem anexar projéteis via propriedades do caster. Magos esqueletos param o movimento, viram-se para o alvo, lançam projéteis balísticos/de gelo, aguardam animação e retomam a velocidade. O mago de gelo pode lançar um segundo projétil. Dragonkin também invoca/acompanha um `seeker` e lança `dragonkin_fireball`.
## Ataques via componente Attack
- **icedwarf:** três ações: cria quatro `shadowblob_little`; prepara uma mudança de escala; cria e arremessa `club_projectile` na direção do alvo.
- **generic_shadowform**, **woodknight_mace** e **kakkapiikkipyörylă:** usam `GenericMonsterController` com `Attack`; a configuração do template não expõe script Lua direto nesse componente.
## Comportamento por controladores
- Inimigos armados usam `SwingableWeaponController`, `EntityAction` e animações de golpe com janelas de dano.
- Magos/conjuradores usam `Skill`, ações de conjuração e templates de projétil.
- Criaturas rápidas usam `ChargingMonsterController` ou `BouncingMonsterController`.
- Morcegos usam `BatMonsterController`; alguns outros perseguem via scripts/`EntityController`.
- `ShootingMonsterController` implementa disparos; projéteis podem ter gravidade e impacto.
- NPCs estáticos usam `StaticMonsterController`; outros executam `Program`/`EntityAction` para diálogo, interação e animação.
## Arquitetura e viabilidade
Na versão 1.4.13, `GameSceneController` mantém um único objeto `hero` e referências a `CharController`, `Entity`, `Health` e `Mana`; também sincroniza câmera, alvo, HUD e conjuração. `CreateHeroObjectAt` procura explicitamente o template `hiro`, atribui o identificador `hero` e cria componentes de vida/mana. `HeroEntityComponent` marca o protagonista e contém seu programa de controle.
Apenas `skelly_dw`, entre os templates auditados, combina `CharController` e `CharAnimController`, mas também possui `SkellyMonsterController` com IA. Os outros 80 não foram definidos como personagens controláveis pelo sistema padrão. Para cada família será necessário adaptar entrada de movimento, animações, colisão, armas/ataques, habilidade especial, vida/morte/respawn e câmera/HUD. Trocar apenas o modelo não basta.
## Pesquisa externa
- SwMini documenta Lua 5.1 para cutscenes, chefes e comportamentos. `Mini.RecreateHero` recria o herói atual para aplicar mudanças de modelo/arma; não converte qualquer `MonsterEntity` em jogador.
- SwordigoDesktop/SRE publica engenharia reversa de Swordigo 1.4.13: `GameSceneController`, `HeroEntityComponent` e `CharController`. É documentação para runtime desktop/Linux, não um APK Android pronto.
## Decisão desta etapa
**Nenhum APK jogável foi gerado nesta etapa.** Não existe ainda um hook validado que converta o objeto `hero` em cada template, nem foram testadas animações e ações no runtime ARM64. O próximo passo seguro é um protótipo controlável limitado a rigs compatíveis, começando por `skelly_dw` e um conjurador, e depois ampliar por famílias de controller.
## Referências
- https://github.com/ItsJustSomeDude/SwMini
- https://github.com/ItsJustSomeDude/SwMini/blob/main/app/src/main/cpp/lua_libs/mini/recreate_hero.c
- https://github.com/TheAevoraLabs/SwordigoDesktop/blob/master/docs/sre13/GameSceneController.md
- https://github.com/TheAevoraLabs/SwordigoDesktop/blob/master/docs/sre13/HeroEntityComponent.md
- https://github.com/TheAevoraLabs/SwordigoDesktop/blob/master/src/ruby/caver/char_controller.h
- https://swordigo.fandom.com/wiki/Enemy
## Catálogo detalhado
O CSV `Swordigo_analise_comportamento_81_personagens.csv` acompanha o material entregue ao usuário e contém uma linha por template, com origem, presença em cenas, contagem de colocações, componentes, controladores e comportamento inferido.
