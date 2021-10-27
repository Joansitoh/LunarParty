package me.joansiitoh.lunarparty.commands.args;

import club.skilldevs.utils.ChatUtils;
import club.skilldevs.utils.commands.Commnds;
import club.skilldevs.utils.texts.DefaultFontInfo;
import club.skilldevs.utils.texts.FancyMessage;
import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.commands.PartyArgument;
import me.joansiitoh.lunarparty.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ListArgument extends PartyArgument {

    public ListArgument() {
        super("list", "lunarparty.list", 1);
        setDescription("Show party members.");
    }

    @Override
    public void execute(Player player, String[] args) {
        Party party = Party.getPlayerParty(player);
        if (party == null) {
            player.sendMessage(Language.NOT_IN_PARTY.toString(true));
            return;
        }

        DefaultFontInfo.sendCenteredMessage(player, ChatColor.AQUA + ChatUtils.CHAT_BAR);
        player.sendMessage("");
        DefaultFontInfo.sendCenteredMessage(player, "&f&lPARTY MEMBER LIST");

        for (UUID uuid : party.getMembers()) {
            FancyMessage message = new FancyMessage("");
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            Player member = Bukkit.getPlayer(uuid);
            if (member != null) name = member.getName();

            boolean owner = party.getOwner().equals(uuid);

            message.then("&8[&a&l?&8]").tooltip("&7| &eParty rank: &f" + (owner ? "Owner" : "Member")).then(" ");

            if (party.getOwner().equals(player.getUniqueId()) && !name.equalsIgnoreCase(player.getName())) {
                message.then("&8[&e&l+&8]").tooltip("&7| &eClick to promote").command("/party promote " + name).then(" ");
                message.then("&8[&c&l-&8]").tooltip("&7| &eClick to kick").command("/party kick " + name).then(" ");
            }

            boolean connected = member != null && member.isOnline();
            message.then((connected ? "&a" : "&c") + name + " &o" + (connected ? "(Online)" : "(Offline)"));
            message.send(player);
        }

        player.sendMessage("");
        DefaultFontInfo.sendCenteredMessage(player, ChatColor.AQUA + ChatUtils.CHAT_BAR);
    }

}
