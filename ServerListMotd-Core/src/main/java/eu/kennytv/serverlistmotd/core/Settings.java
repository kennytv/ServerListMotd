package eu.kennytv.serverlistmotd.core;

import eu.kennytv.serverlistmotd.api.ISettings;

public abstract class Settings implements ISettings {
    private boolean changePlayerCount;
    private boolean showPlayerCount;
    private String motd;
    private String playerCountMessage;
    private String playerCountHoverMessage;
    private String noPermMessage;

    protected void loadSettings() {
        changePlayerCount = getConfigBoolean("custom-playercountmessage");
        showPlayerCount = getConfigBoolean("show-playercount");
        motd = getConfigString("motd");
        noPermMessage = getConfigString("no-permission-message");
        playerCountMessage = getConfigString("playercountmessage");
        playerCountHoverMessage = getConfigString("playercounthovermessage");
    }

    @Override
    public String getMotd() {
        return motd;
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

    public String getNoPermMessage() {
        return noPermMessage;
    }
}
