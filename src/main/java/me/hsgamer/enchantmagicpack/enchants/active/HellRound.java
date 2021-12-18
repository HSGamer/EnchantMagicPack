package me.hsgamer.enchantmagicpack.enchants.active;

import com.sucy.enchant.api.Cooldowns;
import com.sucy.enchant.api.CustomEnchantment;
import mc.promcteam.engine.mccore.util.Protection;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/*
 * Deal damage to entities nearby
 */
public class HellRound extends CustomEnchantment {

    private static final String RANGE = "range";
    private static final String DAMAGE = "damage";

    public HellRound() {
        super("Hell Round", "Deal damage to entities nearby");

        setMaxLevel(5);
        setWeight(2);
        addNaturalItems(Material.STICK);
        addNaturalItems(Material.BLAZE_ROD);

        settings.set(RANGE, 3, 1);
        settings.set(DAMAGE, 5, 2);
        Cooldowns.configure(settings, 5, 0);
    }

    @Override
    public void applyInteractBlock(Player user, int level, PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
        if (Cooldowns.onCooldown(this, user, settings, level)) return;
        double range = settings.get(RANGE, level);
        user.getWorld().spawnParticle(Particle.FLAME, user.getLocation(), (int) (100 * range), range, 2.0, range, 0.05);
        user.getWorld().spawnParticle(Particle.SMOKE_LARGE, user.getLocation(), (int) (50 * range), range, 2.0, range, 0.1);
        for (Entity e : user.getNearbyEntities(range, 2, range)) {
            if (!(e instanceof LivingEntity livingEntity) || Protection.isAlly(user, livingEntity)) continue;
            livingEntity.damage(settings.get(DAMAGE, level), user);
        }
        Cooldowns.start(this, user);
    }
}
