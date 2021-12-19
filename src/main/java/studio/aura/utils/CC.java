package studio.aura.utils;

import org.bukkit.ChatColor;

public class CC {

    public static String toColor(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
