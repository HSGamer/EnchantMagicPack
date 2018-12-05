package me.hsgamer.enchantmagicpack.enchants.active;

import com.rit.sucy.player.Protection;
import com.sucy.enchant.api.Cooldowns;
import com.sucy.enchant.api.CustomEnchantment;
import me.hsgamer.enchantmagicpack.utils.FastParticle;
import me.hsgamer.enchantmagicpack.utils.ParticleType;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

/*
 * Shoot a line and damage the enemy
 */
public class Laser extends CustomEnchantment {

    private static final String LENGTH = "length";
    private static final String DAMAGE = "damage";
    private static final String SPACE = "space";

    public Laser() {
        super("Laser", "Shoot a line and damage the enemy");

        setMaxLevel(5);
        setWeight(2);
        addNaturalItems(Material.STICK);
        addNaturalItems(Material.BLAZE_ROD);

        settings.set(LENGTH, 6, 2);
        settings.set(DAMAGE, 5, 1);
        settings.set(SPACE, 0.5D);

        Cooldowns.configure(settings, 5, 0);
    }

    public void applyInteractBlock(Player user, int level, PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
        if (Cooldowns.onCooldown(this, user, settings, level)) return;
        Location loc = user.getLocation().add(0.0D, 1.2D, 0.0D);
        Vector dir = loc.getDirection();
        double range = settings.get(LENGTH, level);
        double space = settings.getDouble(SPACE);
        for (double i = 0.0D; i < range; i += space) {
            Block tempblock = loc.getBlock();
            if (!(tempblock.isEmpty() || tempblock.getType() == Material.GLASS || tempblock.getType().toString().endsWith("STAINED_GLASS") || tempblock.getType().toString().endsWith("STAINED_GLASS_PANE")))
                break;
            loc = loc.add(dir.multiply(1).normalize());
            FastParticle.spawnParticle(loc.getWorld(), ParticleType.REDSTONE, loc, 5, 0, 0, 0, 0, Color.RED);
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, .3, .3, .3)) {
                if ((entity instanceof LivingEntity) && (entity.getEntityId() != user.getEntityId()) && !(entity instanceof ArmorStand) && !(Protection.isAlly(user, (LivingEntity) entity))) {
                    ((Damageable) entity).damage(settings.get(DAMAGE, level), user);
                }
            }
        }
        Cooldowns.start(this, user);
    }
}
