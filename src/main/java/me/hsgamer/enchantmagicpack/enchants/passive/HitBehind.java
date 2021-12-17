package me.hsgamer.enchantmagicpack.enchants.passive;

import com.sucy.enchant.api.CustomEnchantment;
import com.sucy.enchant.api.ItemSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Teleport behind the enemy when fighting
 */
public class HitBehind extends CustomEnchantment {

    private static final String CHANCE = "chance";

    public HitBehind() {

        super("HitBehind", "Chance to teleport behind the enemy when fighting (Required Sneaking)");

        setMaxLevel(5);
        setWeight(1);
        addNaturalItems(ItemSet.SWORDS.getItems());

        settings.set(CHANCE, 5, 5);

    }

    public void applyOnHit(LivingEntity user, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!((Player) user).isSneaking()) return;
        if (Math.random() * 100 <= settings.get(CHANCE, level)) {
            Location newTeleport = target.getLocation().add(target.getLocation().getDirection().multiply(-1.2));
            newTeleport.setY(target.getLocation().getY() + 0.1);
            if ((target.getType() == EntityType.SPIDER) || (target.getType() == EntityType.CAVE_SPIDER) || (target.getType() == EntityType.CHICKEN)) {
                newTeleport.setPitch((float) 20.0);
            }
            if (target instanceof Zombie zombie && !zombie.isAdult()) {
                newTeleport.setPitch((float) 20.0);
            }
            if (newTeleport.getBlock().getType() == Material.AIR) {
                user.getWorld().spawnParticle(Particle.FLAME, user.getLocation(), 100, 0.1, 0.1, 0.1, 0.05);
                user.teleport(newTeleport);
                user.getWorld().spawnParticle(Particle.PORTAL, user.getLocation(), 1000, 0.1, 0.1, 0.1, 0.05);
            }
        }
    }
}
