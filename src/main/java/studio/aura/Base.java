package studio.aura;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.aura.utils.CC;

public class Base {

    private final Guardian guardian;
    private final FLog fLog = new FLog(Guardian.getInstance());

    public Base(Guardian guardian){
        this.guardian = guardian;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    public void botDetected(Player player, String kickReason){

        player.kickPlayer(CC.toColor(kickReason));
    }

    public void check(Player player) {
        boolean enabled = getGuardian().getConfig().getBoolean("inventory.enabled");
        Inventory inventory = Bukkit.createInventory(
                player,
                getGuardian().getConfig().getInt("inventory.size"),
                CC.toColor(getGuardian().getConfig().getString("inventory.title"))
        );
        for (final String i : getGuardian().getConfig().getConfigurationSection("inventory.items").getKeys(false)) {
            ItemStack item = new ItemStack(Material.valueOf(getGuardian().getConfig().getString("inventory.items." + i + ".item")));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(CC.toColor(getGuardian().getConfig().getString("inventory.items." + i + ".name")));
            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        if (enabled) {
            player.openInventory(inventory);
        }
    }

    public boolean isProtectionEnabled(){
        return getGuardian().getProtection().getBoolean("anti-bot");

    }

    public void setBotProtection(boolean protection) {
        getGuardian().getProtection().set("anti-bot", protection);
        fLog.save();
    }

    public void setCommandProtection(boolean protection){
        getGuardian().getProtection().set("anti-command", protection);
    }
}
