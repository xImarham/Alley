## 🎉 Alley v2.0.1

### 🔧 **PATCH RELEASE** - Bug Fixes & Improvements

**Previous Version:** `2.0.0`
**New Version:** `2.0.1`

### 📝 Changes:

- feat: implement auto-updating feature (165aa42c)
- Merge pull request #11 from RevereInc/chore/visibility-logic (efbe1a2c) ([#11](https://github.com/RevereInc/alley-practice/pull/11))
- chore(visibility-service): filter visible players by their ffa match instance (50a13d33)
- chore: apply scoreboard config from pvp zone (17fadde8)
- Merge pull request #10 from RevereInc/feat/lobby-music (633d5220) ([#10](https://github.com/RevereInc/alley-practice/pull/10))
- chore: handle mongo storage for music (f50d021d)
- chore: finished music implementation, added cooldowns to command framework, cleaned up time utility (b08f63d9)
- chore: did some changes to the music menu (19f4ab91)
- feat: initial base of the "lobby music" system (c0b51848)
- Merge pull request #9 from RevereInc/feat/golden-heads (9180bd83) ([#9](https://github.com/RevereInc/alley-practice/pull/9))
- chore: adjust head consume effects and add cooldown (417f2cb9)
- chore: doing the NECESSARY changes requested from mr insurant (31b7e318)
- chore: create LICENSE.md (6fb4f832)
- chore: create CODE_OF_CONDUCT.md (ce1e55ea)
- feat: implemented golden heads (3a007087)
- Merge pull request #3 from RevereInc/chore/stable-matchmaking-impl (525644bb) ([#3](https://github.com/RevereInc/alley-practice/pull/3))
- Merge pull request #8 from RevereInc/chore/party-revisions (ab521ea6) ([#8](https://github.com/RevereInc/alley-practice/pull/8))
- Merge branch 'chore/stable-matchmaking-impl' into chore/party-revisions (76d42de7)
- Merge pull request #7 from RevereInc/chore/cleanup-and-bug-fixing (0ea14d7c) ([#7](https://github.com/RevereInc/alley-practice/pull/7))
- chore(profile): removed "&& this.state != EnumProfileState.WAITING" from isBusy method (07b97064)
- chore(party): fixed issues with party upon leaving, queueing, disbanding, etc... (2d5d8e2e)
- chore: revert party privacy placeholder (acadcf99)
- fix(placeholder-service): actually register the expansion now (2fc3d616)
- chore: create new Service class for papi expansion (fefa4a9e)
- chore(alley): getting rid of instance call of the api (81bc42f5)
- chore(ffa): split listener into multiple classes (21b2ce66)
- feat: added ListenerService. idk if this is really necessary? (cc22a87c)
- chore(ffa): depending on the kits no-hunger setting, cancel food level change event. (726dd4c4)
- feat: added "isBusy" method to simplify state checks (ae649243)
- fix: prevent players from getting stuck in match ending state as they leave. Also notifying other participants that they indeed disconnected, rather than sending the regular death message. (ee76d195)
- feat(scoreboard): added "getFancyName" method to profile to make things easier (231429f2)
- feat(scoreboard): add party privacy placeholder (0dd89060)
- chore(expansion): translate level color codes (f0b2dbcd)
- chore(commands): replace "Alley.getInstance()" with "this.plugin" (ee08a3c7)
- Merge pull request #6 from RevereInc/feat/crafting-handler-review (b4537747) ([#6](https://github.com/RevereInc/alley-practice/pull/6))
- chore(server-service): use matchMaterial instead of valueOf (ec732def)
- chore: doing the necessary changes requested from the reviewer: using set instead of list, refactored methods, removed updateCraftingRecipes method, no longer removing recipes entirely, rather setting crafting result to null via event listener... using our own logger, getting rid of the "double space" bruh. Simplified the command and using locale for the message. (9df0f0ae)
- Reapply "Merge pull request #5 from RevereInc/feat/crafting-recipe-handler" (c09c934a) ([#5](https://github.com/RevereInc/alley-practice/pull/5))
- Revert "Merge pull request #5 from RevereInc/feat/crafting-recipe-handler" (d1a9bcce) ([#5](https://github.com/RevereInc/alley-practice/pull/5))
- Merge pull request #5 from RevereInc/feat/crafting-recipe-handler (20d2e275) ([#5](https://github.com/RevereInc/alley-practice/pull/5))
- chore: refactor blockedCraftingMaterials to blockedCraftingItems (0b81efb9)
- feat: allow enabling/disabling different crafting recipes. (eefa9cb5)
- chore: moved DefaultReflection to "instance" package (331ee16f)
- feat(reflection-services): added VirtualStackCommand and implemented VirtualStackReflectionService that allows bypassing the default 64 item stack size limit (b1d4abd7)
- feat: added KitHelperCommand (e1f4eb0d)
- feat: made rest of the arena commands configurable (05b5435d)
- chore(abstract-match): moved one line :| (ed43677a)
- feat: started making arena commands configurable (ff1ae0b2)
- feat(settings): ability to toggle receiving duel requests (party duels through menu is not affected) (313e3f81)
- fix(match-history): properly pass arena name (12ea09e3)

---
**Download:** [Alley-2.0.1.jar](https://github.com/RevereInc/alley-practice/releases/download/v2.0.1/Alley-2.0.1.jar)

**Installation:** Place the JAR file in your `plugins/` folder and restart your server.

---
### 🏷️ Version Bump Keywords

- Add `MAJOR` to commit messages for breaking changes (X.0.0)

- Add `MINOR` to commit messages for new features (X.Y.0)

- Add `PATCH` to commit messages for bug fixes (X.Y.Z)

- No keyword = automatic patch bump

