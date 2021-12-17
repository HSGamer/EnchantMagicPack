package me.hsgamer.enchantmagicpack;

import com.sucy.enchant.api.EnchantPlugin;
import com.sucy.enchant.api.EnchantmentRegistry;
import me.hsgamer.enchantmagicpack.enchants.active.*;
import me.hsgamer.enchantmagicpack.enchants.death.SelfExplode;
import me.hsgamer.enchantmagicpack.enchants.passive.AutoTool;
import me.hsgamer.enchantmagicpack.enchants.passive.Dodge;
import me.hsgamer.enchantmagicpack.enchants.passive.HitBehind;
import me.hsgamer.enchantmagicpack.enchants.passive.StunHit;
import me.hsgamer.enchantmagicpack.enchants.projectile.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantMagicPack extends JavaPlugin implements EnchantPlugin {
    private static EnchantMagicPack instance;

    public static EnchantMagicPack getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        new Listeners(this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        instance = null;
    }

    @Override
    public void registerEnchantments(EnchantmentRegistry enchantmentRegistry) {
        enchantmentRegistry.register(
                new TimeFreezing(),
                new AutoTool(),
                new Pyro(),
                new HellRound(),
                new Laser(),
                new StrikeDown(),
                new Dodge(),
                new HitBehind(),
                new StunHit(),
                new ExplosiveArrow(),
                new FrozenArrow(),
                new SpikeArrow(),
                new Volley(),
                new SelfExplode(),
                new GravityArrow(),
                new ToxicArrow()
        );
    }
}
