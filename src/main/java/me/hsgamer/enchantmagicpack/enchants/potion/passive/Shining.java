package me.hsgamer.enchantmagicpack.enchants.potion.passive;

import com.sucy.enchant.api.ItemSet;
import org.bukkit.potion.PotionEffectType;

public class Shining extends PotionPassive {

    public Shining() {
        super("Shining", "Make your body glowing");

        setMaxLevel(1);

        addNaturalItems(ItemSet.CHESTPLATES.getItems());
    }

    /**
     * @return type of potion applied by this enchantment
     */
    public PotionEffectType type() {
        return PotionEffectType.GLOWING;
    }
}
