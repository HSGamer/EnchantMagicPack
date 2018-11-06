package me.hsgamer.enchantmagicpack.enchants.active;

import com.sucy.enchant.api.Cooldowns;
import com.sucy.enchant.api.CustomEnchantment;
import com.sucy.enchant.api.Tasks;
import de.slikey.effectlib.effect.FlameEffect;
import de.slikey.effectlib.effect.SmokeEffect;
import me.hsgamer.enchantmagicpack.EnchantMagicPack;
import me.hsgamer.enchantmagicpack.utils.EffectLibUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Hashtable;
import java.util.UUID;

public class Pyro extends CustomEnchantment {
    private static Hashtable<UUID, BukkitTask> tasks = new Hashtable<>();
    private static String DISTANCE = "distance";
    private static String TIME = "time";
    private static String FIRETICKS = "fire-ticks";

    public Pyro() {
        super("Pyro", "Make the entities around on fire");

        setMaxLevel(3, 3);
        setWeight(0.75);

        settings.set(DISTANCE, 3, 0.5);
        settings.set(TIME, 4, 3);
        settings.set(FIRETICKS, 20, 10);
        Cooldowns.configure(settings, 5, 4);

        addNaturalItems(Material.STICK, Material.BLAZE_ROD);
    }

    public void applyInteractBlock(Player user, int level, PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
        if (Cooldowns.onCooldown(this, user, settings, level)) return;
        Cooldowns.start(this, user);
        SmokeEffect smoke = new SmokeEffect(EffectLibUtils.getEffectLib());
        FlameEffect flame = new FlameEffect(EffectLibUtils.getEffectLib());
        smoke.setEntity(user);
        flame.setEntity(user);
        smoke.infinite();
        flame.infinite();
        smoke.start();
        flame.start();
        int fireticks = (int) settings.get(FIRETICKS, level);
        int time = (int) (settings.get(TIME, level) * 20);
        tasks.put(user.getUniqueId(), Tasks.schedule(() -> {
            for (Entity entity : user.getNearbyEntities(1, 2, 1)) {
                if (entity instanceof LivingEntity) {
                    entity.setFireTicks(fireticks);
                }
            }
        }, 0, 0));
        new BukkitRunnable() {
            @Override
            public void run() {
                tasks.remove(user.getUniqueId()).cancel();
                smoke.cancel();
                flame.cancel();
            }
        }.runTaskLater(EnchantMagicPack.getInstace(), time);
    }
}
