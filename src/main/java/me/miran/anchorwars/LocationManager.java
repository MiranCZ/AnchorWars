package me.miran.anchorwars;

import org.bukkit.Location;

import java.util.ArrayList;

public class LocationManager {

    public String compileLoc(Location loc, boolean pitchAYaw) {
        if (loc == null) {
            return null;
        }

        String newLoc = Math.floor(loc.getX()) + ";" + Math.floor(loc.getY()) + ";" + Math.floor(loc.getZ());

        if (pitchAYaw) {
            newLoc = newLoc + ";" + loc.getYaw();
            newLoc = newLoc + ";" + loc.getPitch();

        }

        return newLoc;
    }

    public Location decompileLoc(String loc) {

        if (loc == null) {
            return new Location(null, 0, 0, 0);
        }
        Location newLoc = new Location(null, 0, 0, 0, 0, 0);

        char[] text = loc.toCharArray();
        ArrayList<Double> variables = new ArrayList<>();
        String str = "";


        for (int i = 0; i < text.length; i++) {
            String s = String.valueOf(text[i]);

            boolean save = false;

            if (!s.equals(";")) {
                str = str + s;

            } else {
                save = true;
            }
            if (text.length - 1 == i) {
                save = true;
            }

            if (save) {
                variables.add(Double.parseDouble(str));
                str = "";
            }
        }
        newLoc.setX(variables.get(0));
        newLoc.setY(variables.get(1));
        newLoc.setZ(variables.get(2));
        if (variables.size() > 3) {
            int yaw = (int) Math.floor(variables.get(3));
            newLoc.setYaw(yaw);
            int pitch = (int) Math.floor(variables.get(4));
            newLoc.setPitch(pitch);

        }

        return newLoc;
    }

}
