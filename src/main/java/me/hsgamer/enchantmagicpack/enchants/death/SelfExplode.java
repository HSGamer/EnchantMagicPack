package me.hsgamer.enchantmagicpack.enchants.death;

import com.sucy.enchant.api.Cooldowns;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SelfExplode extends DeathEnchantment {
    private static final String DAMAGE = "damage";
    private static final String BLOCK_FIRE = "block-fire";
    private static final String BLOCK_BREAK = "block-break";

    public SelfExplode() {
        super("Self-Explode", "Explode when death");

        setMaxLevel(5);
        setWeight(2);

        Cooldowns.configure(settings, 5, 0);
        settings.set(DAMAGE, 2, 1);
        settings.set(BLOCK_BREAK, true);
        settings.set(BLOCK_FIRE, true);

        addNaturalItems(Material.PAPER);
    }

    @Override
    public void applyOnDeath(Player user, int level, PlayerDeathEvent event) {
        Location loc = user.getLocation();
        loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), settings.getFloat(DAMAGE, level), settings.getBoolean(BLOCK_FIRE), settings.getBoolean(BLOCK_BREAK));
    }
}
