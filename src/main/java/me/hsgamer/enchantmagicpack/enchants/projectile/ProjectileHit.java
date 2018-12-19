package me.hsgamer.enchantmagicpack.enchants.projectile;

import com.sucy.enchant.api.CustomEnchantment;
import me.hsgamer.enchantmagicpack.ConflictGroup;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;

public abstract class ProjectileHit extends CustomEnchantment {
    ProjectileHit(String name, String description) {
        super(name, description);

        addNaturalItems(Material.BOW);
        setGroup(ConflictGroup.PROJECTILE_HIT);
    }

    public void applyProjectileHit(LivingEntity user, int level, ProjectileHitEvent event) {
    }
}
