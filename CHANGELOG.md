## 🎉 Alley v2.1.6

### 🔧 **PATCH RELEASE** - Bug Fixes & Improvements

**Previous Version:** `2.1.5`
**New Version:** `2.1.6`

### 📝 Changes:

- chore: check if said hotbar item already exists in "createHotbarItem" method itself (f6eb574c)
- feat: implement the hotbar commands (needs testing and clean up) (d2a8e204)
- chore(hotbar-service-impl): remove "isHotbarItem" method (2f38abe2)
- chore(hotbar-service-impl): inject layout service, queue service (89ecc957)
- feat: add the remaining menu instances to the "HotbarServiceImpl#getMenuInstanceFromName" method and add an informative comment in hotbar.yml and removed duplicated kit_editor hotbar item (fcbc59fe)
- chore: added player parameter to HotbarServiceImpl::getMenuInstanceFromName so we can access profile (e1cb7f44)

---
**Download:** [Alley-2.1.6.jar](https://github.com/RevereInc/alley-practice/releases/download/v2.1.6/Alley-2.1.6.jar)

**Installation:** Place the JAR file in your `plugins/` folder and restart your server.

---
### 🏷️ Version Bump Keywords

- Add `MAJOR` to commit messages for breaking changes (X.0.0)

- Add `MINOR` to commit messages for new features (X.Y.0)

- Add `PATCH` to commit messages for bug fixes (X.Y.Z)

- No keyword = automatic patch bump

- Add `SKIP-RELEASE` or `[skip release]` to skip creating a release

