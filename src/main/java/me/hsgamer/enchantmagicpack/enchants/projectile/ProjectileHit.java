package me.hsgamer.enchantmagicpack.enchants.projectile;

import me.hsgamer.enchantmagicpack.ConflictGroup;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import studio.magemonkey.fabled.enchants.api.CustomEnchantment;

public abstract class ProjectileHit extends CustomEnchantment {
    ProjectileHit(String name, String description) {
        super(name, description);

        addNaturalItems(Material.BOW);
        setGroup(ConflictGroup.PROJECTILE_HIT);
    }

    public void applyProjectileHit(LivingEntity user, int level, ProjectileHitEvent event) {
    }
}
