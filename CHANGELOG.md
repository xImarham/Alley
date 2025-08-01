## 🎉 Alley v2.2.0

### 📈 **MINOR RELEASE** - New Features Added

**Previous Version:** `2.1.9`
**New Version:** `2.2.0`

### 📝 Changes:

- 🚀 Auto-update version to 2.1.9 (🔧 **PATCH** RELEASE) (ea033846)
- chore: Added missing string in settings.yml (e769ae35)
- 🚀 Auto-update version to 2.1.8 (🔧 **PATCH** RELEASE) (1fa75700)
- chore(duel-request-service-impl): validate that original arena name isnt null before overwriting the final arena (bd73f78c)
- 🚀 Auto-update version to 2.1.7 (🔧 **PATCH** RELEASE) (853ce3ef)
- chore(duel-request-service-impl): get original arena name instead of the copy (7b923598)
- 🚀 Auto-update version to 2.1.6 (🔧 **PATCH** RELEASE) (cab2ce3a)
- chore: check if said hotbar item already exists in "createHotbarItem" method itself (f6eb574c)
- 🚀 Auto-update version to 2.1.5 (🔧 **PATCH** RELEASE) (1369d7f8)
- chore: fix auto-release.yml skip keyword (cc247c9a)
- chore: doing the requested changes (df51e457)
- feat: add bstats for data (696ebdb6)
- fix: ensure schematics folder exists, as standalone arenas won't be saved if it doesn't (2a26a028)
- feat: implement the hotbar commands (needs testing and clean up) (d2a8e204)
- chore(hotbar-service-impl): remove "isHotbarItem" method (2f38abe2)
- chore(hotbar-service-impl): inject layout service, queue service (89ecc957)
- feat: add the remaining menu instances to the "HotbarServiceImpl#getMenuInstanceFromName" method and add an informative comment in hotbar.yml and removed duplicated kit_editor hotbar item (fcbc59fe)
- chore: added player parameter to HotbarServiceImpl::getMenuInstanceFromName so we can access profile (e1cb7f44)
- 🚀 Auto-update version to 2.1.4 (🔧 **PATCH** RELEASE) (2f6e3208)
- chore: add prevent release keyword (2cab2e52)
- 🚀 Auto-update version to 2.1.3 (🔧 **PATCH** RELEASE) (f461ef81)
- chore: update permission lack message to test workflow (5cc24254)
- chore: fix service command package (472c64de)
- chore: rename some repository subclasses to services (9991617e)
- chore: refactor remaining missed classes and updated misspellings (80508fb2)
- chore: update doc-strings (58b276d3)
- chore: refactor classes to support new naming conventions (2ce96830)
- 🚀 Auto-update version to 2.1.2 (🔧 **PATCH** RELEASE) (09ca7ba2)
- chore: prevent duplicate build-test runs (dfce0814)
- 🚀 Auto-update version to 2.1.1 (🔧 **PATCH** RELEASE) (7dd8b8b1)
- feat: add build test workflow (060bb391)
- chore: prevent workflow from running in other branches (11dbdfbc)
- 🚀 Auto-update version to 2.1.0 (📈 **MINOR** RELEASE) (ea39e543)
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
**Download:** [Alley-2.2.0.jar](https://github.com/xImarham/Alley/releases/download/v2.2.0/Alley-2.2.0.jar)

**Installation:** Place the JAR file in your `plugins/` folder and restart your server.

---
### 🏷️ Version Bump Keywords

- Add `MAJOR` to commit messages for breaking changes (X.0.0)

- Add `MINOR` to commit messages for new features (X.Y.0)

- Add `PATCH` to commit messages for bug fixes (X.Y.Z)

- No keyword = automatic patch bump

- Add `SKIP-RELEASE` or `[skip release]` to skip creating a release

