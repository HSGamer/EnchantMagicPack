package me.hsgamer.enchantmagicpack.enchants.active;

import com.sucy.enchant.api.Cooldowns;
import com.sucy.enchant.api.CustomEnchantment;
import mc.promcteam.engine.mccore.util.Protection;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/*
 * Strike lightning on the enemy
 */

public class StrikeDown extends CustomEnchantment {

    private static final String DAMAGE = "damage";

    public StrikeDown() {
        super("StrikeDown", "Strike lightning on the enemy");

        setMaxLevel(5);
        setWeight(2);
        addNaturalItems(Material.STICK);
        addNaturalItems(Material.BLAZE_ROD);

        settings.set(DAMAGE, 10, 5);
        Cooldowns.configure(settings, 5, 0);
    }

    public void applyInteractEntity(final Player player, final int level, final PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof LivingEntity livingEntity) || Protection.isAlly(player, livingEntity))
            return;
        if (Cooldowns.onCooldown(this, player, settings, level)) return;
        event.getRightClicked().getWorld().strikeLightning(event.getRightClicked().getLocation());
        ((Damageable) event.getRightClicked()).damage(settings.get(DAMAGE, level), player);
        Cooldowns.start(this, player);
    }

}
