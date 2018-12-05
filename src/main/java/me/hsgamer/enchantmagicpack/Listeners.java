package me.hsgamer.enchantmagicpack;

import com.sucy.enchant.api.CustomEnchantment;
import com.sucy.enchant.api.Enchantments;
import me.hsgamer.enchantmagicpack.enchants.death.DeathEnchantment;
import me.hsgamer.enchantmagicpack.enchants.projectile.hit.ProjectileHit;
import me.hsgamer.enchantmagicpack.enchants.projectile.shoot.ProjectileShoot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

import java.util.*;

public class Listeners implements Listener {
    Listeners(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private static Hashtable<Projectile, HashMap<ProjectileHit, Integer>> HIT_ENCHANTS = new Hashtable<>();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onShoot(EntityShootBowEvent event) {
        if (event.isCancelled()) return;
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        Entity projectile = event.getProjectile();
        if (!(projectile instanceof Projectile)) return;
        ItemStack item = event.getBow();
        Map<CustomEnchantment, Integer> enchants = Enchantments.getCustomEnchantments(event.getBow());
        HashMap<ProjectileHit, Integer> hitEnchants = new HashMap<>();
        enchants.forEach((enchant, level) -> {
            if (enchant instanceof ProjectileShoot)
                ((ProjectileShoot) enchant).applyProjectileShoot(entity, item, level, event);
            else if (enchant instanceof ProjectileHit) {
                hitEnchants.put((ProjectileHit) enchant, level);
            }
        });
        if (!enchants.isEmpty()) HIT_ENCHANTS.put((Projectile) projectile, hitEnchants);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHit(ProjectileHitEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        if (!(shooter instanceof Player)) return;
        Projectile projectile = event.getEntity();
        if (HIT_ENCHANTS.contains(projectile)) HIT_ENCHANTS.remove(projectile).forEach((enchant, level) -> enchant.applyProjectileHit((LivingEntity) shooter, level, event));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getEntity();
        Map<CustomEnchantment, Integer> enchants = Enchantments.getEnchantments(player);
        enchants.forEach((enchant, level) -> {
            if (enchant instanceof DeathEnchantment) {
                ((DeathEnchantment) enchant).applyOnDeath(player, level, event);
            }
        });
    }
}
