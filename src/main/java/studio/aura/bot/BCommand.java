package studio.aura.bot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import studio.aura.Base;
import studio.aura.Guardian;
import studio.aura.utils.CC;

public class BCommand implements CommandExecutor {

    private final Guardian guardian;
    private final Base base = new Base(Guardian.getInstance());

    public BCommand(Guardian guardian){
        this.guardian = guardian;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }
        if (!(sender.hasPermission("guardian.command.allow"))) {
            return false;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            usage(player);
        } else {
            if (args[0].equalsIgnoreCase("disable")) {
                if (base.isProtectionEnabled()) {
                    base.setBotProtection(false);
                    player.sendMessage(CC.toColor(getGuardian().getConfig().getString("command.guardian.disabled")));
                }
            } else if (args[0].equalsIgnoreCase("enable")) {
                if (!base.isProtectionEnabled()) {
                    base.setBotProtection(true);
                    player.sendMessage(CC.toColor(getGuardian().getConfig().getString("command.guardian.enabled")));
                }
            }
        }

        return true;
    }

    private void usage(Player player) {

        for (final String i : getGuardian().getConfig().getStringList("command.guardian.wrong-usage")) {
            player.sendMessage(CC.toColor(i));
        }
    }
}
