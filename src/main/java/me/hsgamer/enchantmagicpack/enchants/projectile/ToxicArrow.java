package me.hsgamer.enchantmagicpack.enchants.projectile;

import me.hsgamer.enchantmagicpack.Utils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import studio.magemonkey.fabled.enchants.api.Tasks;

import java.util.HashMap;
import java.util.Map;

public class ToxicArrow extends ProjectileShoot {
    private static final String AMPLIFIER = "amplifier";
    private static final String DURATION = "duration";
    private static final String RANGE = "range";
    private static final String DELAY = "delay";
    private static final String PARTICLE_AMOUNT = "particle-amount";
    private static final Map<Arrow, BukkitTask> arrows = new HashMap<>();

    public ToxicArrow() {
        super("Toxic Arrow", "Make nearby entity be poisoned");

        setMaxLevel(3, 3);

        settings.set(AMPLIFIER, 1, 1);
        settings.set(DURATION, 20, 20);
        settings.set(RANGE, 3, 2);
        settings.set(DELAY, 10, 2);
        settings.set(PARTICLE_AMOUNT, 50);
    }

    @Override
    public void applyProjectileShoot(LivingEntity user, ItemStack bow, int level, EntityShootBowEvent event) {
        Entity projectile = event.getProjectile();
        if (!(projectile instanceof Arrow arrow)) return;
        int duration = (int) settings.get(DURATION, level);
        int amplifier = (int) settings.get(AMPLIFIER, level);
        double range = settings.get(RANGE, level);
        int delay = (int) settings.get(DELAY, level);
        PotionEffect effect = new PotionEffect(PotionEffectType.POISON, duration, amplifier);
        arrows.put(arrow, Tasks.schedule(new ToxicArrowRunnable(arrow, user, range, range, range, settings.getInt(PARTICLE_AMOUNT), effect), delay, delay));
    }

    static class ToxicArrowRunnable extends BukkitRunnable {
        Arrow arrow;
        double offX;
        double offY;
        double offZ;
        PotionEffect effect;
        LivingEntity user;
        int particleAmount;

        ToxicArrowRunnable(Arrow arrow, LivingEntity user, double offX, double offY, double offZ, int particleAmount, PotionEffect effect) {
            this.arrow = arrow;
            this.offX = offX;
            this.offY = offY;
            this.offZ = offZ;
            this.effect = effect;
            this.user = user;
            this.particleAmount = particleAmount;
        }

        @Override
        public void run() {
            if (arrow.isDead()) {
                arrows.remove(arrow).cancel();
            }
            Location start = arrow.getLocation();
            for (Entity e : arrow.getNearbyEntities(offX, offY, offZ)) {
                if (!(e instanceof LivingEntity livingEntity)) continue;
                if (Utils.isAlly(user, livingEntity)) continue;
                Location end = e.getLocation();
                Location temp = start.clone();
                double distance = temp.distance(end);
                Vector dir = end.subtract(temp).toVector().normalize().multiply(0.1);
                double part = dir.length();
                for (double i = 0; i < distance; i += part) {
                    temp = temp.add(dir);
                    temp.getWorld().spawnParticle(Particle.REDSTONE, temp, 2, 0, 0, 0, 0, new Particle.DustOptions(Color.GREEN, 1));
                }
                livingEntity.addPotionEffect(effect);
            }
        }
    }
}
