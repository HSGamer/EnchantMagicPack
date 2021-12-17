package me.hsgamer.enchantmagicpack.enchants.active;

import com.sucy.enchant.api.Cooldowns;
import com.sucy.enchant.api.CustomEnchantment;
import com.sucy.enchant.api.Tasks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeFreezing extends CustomEnchantment {
    private static final String LENGTH = "length";
    private static final String TIME = "time";
    private static final String FREQUENCY = "frequency";
    private static final Map<Entity, Vector> vector = new HashMap<>();

    public TimeFreezing() {
        super("Time-Freezing", "Make the nearby projectiles stop moving");

        setMaxLevel(5);
        setWeight(1.25);
        Cooldowns.configure(settings, 7, 2);
        settings.set(LENGTH, 2, 1.5);
        settings.set(TIME, 5, 2);
        settings.set(FREQUENCY, 10, -1);

        addNaturalItems(Material.STICK, Material.BLAZE_ROD);
    }

    @Override
    public void applyInteractBlock(Player user, int level, PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (Cooldowns.onCooldown(this, user, settings, level)) return;
        Cooldowns.start(this, user);

        final double length = settings.get(LENGTH, level);
        final int time = (int) (settings.get(TIME, level) * 20);
        final int frequency = (int) settings.get(FREQUENCY, level);

        final Location loc = user.getLocation();
        final List<Entity> entities = new ArrayList<>();
        Tasks.schedule(new BukkitRunnable() {
            private int currentTime = time;

            @Override
            public void run() {
                if (currentTime > 0) {
                    currentTime -= frequency;
                } else {
                    cancel();
                    return;
                }
                loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, (int) (length * 150), length, length, length, 0.01);
                loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                for (Entity entity : loc.getWorld().getNearbyEntities(loc, length, length, length)) {
                    if (entity instanceof Projectile) {
                        vector.computeIfAbsent(entity, e -> {
                            entities.add(entity);
                            entity.setGravity(false);
                            entity.setVelocity(new Vector(0, 0, 0));
                            return entity.getVelocity().multiply(entity.getVelocity().length());
                        });
                    }
                }
            }

            @Override
            public synchronized void cancel() throws IllegalStateException {
                super.cancel();
                for (Entity entity : entities) {
                    entity.setGravity(true);
                    entity.setVelocity(vector.get(entity));
                    vector.remove(entity);
                }
            }
        }, frequency, frequency);
    }
}
