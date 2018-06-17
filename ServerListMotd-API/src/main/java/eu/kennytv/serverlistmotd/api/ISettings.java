package eu.kennytv.serverlistmotd.api;

/**
 * @author KennyTV
 * @since 1.0
 */
public interface ISettings {

    String getMotd();

    void reloadConfig();

    String getPlayerCountMessage();

    String getPlayerCountHoverMessage();

    boolean reloadServerIcon();

    boolean hasCustomPlayerCount();

    boolean showPlayerCount();
}
