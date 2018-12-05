package me.hsgamer.enchantmagicpack.enchants.projectile.shoot;

import com.rit.sucy.player.Protection;
import com.sucy.enchant.api.Tasks;
import me.hsgamer.enchantmagicpack.utils.FastParticle;
import me.hsgamer.enchantmagicpack.utils.ParticleType;
import org.bukkit.Color;
import org.bukkit.Location;
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

import java.util.Hashtable;

public class ToxicArrow extends ProjectileShoot {
    private static String AMPLIFIER = "amplifier";
    private static String DURATION = "duration";
    private static String RANGE = "range";
    private static String DELAY = "delay";
    private static String PARTICLE_AMOUNT = "particle-amount";
    private static Hashtable<Arrow, BukkitTask> arrows = new Hashtable<>();

    public ToxicArrow() {
        super("ToxicArrow", "Make nearby entity be poisoned");

        setMaxLevel(3, 3);

        settings.set(AMPLIFIER, 1, 1);
        settings.set(DURATION, 20, 20);
        settings.set(RANGE, 3, 2);
        settings.set(DELAY, 10, 2);
        settings.set(PARTICLE_AMOUNT, 50);
    }

    public void applyProjectileShoot(LivingEntity user, ItemStack bow, int level, EntityShootBowEvent event) {
        Entity projectile = event.getProjectile();
        if (!(projectile instanceof Arrow)) return;
        int duration = (int) settings.get(DURATION, level);
        int amplifier = (int) settings.get(AMPLIFIER, level);
        double range = settings.get(RANGE, level);
        int delay = (int) settings.get(DELAY, level);
        PotionEffect effect = new PotionEffect(PotionEffectType.POISON, duration, amplifier);
        arrows.put((Arrow) projectile, Tasks.schedule(new ToxicArrowRunnable((Arrow) projectile, user, range, range, range, settings.getInt(PARTICLE_AMOUNT), effect), delay, delay));
    }

    class ToxicArrowRunnable extends BukkitRunnable {
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
                if (!(e instanceof LivingEntity)) continue;
                if (Protection.isAlly(user, (LivingEntity) e)) continue;
                Location end = e.getLocation();
                Location temp = start.clone();
                double distance = temp.distance(end);
                Vector dir = end.subtract(temp).toVector().normalize().multiply(0.1);
                double part = dir.length();
                for (double i = 0; i < distance; i += part) {
                    temp = temp.add(dir);
                    FastParticle.spawnParticle(temp.getWorld(), ParticleType.REDSTONE, temp, 2, 0, 0, 0, 0.01, Color.GREEN);
                }
                ((LivingEntity) e).addPotionEffect(effect, true);
            }
        }
    }
}
