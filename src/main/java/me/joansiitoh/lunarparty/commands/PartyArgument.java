package me.joansiitoh.lunarparty.commands;

import lombok.Getter;
import lombok.Setter;
import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.sLunar;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 22/09/2021 - 13:48.
 */
@Getter @Setter
public abstract class PartyArgument {

    public final sLunar INSTANCE = sLunar.INSTANCE;

    private final String argument, permission;
    private final int arguments;

    private String description;
    private String usage;

    public PartyArgument(String argument, String permission, int arguments) {
        this.argument = argument;
        this.permission = permission;
        this.arguments = arguments;
    }

    public abstract void execute(Player player, String[] args);

    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        List<String> arguments = new ArrayList<>();
        int arg = 2, playerPos = -1;
        for (String use : usage.split(" ")) {
            if (use.contains("player")) playerPos = arg;
            arg++;
        }

        if (playerPos != -1 && args.length == playerPos) {
            int finalPlayerPos = playerPos;
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.getName().toLowerCase().startsWith(args[finalPlayerPos - 1].toLowerCase()))
                    arguments.add(player.getName());
            });
            return arguments;
        }
        return arguments;
    }

}
