package me.joansiitoh.lunarparty;

import com.lunarclient.bukkitapi.nethandler.client.LCPacketTeammates;
import lombok.Getter;
import me.joansiitoh.lunarparty.commands.test;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.lunarclient.bukkitapi.LunarClientAPI;

import java.util.*;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 11/09/2021 - 16:31.
 */
@Getter
public class sLunar extends JavaPlugin {

    public static HashMap<Player, List<Player>> leaderHash = new HashMap<>();
    public static HashMap<Player, Player> memberHash = new HashMap<>();

    public static void updatePlayer(Player... list) {
        for (Player player : list) {
            List<Player> members = new ArrayList<>();
            if (leaderHash.containsKey(player)) members = leaderHash.get(player);
            else if (memberHash.containsKey(player)) {
                Player leader = memberHash.get(player);
                members.add(leader);
                members.addAll(leaderHash.get(leader));
                members.remove(player);
            }

            INSTANCE.getLunarClientAPI().sendTeammates(player,
                    new LCPacketTeammates(player.getUniqueId(), System.currentTimeMillis(), getTeamHash(player, members))
            );
        }
    }

    public static Map<UUID, Map<String, Double>> getTeamHash(Player player, List<Player> targets) {
        Map<UUID, Map<String, Double>> playerMap = new HashMap<>();
        for (Player member : targets) {
            Map<String, Double> posMap = new HashMap<>();

            posMap.put("x", member.getLocation().getX());
            posMap.put("y", member.getLocation().getY());
            posMap.put("z", member.getLocation().getZ());

            playerMap.put(member.getUniqueId(), posMap);
        }

        Map<String, Double> posMap = new HashMap<>();

        posMap.put("x", player.getLocation().getX());
        posMap.put("y", player.getLocation().getY());
        posMap.put("z", player.getLocation().getZ());

        playerMap.put(player.getUniqueId(), posMap);
        return playerMap;
    }


    ///////////////////////////////////////////////////////

    public static sLunar INSTANCE;

    private LunarClientAPI lunarClientAPI;

    public void onEnable() {
        INSTANCE = this;

        lunarClientAPI = new LunarClientAPI();

        getCommand("test").setExecutor(new test());
    }

    public void onDisable() {

    }

    public void registerListeners(Listener... listeners) {
        for (Listener listener : listeners)
            getServer().getPluginManager().registerEvents(listener, this);
    }

}
