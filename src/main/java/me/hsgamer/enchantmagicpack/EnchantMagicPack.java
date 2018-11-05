package me.hsgamer.enchantmagicpack;

import com.sucy.enchant.api.EnchantPlugin;
import com.sucy.enchant.api.EnchantmentRegistry;
import me.hsgamer.enchantmagicpack.enchants.AutoTool;
import me.hsgamer.enchantmagicpack.enchants.Pyro;
import me.hsgamer.enchantmagicpack.enchants.TimeFreezing;
import me.hsgamer.enchantmagicpack.utils.EffectLibUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantMagicPack extends JavaPlugin implements EnchantPlugin {
    private static EnchantMagicPack instace;

    public static EnchantMagicPack getInstace() {
        return instace;
    }

    @Override
    public void onEnable() {
        instace = this;
        EffectLibUtils.inputEffectLib(instace);
    }

    @Override
    public void onDisable() {
        instace = null;
        EffectLibUtils.clear();
    }

    @Override
    public void registerEnchantments(EnchantmentRegistry enchantmentRegistry) {
        enchantmentRegistry.register(
                new TimeFreezing(),
                new AutoTool(),
                new Pyro()
        );
    }
}
