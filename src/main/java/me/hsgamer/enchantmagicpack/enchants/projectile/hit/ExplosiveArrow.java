package me.hsgamer.enchantmagicpack.enchants.projectile.hit;

import com.rit.sucy.player.Protection;
import com.sucy.enchant.api.Cooldowns;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;

/*
 * Explode the area when hitting the enemy
 */
public class ExplosiveArrow extends ProjectileHit {

    private static final String DAMAGE = "damage";
    private static final String BLOCK_FIRE = "block-fire";
    private static final String BLOCK_BREAK = "block-break";

    public ExplosiveArrow() {
        super("ExplosiveArrow", "Explode the area when hitting the enemy");

        setMaxLevel(5);
        setWeight(2);

        Cooldowns.configure(settings, 5, 0);
        settings.set(DAMAGE, 2, 1);
        settings.set(BLOCK_BREAK, true);
        settings.set(BLOCK_FIRE, true);
    }

    public void applyProjectileHit(LivingEntity user, int level, ProjectileHitEvent event) {
        // Check if the user is on Cooldown
        if (Cooldowns.onCooldown(this, user, settings, level)) return;
        // Check if the location is Null
        if (event.getHitEntity() == null) return;
        // Get the location of the target
        Location target = event.getHitEntity().getLocation();
        // Create the explosion on the enemy's location
        event.getHitEntity().getLocation().createExplosion(settings.getFloat(DAMAGE, level), settings.getBoolean(BLOCK_FIRE), settings.getBoolean(BLOCK_BREAK));
        // Set the entity nearby on fire
        for (Entity e : target.getWorld().getNearbyEntities(target, settings.getInt(DAMAGE, level), settings.getInt(DAMAGE, level), settings.getInt(DAMAGE, level))) {
            if (e instanceof LivingEntity && !Protection.isAlly(user, (LivingEntity) e)) {
                e.setFireTicks(60);
            }
        }
        // Start the Cooldown
        Cooldowns.start(this, user);
    }
}
