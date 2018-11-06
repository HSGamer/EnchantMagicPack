package me.hsgamer.enchantmagicpack.enchants.projectile.shoot;

import com.sucy.enchant.api.CustomEnchantment;
import me.hsgamer.enchantmagicpack.ConflictGroup;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ProjectileShoot extends CustomEnchantment {
    ProjectileShoot(String name, String description) {
        super(name, description);

        addNaturalItems(Material.BOW);
        setGroup(ConflictGroup.PROJECTILE_SHOOT);
    }

    public void applyProjectileShoot(LivingEntity user, ItemStack bow, int level, EntityShootBowEvent event) {
    }
}
