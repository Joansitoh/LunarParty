package me.joansiitoh.lunarparty.commands.args;

import club.skilldevs.utils.ChatUtils;
import club.skilldevs.utils.texts.DefaultFontInfo;
import club.skilldevs.utils.texts.FancyMessage;
import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.commands.PartyArgument;
import me.joansiitoh.lunarparty.party.Party;
import me.joansiitoh.lunarparty.sLunar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReloadArgument extends PartyArgument {

    public ReloadArgument() {
        super("reload", "lunarparty.staff", 1);
        setDescription("Reload plugin config.");
    }

    @Override
    public void execute(Player player, String[] args) {
        sLunar.INSTANCE.getSettingsFile().reloadConfig();
        player.sendMessage(Language.PREFIX.toString() + ChatUtils.translate("&fThe plugin &aconfig &fhas been &areloaded."));

        sLunar.INSTANCE.getUpdateChecker().checkNow(player);
    }

}
