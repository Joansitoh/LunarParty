package me.joansiitoh.lunarparty.commands;

import club.skilldevs.utils.ChatUtils;
import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.commands.args.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 22/09/2021 - 13:43.
 */
public class PartyCMD extends BukkitCommand {

    private final List<PartyArgument> arguments = new ArrayList<>();
    private final HashMap<Integer, List<PartyArgument>> pageMap = new HashMap<>();

    public PartyCMD() {
        super("party");

        arguments.add(new CreateArgument());
        arguments.add(new DisbandArgument());
        arguments.add(new LeaveArgument());
        arguments.add(new AcceptArgument());
        arguments.add(new InviteArgument());
        arguments.add(new KickArgument());
        arguments.add(new PromoteArgument());
        arguments.add(new ListArgument());

        int page = 1;
        for (int x = 0; x < arguments.size(); x++) {
            if (x != 0 && x % 5 == 0) page++;

            pageMap.putIfAbsent(page, new ArrayList<>());
            List<PartyArgument> list = pageMap.get(page);
            list.add(arguments.get(x));
        }
    }

    /**
     *
     * @param sender
     * @param label
     * @param args
     * @return
     */

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Language.PREFIX.toString() + ChatUtils.translate("&fUse &a/" + label + " help &ffor more information."));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(sender, 1);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("help")) {
            if (!ChatUtils.isNumeric(args[1])) {
                sender.sendMessage(Language.PREFIX + Language.NOT_NUMBER.toString().replace("<party_value>", args[1]));
                return true;
            }

            sendHelpMessage(sender, Integer.parseInt(args[1]));
            return true;
        }

        HashMap<String, PartyArgument> argumentHash = new HashMap<>();
        for (PartyArgument argument : arguments) {
            argumentHash.put(argument.getArgument(), argument);
            if (!args[0].equalsIgnoreCase(argument.getArgument())) continue;
            if (!sender.hasPermission(argument.getPermission())) {
                sender.sendMessage(Language.PREFIX.toString() + Language.NO_PERMISSION.toString());
                return true;
            }

            if (args.length != argument.getArguments()) {
                sender.sendMessage(Language.PREFIX.toString() + "Usage /" + label + " " + args[0] + " " + (argument.getUsage() != null ? argument.getUsage() : ""));
                return true;
            }

            argument.execute((Player) sender, args);
            return true;
        }

        sender.sendMessage(Language.PREFIX.toString() + ChatUtils.translate("&fUse &a/" + label + " help &ffor more information."));
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 0) return list;

        if (sender.hasPermission("sweconomy.admin")) {
            for (PartyArgument argument : arguments) {
                if (args.length > 1 && args[0].equalsIgnoreCase(argument.getArgument()))
                    return argument.tabComplete(sender, alias, args);

                if (argument.getArgument().toLowerCase().startsWith(args[0].toLowerCase()))
                    list.add(argument.getArgument());
            }
        }

        if (list.size() == 0) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    list.add(player.getName());
            });
        }
        Collections.sort(list);
        return list;
    }

    private String getCommandMessage(String command, String description) {
        return ChatUtils.translate("&f* &b/" + command + " &7| &f" + description);
    }

    private void sendHelpMessage(CommandSender sender, int page) {
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.translate("&f&l>> &a&lLUNAPARTY HELP (PAGE " + page + ")"));

        if (pageMap.containsKey(page)) {
            for (PartyArgument argument : pageMap.get(page)) {
                sender.sendMessage(getCommandMessage(getLabel() + " " + argument.getArgument() + (argument.getUsage() == null ? "" : " " + argument.getUsage()),
                        argument.getDescription()));
            }
        }

        sender.sendMessage("");

        if (page != 1)
            sender.sendMessage(getCommandMessage(getLabel() + " help " + (page - 1), "Show page help " + (page - 1)));

        if (page < pageMap.size()) {
            sender.sendMessage(getCommandMessage(getLabel() + " help " + (page + 1), "Show page help " + (page + 1)));
        }

        sender.sendMessage("");
    }

}
