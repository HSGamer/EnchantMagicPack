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
        super("Frozen Arrow", "Freeze the enemy when hit");

        setMaxLevel(5);
        setWeight(2);

        Cooldowns.configure(settings, 5, 0);
        settings.set(DURATION, 3, 1);
    }

    @Override
    public void applyProjectileHit(LivingEntity user, int level, ProjectileHitEvent event) {
        if (Cooldowns.onCooldown(this, user, settings, level)) return;
        if (!(event.getHitEntity() instanceof LivingEntity livingEntity)) return;
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, settings.getInt(DURATION, level) * 20, 10, true, true));
        Cooldowns.start(this, user);
    }
}
