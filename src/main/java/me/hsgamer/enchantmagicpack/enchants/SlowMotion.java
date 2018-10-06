package me.hsgamer.enchantmagicpack.enchants;

import com.sucy.enchant.api.Cooldowns;
import com.sucy.enchant.api.CustomEnchantment;
import com.sucy.enchant.api.Tasks;
import me.hsgamer.enchantmagicpack.EnchantMagicPack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class SlowMotion extends CustomEnchantment {
    private static final String LENGTH = "length";
    private static final String TIME = "time";
    private static final String FREQUENCY = "frequency";
    private static final String PERCENTAGE = "percentage";
    private static Hashtable<Entity, Vector> vector = new Hashtable<>();
    private static Hashtable<UUID, BukkitTask> tasks = new Hashtable<>();

    public SlowMotion() {
        super("Slow Motion", "Make the nearby projectiles move slowly");

        setMaxLevel(5);
        setWeight(0.75);
        Cooldowns.configure(settings, 7, 2);
        settings.set(LENGTH, 2, 1.5);
        settings.set(TIME, 5, 2);
        settings.set(FREQUENCY, 10, -1);
        settings.set(PERCENTAGE, 0.5, 0.1);

        addNaturalItems(Material.STICK, Material.BLAZE_ROD);
    }

    public void applyInteractBlock(Player user, int level, PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (Cooldowns.onCooldown(this, user, settings, level)) return;
            Cooldowns.start(this, user);
            double length = settings.get(LENGTH, level);
            int time = (int) (settings.get(TIME, level) * 20);
            Location loc = user.getLocation();
            int frequency = (int) settings.get(FREQUENCY, level);
            double percent = settings.get(PERCENTAGE, level);
            List<Entity> entities = new ArrayList<>();
            tasks.put(user.getUniqueId(), Tasks.schedule(new BukkitRunnable() {

                @Override
                public void run() {
                    loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, (int) (length * 150), length, length, length, 0.01);
                    loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_PLING, 10, 2);
                    for (Entity entity : loc.getNearbyEntities(length, length, length)) {
                        if (entity instanceof Projectile
                                || entity.getType().equals(EntityType.SMALL_FIREBALL) || entity.getType().equals(EntityType.FIREBALL) || entity.getType().equals(EntityType.DRAGON_FIREBALL)) {
                            if (vector.containsKey(entity)) continue;
                            entities.add(entity);
                            Vector vec = entity.getVelocity().multiply(entity.getVelocity().length());
                            vector.put(entity, vec);
                            entity.setGravity(false);
                            entity.setVelocity(new Vector(vec.getX() * percent, vec.getY() * percent, vec.getZ() * percent));
                        }
                    }
                }
            }, frequency, frequency));
            new BukkitRunnable() {

                @Override
                public void run() {
                    tasks.remove(user.getUniqueId()).cancel();
                    for (Entity entity : entities) {
                        entity.setGravity(true);
                        entity.setVelocity(vector.get(entity));
                        vector.remove(entity);
                    }
                }
            }.runTaskLater(EnchantMagicPack.getInstace(), time);
        }
    }
}
