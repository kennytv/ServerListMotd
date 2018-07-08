package eu.kennytv.serverlistmotd.bungee;

import com.google.common.collect.Lists;
import eu.kennytv.serverlistmotd.bungee.listener.ProxyPingListener;
import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.listener.IPingListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public final class SettingsBungee extends Settings {
    private final ServerListMotdBungeeBase plugin;
    private final IPingListener pingListener;
    private Configuration config;

    SettingsBungee(final ServerListMotdBungeeBase plugin) {
        this.plugin = plugin;

        final PluginManager pm = plugin.getProxy().getPluginManager();
        final ProxyPingListener listener = new ProxyPingListener(this);
        pm.registerListener(plugin, listener);
        pingListener = listener;

        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();

        final File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (final InputStream in = plugin.getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (final IOException e) {
                throw new RuntimeException("Unable to create config.yml!", e);
            }
        }

        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        final File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            config = YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file), "UTF8"));
        } catch (final IOException e) {
            throw new RuntimeException("Unable to load config.yml!", e);
        }

        loadSettings();
    }

    @Override
    public void updateConfig() {
        if (!config.contains("motd")) return;
        config.set("motds", Lists.newArrayList(getConfigString("motd")));
        config.set("motd", null);
        saveConfig();
    }

    @Override
    public void saveConfig() {
        final File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            YamlConfiguration.getProvider(YamlConfiguration.class).save(config, new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
        } catch (final IOException e) {
            throw new RuntimeException("Unable to save config.yml!", e);
        }
    }

    @Override
    public void setToConfig(final String path, final Object var) {
        config.set(path, var);
    }

    @Override
    public String getConfigString(final String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }

    @Override
    public String getRawConfigString(final String path) {
        return config.getString(path);
    }

    @Override
    public boolean getConfigBoolean(final String path) {
        return config.getBoolean(path);
    }

    @Override
    public List<String> getConfigList(final String path) {
        return config.getStringList(path);
    }

    @Override
    public boolean reloadServerIcon() {
        return pingListener.loadIcon();
    }
}
