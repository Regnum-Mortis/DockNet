package com.example.docknet.ui;

import com.example.docknet.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StarImageMapper {
    private static final Map<String, Integer> STAR_MAP;

    static {
        Map<String, Integer> temp = new HashMap<>();
        temp.put("o (blue-white) star".toLowerCase(Locale.ROOT), R.drawable.star_1);
        temp.put("b (blue-white) star".toLowerCase(Locale.ROOT), R.drawable.star_2);
        temp.put("b (blue-white super giant) star".toLowerCase(Locale.ROOT), R.drawable.star_2);
        temp.put("a (blue-white) star".toLowerCase(Locale.ROOT), R.drawable.star_3);
        temp.put("a (blue-white super giant) star".toLowerCase(Locale.ROOT), R.drawable.star_3);
        temp.put("f (white) star".toLowerCase(Locale.ROOT), R.drawable.star_4);
        temp.put("f (white super giant) star".toLowerCase(Locale.ROOT), R.drawable.star_4);
        temp.put("g (white-yellow) star".toLowerCase(Locale.ROOT), R.drawable.star_5);
        temp.put("g (white-yellow super giant) star".toLowerCase(Locale.ROOT), R.drawable.star_5);
        temp.put("k (yellow-orange) star".toLowerCase(Locale.ROOT), R.drawable.star_6);
        temp.put("k (yellow-orange giant) star".toLowerCase(Locale.ROOT), R.drawable.star_6);
        temp.put("m (red dwarf) star".toLowerCase(Locale.ROOT), R.drawable.star_7);
        temp.put("m (red giant) star".toLowerCase(Locale.ROOT), R.drawable.star_7);
        temp.put("m (red super giant) star".toLowerCase(Locale.ROOT), R.drawable.star_7);
        temp.put("l (brown dwarf) star".toLowerCase(Locale.ROOT), R.drawable.star_8);
        temp.put("t (brown dwarf) star".toLowerCase(Locale.ROOT), R.drawable.star_9);
        temp.put("y (brown dwarf) star".toLowerCase(Locale.ROOT), R.drawable.star_10);
        temp.put("t tauri star".toLowerCase(Locale.ROOT), R.drawable.star_11);
        temp.put("herbig ae/be star".toLowerCase(Locale.ROOT), R.drawable.star_12);
        temp.put("wolf-rayet n star".toLowerCase(Locale.ROOT), R.drawable.star_22);
        temp.put("wolf-rayet nc star".toLowerCase(Locale.ROOT), R.drawable.star_23);
        temp.put("wolf-rayet c star".toLowerCase(Locale.ROOT), R.drawable.star_24);
        temp.put("wolf-rayet o star".toLowerCase(Locale.ROOT), R.drawable.star_25);
        temp.put("c star".toLowerCase(Locale.ROOT), R.drawable.star_32);
        temp.put("cn star".toLowerCase(Locale.ROOT), R.drawable.star_33);
        temp.put("cj star".toLowerCase(Locale.ROOT), R.drawable.star_42);
        temp.put("ms-type star".toLowerCase(Locale.ROOT), R.drawable.star_42);
        temp.put("s-type star".toLowerCase(Locale.ROOT), R.drawable.star_42);
        temp.put("white dwarf (d) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (da) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (dab) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (daz) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (dav) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (db) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (dbz) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (dbv) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (do) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (dov) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (dq) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (dc) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("white dwarf (dcv) star".toLowerCase(Locale.ROOT), R.drawable.star_51);
        temp.put("neutron star".toLowerCase(Locale.ROOT), R.drawable.star_91);
        temp.put("black hole".toLowerCase(Locale.ROOT), R.drawable.star_92);
        temp.put("supermassive black hole".toLowerCase(Locale.ROOT), R.drawable.star_92);
        STAR_MAP = Collections.unmodifiableMap(temp);
    }

    public static Integer getResId(String starType) {
        if (starType == null || starType.isEmpty()) return null;
        return STAR_MAP.get(starType.trim().toLowerCase(Locale.ROOT));
    }

    public static List<Map.Entry<String, Integer>> getStarEntries() {
        return new ArrayList<>(STAR_MAP.entrySet());
    }
}
