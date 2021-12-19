package studio.aura;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import studio.aura.bot.BCommand;
import studio.aura.bot.BListener;

import java.io.File;
import java.io.IOException;

public final class Guardian extends JavaPlugin {

    /*
    AUTHOR - KACPERM
    GITHUB - KACPERSDEV
     */

    private static Guardian instance;

    public File c;
    public FileConfiguration cc;
    public File check;
    public FileConfiguration checkConfiguration;

    @Override
    public void onEnable() {
        instance = this;
        file();
        listener();
        commands();
    }

    public static Guardian getInstance() {
        return instance;
    }

    public FileConfiguration getConfig() {
        return cc;
    }

    public FileConfiguration getProtection(){ return this.checkConfiguration; }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void file(){

        c = new File(getDataFolder(), "config.yml");
        if (!(c.exists())) {
            c.getParentFile().mkdir();
            saveResource("config.yml", false);
        }

        cc =new YamlConfiguration();
        try {
            cc.load(c);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        check = new File(getDataFolder(), "check.yml");
        if (!(check.exists())) {
            check.getParentFile().mkdir();
            saveResource("check.yml", false);
        }

        checkConfiguration = new YamlConfiguration();
        try {
            checkConfiguration.load(check);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void listener(){
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new BListener(this), this);
    }

    public void commands(){

        getCommand("guardian").setExecutor(new BCommand(this));
    }
}
