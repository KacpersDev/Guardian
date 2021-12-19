package studio.aura.bot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import studio.aura.Base;
import studio.aura.Guardian;
import studio.aura.utils.CC;

import java.util.ArrayList;

public class BListener implements Listener {

    private final studio.aura.Guardian guardian;
    private final Base base;
    private final ArrayList<Player> underDetection = new ArrayList<>();

    public BListener(studio.aura.Guardian guardian){
        this.guardian = guardian;
        base = new Base(studio.aura.Guardian.getInstance());
    }

    public Guardian getGuardian() {
        return guardian;
    }

    public Base getBase() {
        return base;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (!player.hasPermission("guardian.bot.bypass") || !player.isOp()) {
            if (base.isProtectionEnabled()) {
                underDetection.add(player);
                Bukkit.getScheduler().runTaskTimer(getGuardian(), new Runnable() {
                    int tick = 1;

                    @Override
                    public void run() {

                        tick--;

                        if (tick == 0 || tick < 0) {
                            getBase().check(player);
                            Bukkit.getScheduler().cancelAllTasks();
                        }
                    }
                }, 0L, 20L);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryOpen(InventoryClickEvent event) {

        final boolean staff = Guardian.getInstance().getConfig().getBoolean("log.staff.enabled");
        final boolean console = Guardian.getInstance().getConfig().getBoolean("log.console.enabled");

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getTitle().equalsIgnoreCase(CC.toColor(getGuardian().getConfig().getString("inventory.title")))) {
            event.setCancelled(true);
        }
        if (event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(CC.toColor(getGuardian().getConfig().getString("right-name")))) {
            if (underDetection.contains(player)) {
                underDetection.remove(player);
                player.closeInventory();
                player.updateInventory();
            }
        } else {
            if (underDetection.contains(player)) {
                underDetection.remove(player);
                base.botDetected(player, getGuardian().getConfig().getString("kick-message"));
                if (staff) {
                    for (final Player online : Bukkit.getOnlinePlayers()){
                        if (online.hasPermission("guardian.log.see")) {
                            online.sendMessage(CC.toColor(getGuardian().getConfig().getString("log.staff.message")
                                    .replace("%player%", player.getName())));
                        }
                    }
                }
                if (console) {
                    Bukkit.getConsoleSender().sendMessage(CC.toColor(getGuardian().getConfig().getString("log.console.message").replace("%player%", player.getName())));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){

        Player player = (Player) event.getPlayer();

        if (event.getInventory() == null) return;
        if (event.getInventory().getTitle().equalsIgnoreCase(CC.toColor(getGuardian().getConfig().getString("inventory.title")))) {
            Bukkit.getScheduler().runTaskTimer(getGuardian(), new Runnable() {

                int tick = 1;

                @Override
                public void run() {

                    tick--;

                    if (tick == 0 || tick < 0) {
                        if (underDetection.contains(player)) {
                            player.openInventory(event.getInventory());
                            Bukkit.getScheduler().cancelAllTasks();
                            return;
                        }
                    }
                }
            }, 0L, 20L);
        }
    }
}
