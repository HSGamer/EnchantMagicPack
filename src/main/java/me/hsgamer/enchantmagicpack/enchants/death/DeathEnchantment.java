package me.hsgamer.enchantmagicpack.enchants.death;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import studio.magemonkey.fabled.enchants.api.CustomEnchantment;

public abstract class DeathEnchantment extends CustomEnchantment {
    DeathEnchantment(String name, String description) {
        super(name, description);
    }

    public void applyOnDeath(Player user, int level, PlayerDeathEvent event) {
    }
}
