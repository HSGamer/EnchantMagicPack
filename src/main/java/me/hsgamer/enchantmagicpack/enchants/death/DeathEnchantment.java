package me.hsgamer.enchantmagicpack.enchants.death;

import com.sucy.enchant.api.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEnchantment extends CustomEnchantment {
    DeathEnchantment(String name, String description) {
        super(name, description);
    }

    public void applyOnDeath(Player user, int level, PlayerDeathEvent event) {
    }
}
