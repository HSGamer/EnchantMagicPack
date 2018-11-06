package me.hsgamer.enchantmagicpack.enchants.projectile.hit;

import com.rit.sucy.player.Protection;
import com.sucy.enchant.api.Cooldowns;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;

/*
 * Split the damage to nearby entities
 */
public class SpikeArrow extends ProjectileHit {

    private static final String RANGE = "range";
    private static final String CHANCE = "chance";
    private static final String MAXDAMAGE = "maxdamage";

    public SpikeArrow() {
        super("SpikeArrow", "Split the damage to nearby entities");

        setMaxLevel(3);
        setWeight(2);

        settings.set(MAXDAMAGE, 2, 1);
        settings.set(RANGE, 4, 1);
        settings.set(CHANCE, 0.2, 0.1);

        Cooldowns.configure(settings, 5, 0);
    }

    public void applyProjectileHit(LivingEntity user, int level, ProjectileHitEvent event) {
        if (event.getHitEntity() == null) return;
        if (Cooldowns.onCooldown(this, user, settings, level)) return;
        double range = settings.get(RANGE, level);
        event.getHitEntity().getWorld().spawnParticle(Particle.CRIT, event.getHitEntity().getLocation(), 500, range, range, range, 0.5);
        for (Entity i : event.getHitEntity().getNearbyEntities(range, range, range)) {
            if (!(i instanceof LivingEntity) || Protection.isAlly(user, (LivingEntity) i)) continue;
            if (Math.random() <= settings.get(CHANCE, level)) {
                ((Damageable) i).damage((Math.random()) * settings.get(MAXDAMAGE, level), user);
            }
        }
        Cooldowns.start(this, user);
    }
}
