package me.joansiitoh.lunarparty.commands;

import com.lunarclient.bukkitapi.object.LCWaypoint;
import me.joansiitoh.lunarparty.sLunar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 11/09/2021 - 16:58.
 */
public class test implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("create")) {
                new CreateCMD().execute((Player) sender);
            }
            return true;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                Player target = Bukkit.getPlayer(args[1]);
                new AddCMD().execute((Player) sender, target);
            }
        }

        return false;
    }

}
