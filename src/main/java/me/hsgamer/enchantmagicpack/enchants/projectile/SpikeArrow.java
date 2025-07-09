package me.hsgamer.enchantmagicpack.enchants.projectile;

import me.hsgamer.enchantmagicpack.Utils;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import studio.magemonkey.fabled.enchants.api.Cooldowns;

/*
 * Split the damage to nearby entities
 */
public class SpikeArrow extends ProjectileHit {

    private static final String RANGE = "range";
    private static final String CHANCE = "chance";
    private static final String MAXDAMAGE = "maxdamage";

    public SpikeArrow() {
        super("Spike Arrow", "Split the damage to nearby entities");

        setMaxLevel(3);
        setWeight(2);

        settings.set(MAXDAMAGE, 2, 1);
        settings.set(RANGE, 4, 1);
        settings.set(CHANCE, 0.2, 0.1);

        Cooldowns.configure(settings, 5, 0);
    }

    @Override
    public void applyProjectileHit(LivingEntity user, int level, ProjectileHitEvent event) {
        if (event.getHitEntity() == null) return;
        if (Cooldowns.onCooldown(this, user, settings, level)) return;
        double range = settings.get(RANGE, level);
        event.getHitEntity().getWorld().spawnParticle(Particle.CRIT, event.getHitEntity().getLocation(), 500, range, range, range, 0.5);
        for (Entity i : event.getHitEntity().getNearbyEntities(range, range, range)) {
            if (!(i instanceof LivingEntity livingEntity) || Utils.isAlly(user, livingEntity)) continue;
            if (Math.random() <= settings.get(CHANCE, level)) {
                livingEntity.damage((Math.random()) * settings.get(MAXDAMAGE, level), user);
            }
        }
        Cooldowns.start(this, user);
    }
}
