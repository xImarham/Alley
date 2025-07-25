## 🎉 Alley v2.1.0

### 📈 **MINOR RELEASE** - New Features Added

**Previous Version:** `2.0.7`
**New Version:** `2.1.0`

### 📝 Changes:

- chore: added party and state check for party event menu command (706c2e05)
- chore: update send title parameters for all calls (e37a9274)
- chore: remove redundant profile state check in the remove and add spectator methods within a ffa match (bb4f0244)
- chore: add missing doc-string for IArenaService::deleteTemporaryArena (58f5207e)
- chore: handle flight in lobby for donators properly (69c417b7)
- chore(match-block-listener): Limit block return to one stack to prevent duplication on respawn (288555ca)
- chore(match-block-listener): use ReflectionRepository for BlockAnimationReflectionService. Added a match "running" state check to the block animation runnable's 'playerIsStillPlaying' condition (976f5d5a)
- chore(ffa-damage-listener): prevent players from putting themselves into combat, no longer perform "visualizeTargetHealth" if victim and attacker is the same player (a46a13d6)
- feat(ffa-damage-listener): visualize health in action bar if the setting is enabled (63511349)
- feat(ffa-damage-listener): added bow shot indicator (45a96b21)
- chore(ffa-damage-listener): rod and bow shots now trigger combat (5d12acdd)
- chore: refactor SpectateFFACommand -> FFASpectateCommand (129fcd77)
- chore(ffa-spawn-command): use already existing method to teleport to safe zone (c5f7bcc8)
- chore: added missing ffa commands, added kit setting check to prevent certain issues until we handle building in ffa games (e0df81cd)
- chore: de-geminize his attempt at "fixing" my code (1dfec8c3)
- chore: potentially fix add spectator issue on final death (c3a94141)
- chore: refactored some task classes, added a new hide and seek gamemode, fixed server crashing on hitting a player with a snowball in spleef, added camp protection and platform decay kit settings, removed and refactored unused methods (6d3be9f7)

---
**Download:** [Alley-2.1.0.jar](https://github.com/RevereInc/alley-practice/releases/download/v2.1.0/Alley-2.1.0.jar)

**Installation:** Place the JAR file in your `plugins/` folder and restart your server.

---
### 🏷️ Version Bump Keywords

- Add `MAJOR` to commit messages for breaking changes (X.0.0)

- Add `MINOR` to commit messages for new features (X.Y.0)

- Add `PATCH` to commit messages for bug fixes (X.Y.Z)

- No keyword = automatic patch bump

