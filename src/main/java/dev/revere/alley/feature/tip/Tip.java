package dev.revere.alley.feature.tip;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Emmy
 * @project Alley
 * @since 25/04/2025
 */
public class Tip {
    private final Map<UUID, Long> lastTip;

    public final long COOLDOWN_TIME;

    public Tip() {
        this.lastTip = new HashMap<>();
        this.COOLDOWN_TIME = 1000 * 5; // 5 seconds
    }

    /**
     * Adds a player to the lastTip map with the current time.
     *
     * @param uuid The UUID of the player to add.
     */
    public void addPlayer(UUID uuid) {
        this.lastTip.put(uuid, System.currentTimeMillis());
    }

    /**
     * Checks if a player can receive a tip based on the cooldown time.
     *
     * @param uuid The UUID of the player to check.
     * @return true if the player can receive a tip, false otherwise.
     */
    public boolean canGiveTip(UUID uuid) {
        Long lastTime = this.lastTip.get(uuid);
        if (lastTime == null) {
            return true;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= COOLDOWN_TIME) {
            this.lastTip.put(uuid, currentTime);
            return true;
        }

        return false;
    }

    /**
     * Returns the last tip time for a player.
     *
     * @param uuid The UUID of the player.
     * @return The last tip time in milliseconds since epoch.
     */
    public long getLastTipTime(UUID uuid) {
        return this.lastTip.getOrDefault(uuid, 0L);
    }

    /**
     * Returns a random tip from the list of tips.
     *
     * @return A random tip as a String.
     */
    public String getRandomTip() {
        return this.TIPS.get(ThreadLocalRandom.current().nextInt(this.TIPS.size()));
    }

    /**
     * This list consist of real-life tips that are 100% accurate and not made up.
     * And it might be infected by Copilot, a.k.a. the AI that is better than you.
     */
    private final List<String> TIPS = Arrays.asList(
            "&6Tip: &fUse F5 to look at your opponent one last time before they end you.",
            "&6Tip: &fW-tap like your life depends on it. It kinda does.",
            "&6Tip: &fKeep your crosshair at head level... unless you like tickling toes.",
            "&6Tip: &fPractice spacing — or just hug them and pray.",
            "&6Tip: &fStrafing is cool, but have you tried not getting hit at all?",
            "&6Tip: &fBlock-hitting reduces damage, but won’t save you from bad aim.",
            "&6Tip: &fDon’t spam-click... unless you’re into self-sabotage.",
            "&6Tip: &fPing and FPS matter, but not as much as your lack of skill.",
            "&6Tip: &fS-tap to assert dominance. Or just panic like usual.",
            "&6Tip: &fFollowing Emmy on github is a great way to get better at pvp. &7(github.com/hmEmmy)",
            "&6Tip: &fDon’t panic. That’s my job — watching you panic.",
            "&4AUTHENTIC TIP: &dMinemen Club; &fThe only place where you can get better at pvp and still be bad.",
            "&6Tip: &fStrafe like you're dodging responsibilities.",
            "&6Running away has always been a valid strategy.",
            "&c&lPRO Tip: &cYou’ll never beat Emmy. Accept it.",
            "&6Tip: &fUse the environment. Jump into a cactus — tactical.",
            "&6Tip: &fPractice makes perfect, but you might just be a lost cause.",
            "&6Tip: &fWatch your opponent’s moves. Then copy them poorly.",
            "&6Tip: &fCommunicate with your team. Yelling ‘HELP’ counts, right?",
            "&6Tip: &fCan't win against somebody? They're too good? Just disconnect and touch grass.",
            "&5Exclusive Tip: &fRemi doesn’t know how to play. Just ask him. And ignore the fact that he is also a contributor to this plugin. He has never pushed a commit ever since he decided to 'quit the com'.",
            "&6Tip: &fUse aim trainers — or just continue closing your eyes mid-fight.",
            "&fDid you know that Alley is almost 2 years old? &dIt may seem a little crazy, PRETTY BABY!",
            "&6Tip: &fDon’t rush in. Let your teammates go first and take the L.",
            "&6Tip: &fThrow rods and pray. A true PvP strategy.",
            "&cBased on your stats, I assume that you are bad. There's no way around it.",
            "&6Tip: &fHotbar swap like you mean it. Not like your grandma’s texting.",
            "&6Tip: &fUse your sword. Or your fists. Or just run away.",
            "&6Your teammate is getting 2v1'd? &fJust watch. It’s entertaining.",
            "&6Tip: &fIs somebody constantly queueing you and you're losing each time? Just own the server and use &c/ban &fx)",
            "&6Tip: &fEat your gapples. Or don’t. It’s your funeral.",
            "&eListen to your teammates. They’re probably screaming at you for a reason.",
            "&6Tip: &fUse your bow. Or just stare at it in confusion.",
            "&6Tip: &fStop jumping — you're not a bunny.",
            "&6Tip: &fCombos are about timing. You’re about guessing.",
            "&cRemi is a racist (This is in-fact true info).",
            "&6Tip: &fStaring Alley's repository will definitely improve your aim. &7(github.com/RevereInc/Alley)",
            "&6Tip: &fPredict your opponent. Then ignore that and panic anyway.",
            "&6Tip: &fIf your enemy is a &cb*tch&f, be a &cb*tch &ftoo.",
            "&6Tip: &fLose 1v1s? Blame lag. Always works.",
            "&6Tip: &fDid you know Alley has a built-in anti-cheat? It’s called ‘Emmy’.",
            "&6Tip: &fSneak mid-fight. Confuse your enemy... and yourself.",
            "&6Tip: &fIf you're a developer, don't meet Remi. He will call you a skid.",
            "&6Tip: &fSwitch to a shovel. Psychological warfare.",
            "&6Tip: &fIf you're accused of being a too bad player, just ask for proof (m***r vibes).",
            "&6Tip: &fDon’t play ranked. For everyone’s sake.",
            "&6Tip: &fIf you’re winning, it’s all you. You’re a god.",
            "&6Tip: &fEvery loss is a learning experience. You must be a genius by now.",
            "&6Tip: &fIf you’re losing, it’s the server’s fault. Blame Alley.",
            "&6Tip: &fIf someone says 'ez' after winning, just call them a skid.",
            "&6Tip: &fLower your sensitivity. Or keep spinning in circles, that’s cool too.",
            "&6Tip: &fIf you’re losing, just blame your teammates. It’s their fault.",
            "&fYou can always try to get better, but you probably won't."
    );
}