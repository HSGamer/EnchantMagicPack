package me.hsgamer.enchantmagicpack.enchants.projectile;

import com.sucy.enchant.api.Cooldowns;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*
 * Freeze the enemy when hit
 */
public class FrozenArrow extends ProjectileHit {

    private static final String DURATION = "duration";

    public FrozenArrow() {
        super("FrozenArrow", "Freeze the enemy when hit");

        setMaxLevel(5);
        setWeight(2);

        Cooldowns.configure(settings, 5, 0);
        settings.set(DURATION, 3, 1);
    }

    public void applyProjectileHit(LivingEntity user, int level, ProjectileHitEvent event) {
        // Check if the user is on Cooldown
        if (Cooldowns.onCooldown(this, user, settings, level)) return;
        // Check if the location is Null
        if (event.getHitEntity() == null) return;
        // Check if the target is LivingEntity
        if (!(event.getHitEntity() instanceof LivingEntity)) return;
        // Freeze the target
        ((LivingEntity) event.getHitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, settings.getInt(DURATION, level) * 20, 10, true, true));
        // Start the Cooldown
        Cooldowns.start(this, user);
    }
}
