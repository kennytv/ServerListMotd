package eu.kennytv.serverlistmotd.spigot;

import com.google.common.collect.Lists;
import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.listener.IPingListener;
import eu.kennytv.serverlistmotd.spigot.listener.PacketListener;
import eu.kennytv.serverlistmotd.spigot.listener.ServerListPingListener;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public final class SettingsSpigot extends Settings {
    private final ServerListMotdSpigotBase plugin;
    private final IPingListener pingListener;
    private FileConfiguration config;

    SettingsSpigot(final ServerListMotdSpigotBase plugin) {
        this.plugin = plugin;

        final PluginManager pm = plugin.getServer().getPluginManager();
        if (pm.getPlugin("ProtocolLib") == null) {
            final ServerListPingListener listener = new ServerListPingListener(this);
            pm.registerEvents(listener, plugin);
            pingListener = listener;
        } else
            pingListener = new PacketListener(plugin, this);

        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        final File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (final InputStream in = plugin.getResource("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (final IOException e) {
                throw new RuntimeException("Unable to create config.yml!", e);
            }
        }

        reloadConfig();
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
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reloadConfig() {
        final File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), "UTF8"));
        } catch (final IOException e) {
            throw new RuntimeException("Unable to load config.yml!", e);
        }

        loadSettings();
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
    public void setToConfig(final String path, final Object var) {
        config.set(path, var);
    }

    @Override
    public boolean reloadServerIcon() {
        return pingListener.loadIcon();
    }
}
