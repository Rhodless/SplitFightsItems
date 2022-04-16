package fr.splifight.items.utils;

import org.bukkit.entity.Player;

public class LocationUtils {

    public static String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return "N";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "N";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "E";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "S";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "O";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "O";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return "O";
        }
    }

}
