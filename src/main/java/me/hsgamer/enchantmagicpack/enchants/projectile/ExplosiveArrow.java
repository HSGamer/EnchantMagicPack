package me.hsgamer.enchantmagicpack.enchants.projectile;

import me.hsgamer.enchantmagicpack.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import studio.magemonkey.fabled.enchants.api.Cooldowns;

/*
 * Explode the area when hitting the enemy
 */
public class ExplosiveArrow extends ProjectileHit {

    private static final String DAMAGE = "damage";
    private static final String BLOCK_FIRE = "block-fire";
    private static final String BLOCK_BREAK = "block-break";

    public ExplosiveArrow() {
        super("Explosive Arrow", "Explode the area when hitting the enemy");

        setMaxLevel(5);
        setWeight(2);

        Cooldowns.configure(settings, 5, 0);
        settings.set(DAMAGE, 2, 1);
        settings.set(BLOCK_BREAK, true);
        settings.set(BLOCK_FIRE, true);
    }

    @Override
    public void applyProjectileHit(LivingEntity user, int level, ProjectileHitEvent event) {
        if (Cooldowns.onCooldown(this, user, settings, level)) return;
        if (event.getHitEntity() == null) return;
        Location target = event.getHitEntity().getLocation();
        Utils.spawnTnt(target, user, settings.getInt(DAMAGE, level), 0, settings.getBoolean(BLOCK_FIRE), settings.getBoolean(BLOCK_BREAK), true);
        for (Entity e : target.getWorld().getNearbyEntities(target, settings.getInt(DAMAGE, level), settings.getInt(DAMAGE, level), settings.getInt(DAMAGE, level))) {
            if (e instanceof LivingEntity livingEntity && !Utils.isAlly(user, livingEntity)) {
                e.setFireTicks(60);
            }
        }
        Cooldowns.start(this, user);
    }
}
