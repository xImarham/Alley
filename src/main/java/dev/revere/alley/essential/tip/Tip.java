package dev.revere.alley.essential.tip;

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
            "&bTip: &fUse F5 to look at your opponent one last time before they end you.",
            "&bTip: &fW-tap like your life depends on it. It kinda does.",
            "&bTip: &fKeep your crosshair at head level... unless you like tickling toes.",
            "&bTip: &fPractice spacing — or just hug them and pray.",
            "&bTip: &fStrafing is cool, but have you tried not getting hit at all?",
            "&bTip: &fBlock-hitting reduces damage, but won’t save you from bad aim.",
            "&bTip: &fDon’t spam-click... unless you’re into self-sabotage.",
            "&bTip: &fPing and FPS matter, but not as much as your lack of skill.",
            "&bTip: &fS-tap to assert dominance. Or just panic like usual.",
            "&bTip: &fFollowing Emmy on github is a great way to get better at pvp. &7(github.com/hmEmmy)",
            "&bTip: &fDon’t panic. That’s my job — watching you panic.",
            "&4AUTHENTIC TIP: &dMinemen Club; &fThe only place where you can get better at pvp and still be bad.",
            "&bTip: &fStrafe like you're dodging responsibilities.",
            "&6Running away has always been a valid strategy.",
            "&c&lPRO Tip: &cYou’ll never beat Emmy. Accept it.",
            "&bTip: &fUse the environment. Jump into a cactus — tactical.",
            "&bTip: &fPractice makes perfect, but you might just be a lost cause.",
            "&bTip: &fWatch your opponent’s moves. Then copy them poorly.",
            "&bTip: &fCommunicate with your team. Yelling ‘HELP’ counts, right?",
            "&bTip: &fCan't win against somebody? They're too good? Just disconnect and touch grass.",
            "&5Exclusive Tip: &fRemi doesn’t know how to play. Just ask him. And ignore the fact that he is also a contributor to this plugin. He has never pushed a commit ever since he decided to 'quit the com'.",
            "&bTip: &fUse aim trainers — or just continue closing your eyes mid-fight.",
            "&fDid you know that Alley is almost 2 years old? &dIt may seem a little crazy, PRETTY BABY!",
            "&bTip: &fDon’t rush in. Let your teammates go first and take the L.",
            "&bTip: &fThrow rods and pray. A true PvP strategy.",
            "&cBased on your stats, I assume that you are bad. There's no way around it.",
            "&bTip: &fHotbar swap like you mean it. Not like your grandma’s texting.",
            "&bTip: &fUse your sword. Or your fists. Or just run away.",
            "&bYour teammate is getting 2v1'd? &fJust watch. It’s entertaining.",
            "&bTip: &fIs somebody constantly queueing you and you're losing each time? Just own the server and use &c/ban &fx)",
            "&bTip: &fEat your gapples. Or don’t. It’s your funeral.",
            "&eListen to your teammates. They’re probably screaming at you for a reason.",
            "&bTip: &fUse your bow. Or just stare at it in confusion.",
            "&bTip: &fStop jumping — you're not a bunny.",
            "&bTip: &fCombos are about timing. You’re about guessing.",
            "&cRemi is a racist (This is in-fact true info).",
            "&bTip: &fStaring Alley's repository will definitely improve your aim. &7(github.com/RevereInc/Alley)",
            "&bTip: &fPredict your opponent. Then ignore that and panic anyway.",
            "&bTip: &fIf your enemy is a &cb*tch&f, be a &cb*tch &ftoo.",
            "&bTip: &fLose 1v1s? Blame lag. Always works.",
            "&bTip: &fDid you know Alley has a built-in anti-cheat? It’s called ‘Emmy’.",
            "&bTip: &fSneak mid-fight. Confuse your enemy... and yourself.",
            "&bTip: &fIf you're a developer, don't meet Remi. He will call you a skid.",
            "&bTip: &fSwitch to a shovel. Psychological warfare.",
            "&bTip: &fIf you're accused of being a too bad player, just ask for proof (m***r vibes).",
            "&bTip: &fDon’t play ranked. For everyone’s sake.",
            "&bTip: &fIf you’re winning, it’s all you. You’re a god.",
            "&bTip: &fEvery loss is a learning experience. You must be a genius by now.",
            "&bTip: &fIf you’re losing, it’s the server’s fault. Blame Alley.",
            "&bTip: &fIf someone says 'ez' after winning, just call them a skid.",
            "&bTip: &fLower your sensitivity. Or keep spinning in circles, that’s cool too.",
            "&bTip: &fIf you’re losing, just blame your teammates. It’s their fault.",
            "&fYou can always try to get better, but you probably won't."
    );
}