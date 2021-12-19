package studio.aura;

import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class FLog {

    private final Guardian guardian;

    public FLog(Guardian guardian){
        this.guardian = guardian;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    public void save(){

        try {
            getGuardian().checkConfiguration.save(getGuardian().check);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            getGuardian().checkConfiguration.load(getGuardian().check);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
