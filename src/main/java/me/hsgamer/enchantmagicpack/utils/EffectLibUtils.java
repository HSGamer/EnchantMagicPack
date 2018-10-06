package me.hsgamer.enchantmagicpack.utils;

import de.slikey.effectlib.EffectManager;
import org.bukkit.plugin.Plugin;

public class EffectLibUtils {
    private static EffectManager em;

    public static void inputEffectLib(Plugin plugin) {
        em = new EffectManager(plugin);
    }

    public static void clear() {
        em.dispose();
    }

    public static EffectManager getEffectLib() {
        return em;
    }
}
