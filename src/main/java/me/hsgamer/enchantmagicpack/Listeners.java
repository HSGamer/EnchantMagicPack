package me.hsgamer.enchantmagicpack;

import com.sucy.enchant.api.CustomEnchantment;
import com.sucy.enchant.api.Enchantments;
import me.hsgamer.enchantmagicpack.enchants.death.DeathEnchantment;
import me.hsgamer.enchantmagicpack.enchants.projectile.ProjectileHit;
import me.hsgamer.enchantmagicpack.enchants.projectile.ProjectileShoot;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Listeners implements Listener {
    private static final Map<UUID, Map<ProjectileHit, Integer>> HIT_ENCHANTS = new HashMap<>();

    Listeners(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onShoot(EntityShootBowEvent event) {
        LivingEntity entity = event.getEntity();
        UUID projectile = event.getProjectile().getUniqueId();
        ItemStack item = event.getBow();
        Map<CustomEnchantment, Integer> enchants = Enchantments.getCustomEnchantments(event.getBow());
        Map<ProjectileHit, Integer> hitEnchants = new HashMap<>();
        enchants.forEach((enchant, level) -> {
            if (enchant instanceof ProjectileShoot projectileShoot)
                projectileShoot.applyProjectileShoot(entity, item, level, event);
            else if (enchant instanceof ProjectileHit projectileHit)
                hitEnchants.put(projectileHit, level);
        });
        if (!enchants.isEmpty()) HIT_ENCHANTS.put(projectile, hitEnchants);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHit(ProjectileHitEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        if (!(shooter instanceof LivingEntity livingEntity)) return;
        UUID projectile = event.getEntity().getUniqueId();
        if (HIT_ENCHANTS.containsKey(projectile))
            HIT_ENCHANTS.remove(projectile).forEach((enchant, level) -> enchant.applyProjectileHit(livingEntity, level, event));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Map<CustomEnchantment, Integer> enchants = Enchantments.getEnchantments(player);
        enchants.forEach((enchant, level) -> {
            if (enchant instanceof DeathEnchantment deathEnchantment)
                deathEnchantment.applyOnDeath(player, level, event);
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTntDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof TNTPrimed tnt)) {
            return;
        }
        if (tnt.hasMetadata(Utils.DAMAGE_META) && isAnyFalse(tnt.getMetadata(Utils.DAMAGE_META))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTntBreak(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed tnt)) {
            return;
        }
        if (tnt.hasMetadata(Utils.BREAK_META) && isAnyFalse(tnt.getMetadata(Utils.BREAK_META))) {
            event.blockList().clear();
        }
    }

    private boolean isAnyFalse(List<MetadataValue> list) {
        for (MetadataValue value : list) {
            if (!value.asBoolean()) {
                return true;
            }
        }
        return false;
    }
}
