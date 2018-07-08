package eu.kennytv.serverlistmotd.core;

import eu.kennytv.serverlistmotd.api.ISettings;

import java.util.List;
import java.util.Random;

public abstract class Settings implements ISettings {
    private static final Random RANDOM = new Random();
    private boolean changePlayerCount;
    private boolean showPlayerCount;
    private List<String> motd;
    private String playerCountMessage;
    private String playerCountHoverMessage;
    private String noPermMessage;

    protected void loadSettings() {
        updateConfig();
        changePlayerCount = getConfigBoolean("custom-playercountmessage");
        showPlayerCount = getConfigBoolean("show-playercount");
        motd = getConfigList("motds");
        noPermMessage = getConfigString("no-permission-message");
        playerCountMessage = getConfigString("playercountmessage");
        playerCountHoverMessage = getConfigString("playercounthovermessage");
    }

    @Deprecated
    public abstract void updateConfig();

    @Override
    public String getMotd() {
        return motd.size() > 1 ? motd.get(RANDOM.nextInt(motd.size())) : motd.get(0);
    }

    @Override
    public String getPlayerCountMessage() {
        return playerCountMessage;
    }

    @Override
    public String getPlayerCountHoverMessage() {
        return playerCountHoverMessage;
    }

    @Override
    public boolean hasCustomPlayerCount() {
        return changePlayerCount;
    }

    @Override
    public boolean showPlayerCount() {
        return showPlayerCount;
    }

    public abstract void saveConfig();

    public abstract void setToConfig(String path, Object var);

    public abstract String getConfigString(String path);

    public abstract String getRawConfigString(String path);

    public abstract boolean getConfigBoolean(String path);

    public abstract List<String> getConfigList(String path);

    public String getNoPermMessage() {
        return noPermMessage;
    }
}
