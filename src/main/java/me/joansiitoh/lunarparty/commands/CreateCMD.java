package me.joansiitoh.lunarparty.commands;

import me.joansiitoh.lunarparty.sLunar;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 29/09/2021 - 17:36.
 */
public class CreateCMD {

    public void execute(Player p) {
        if (sLunar.leaderHash.containsKey(p) || sLunar.memberHash.containsKey(p)) {
            p.sendMessage("Ya tienes party");
            return;
        }

        sLunar.leaderHash.put(p, new ArrayList<>());
        p.sendMessage("party creada");
    }

}
