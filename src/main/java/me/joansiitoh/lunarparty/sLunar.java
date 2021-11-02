package me.joansiitoh.lunarparty;

import club.skilldevs.utils.ChatUtils;
import club.skilldevs.utils.FileConfig;
import club.skilldevs.utils.sLoader;
import de.jeff_media.updatechecker.UpdateChecker;
import lombok.Getter;
import me.joansiitoh.lunarparty.commands.PartyCMD;
import me.joansiitoh.lunarparty.listeners.PlayerListeners;
import me.joansiitoh.lunarparty.party.PartyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.lunarclient.bukkitapi.LunarClientAPI;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 11/09/2021 - 16:31.
 */
@Getter
public class sLunar extends JavaPlugin {

    ///////////////////////////////////////////////////////

    public static sLunar INSTANCE;

    private FileConfig settingsFile;
    private PartyManager partyManager;
    private LunarClientAPI lunarClientAPI;

    private UpdateChecker updateChecker;

    public void onEnable() {
        INSTANCE = this;

        ///////////////////////////////////////////////

        this.settingsFile = new FileConfig(this, "settings.yml");
        this.partyManager = new PartyManager();
        this.lunarClientAPI = new LunarClientAPI();
        new sLoader(this);

        ///////////////////////////////////////////////

        updateChecker = UpdateChecker.init(this, 97182).checkEveryXHours(24).checkNow();
        Language.load();

        ///////////////////////////////////////////////

        registerCommands(new PartyCMD());
        registerListeners(new PlayerListeners());
    }

    public void onDisable() {

    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands(BukkitCommand... commands) {
        CommandMap commandMap;
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (Exception e) {
            logger("Error while register command.", "Command");
            return;
        }

        Arrays.stream(commands).forEach(command -> {
            commandMap.register(this.getName(), command);
            //this.commands.add(command);
        });
    }

   /* public void registerDatabaseConnection() {
        DatabaseCredentials databaseCredentials;
        ConfigCursor configCursor;
        FileConfig databaseConfig = this.getDatabaseConfig();

        switch (databaseConfig.getString("MODE".toUpperCase())) {
            case "SQL":
                configCursor = new ConfigCursor(databaseConfig, "SQL");
                databaseCredentials = new DatabaseCredentials(
                        configCursor.getString("HOST"),
                        configCursor.getInt("PORT"),
                        configCursor.getString("DATABASE"),
                        configCursor.getString("TABLE"),
                        configCursor.getString("USERNAME"),
                        configCursor.getString("PASSWORD"));
                databaseHandler = new SQLHandler(databaseCredentials);
                break;
            default:
                logger("Please provided a valid database type.");
                Bukkit.getScheduler().cancelTasks(this);
                Bukkit.getPluginManager().disablePlugin(this);
                break;
        }
    }*/

    //////////////////////////////////////////////////////////////////////////////////////////////

    public static void logger(String message) {
        logger(message, null);
    }

    public static void logger(String message, String subMsg) {
        if (!INSTANCE.getSettingsFile().getBoolean("ENABLE-DEBUG-LOG")) return;
        Bukkit.getConsoleSender().sendMessage(ChatUtils.translate(Language.PREFIX.getDef() + (subMsg != null ? ("&7[&d" + subMsg + "&7] &f") : "") + message));
    }

}
