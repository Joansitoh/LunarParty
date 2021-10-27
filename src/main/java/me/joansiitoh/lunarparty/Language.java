package me.joansiitoh.lunarparty;

import club.skilldevs.utils.ChatUtils;
import club.skilldevs.utils.FileConfig;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Getter
public enum Language {

    PREFIX("PREFIX", "DEFAULT", "&5[LunarParty]&f: "),

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    NO_PERMISSION("NO-PERMISSION", "DEFAULT", "&fYou &cdon't &fhave permissions."),
    PLAYER_NOT_FOUND("PLAYER-NOT-FOUND", "DEFAULT", "&fPlayer &c<party_target> &fnot found."),

    NOT_NUMBER("NOT-NUMBER", "DEFAULT", "&c<party_value> &fis not a valid number."),
    UNDER_ZERO("NEED-POSITIVE-NUMBER", "DEFAULT", "&c<party_value> &fis a negative value."),

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    CREATED("CREATED", "PARTY.DEFAULT", "&fParty &acreated &fcorrectly."),

    LEAVE("LEAVE", "PARTY.DEFAULT", "&fYou &cleave &fyour party."),
    LEFT("LEFT", "PARTY.DEFAULT", "&c<party_target> &fhas &cleft &fthe party."),

    DISBAND("DISBAND", "PARTY.DEFAULT", "&fYou &cparty &fhas been &cdisband&f."),
    DISBANDED("DISBANDED", "PARTY.DEFAULT", "&fLeader has &cdisband &fthe party&f."),

    INVITE("INVITE", "PARTY.DEFAULT", "&fYou invite &a<party_target> &fto your party."),
    INVITE_MAX("INVITE-LIMIT", "PARTY.DEFAULT", "&fYou &cexceded &fthe member limit."),
    INVITED_HOVER("INVITED_HOVER", "PARTY.DEFAULT", "&7| &eClick to join."),
    INVITED("INVITED", "PARTY.DEFAULT",
            "&fYou have been invited to &a<party_target>&f's party.",
            "&fUse &e/party accept <party_target> &for click <hover>&e&lHERE</hover> &fto join."),

    NOT_INVITED("NOT-INVITED", "PARTY.DEFAULT", "&c<party_target> &fhas &cnot &finvite you."),
    ALREADY_INVITED("ALREADY-INVITED", "PARTY.DEFAULT", "&fYou &calready invited &c<party_target> &fto party."),

    KICK("KICK", "PARTY.DEFAULT", "&c<party_target> &fhas been &ckicked &ffrom party."),
    KICKED("KICKED", "PARTY.DEFAULT", "&fYou have been &ckicked &ffrom party."),

    ACCEPT("ACCEPT", "PARTY.DEFAULT", "&fYou have been &aaccept &fthe party invitation."),
    ACCEPTED("ACCEPTED", "PARTY.DEFAULT", "&a<party_target> &fhas &aaccept &fthe party invitation."),

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    PLAYER_NOT_IN_PARTY("PLAYER-NOT-IN-PARTY", "PARTY.ERRORS", "&c<party_target> &fis &cnot &fin a party."),
    PLAYER_NOT_IN_YOUR_PARTY("PLAYER-NOT-IN-YOUR-PARTY", "PARTY.ERRORS", "&c<party_target> &fis not in your party."),

    NOT_IN_PARTY("NOT-IN-PARTY", "PARTY.ERRORS", "&fYou are &cnot &fin a party."),
    ONLY_OWNER("ONLY-OWNER", "PARTY.ERRORS", "&fOnly &cparty owner &fcan do this."),

    ALREADY_IN_PARTY("ALREADY-IN-PARTY", "PARTY.ERRORS", "&fYou are &calready &fon a party."),
    ALREADY_HAS_PARTY("ALREADY-HAS-PARTY", "PARTY.ERRORS", "&c<party_target> &falready has party."),

    ;


    private final String path;
    private final String subPath;

    private String def;
    private List<String> defList;

    private static final FileConfig config = new FileConfig(sLunar.INSTANCE, "language.yml");

    Language(String path, String def) {
        this.path = path;
        this.subPath = "";
        this.def = def;
    }

    Language(String path, String subPath, String... defList) {
        this.path = path;
        this.subPath = subPath;
        this.defList = Arrays.asList(defList);
        this.def = null;
    }

    Language(String path, String subPath, String def) {
        this.path = path;
        this.subPath = subPath;
        this.defList = null;
        this.def = def;
    }

    private String getFinalPath() {
        return this.subPath.equalsIgnoreCase("") ? this.path : this.subPath + "." + this.path;
    }

    public String toString(boolean prefix) {
        return (prefix ? Language.PREFIX.toString(false) : "") + ChatUtils.translate(config.getString(getFinalPath(), this.def, true));
    }

    public String toString() {
        return ChatUtils.translate(config.getString(getFinalPath(), this.def, true));
    }

    public List<String> toStringList() {
        return config.getStringList(getFinalPath());
    }

    public static void load() {
        for (Language item : Language.values()) {
            if (!config.getConfiguration().contains(item.getFinalPath())) {
                config.getConfiguration().set(item.getFinalPath(), item.getDef() == null || item.getDef().equalsIgnoreCase("") ? item.getDefList() : item.getDef());
            }
        }

        config.save();
        config.setHeader(
                "#######################################",
                "",
                "This is language file config.",
                "You have multiple variables depending the context.",
                "",
                "VARIABLES:",
                "* <party_target> | Get the target in arguments with players.",
                "* <party_value> | Get and extra value of string.",
                "  - Example: '<party_value> is not a number'",
                "",
                "#######################################"
        );
    }


}
