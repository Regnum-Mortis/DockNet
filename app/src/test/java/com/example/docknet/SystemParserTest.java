package com.example.docknet;

import com.example.docknet.data.SystemParser;
import com.example.docknet.model.SystemInfo;

import org.json.JSONObject;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SystemParserTest {

    @Test
    public void parseSystemList_basic() throws Exception {
        String json = "[ { \"name\": \"Sol\" }, { \"name\": \"Alpha Centauri\" } ]";
        List<String> list = SystemParser.parseSystemListFromJson(json);
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("Sol", list.get(0));
        assertEquals("Alpha Centauri", list.get(1));
    }

    @Test
    public void parseSystemInfo_and_infoFormater_distance() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("name", "TestSystem");
        JSONObject primary = new JSONObject();
        primary.put("type", "G (white-yellow) star");
        primary.put("name", "TestStar");
        primary.put("isScoopable", true);
        obj.put("primaryStar", primary);
        JSONObject coords = new JSONObject();
        coords.put("x", 10);
        coords.put("y", 0);
        coords.put("z", 0);
        obj.put("coords", coords);
        JSONObject info = new JSONObject();
        info.put("allegiance", "Federation");
        obj.put("information", info);
        obj.put("coordsLocked", false);
        obj.put("population", 12345);

        SystemInfo s = SystemParser.parseSystemInfoFromJson(obj);
        assertNotNull(s);
        assertEquals("TestSystem", s.name);
        assertEquals("G (white-yellow) star", s.primaryStarType);
        Map<String, String> form = SystemParser.infoFormatter(s);
        assertNotNull(form);
        String distStr = form.get("distanceToSol");
        assertNotNull(distStr);
        assertTrue(Double.parseDouble(distStr) > 0.0);

        String formatted = SystemParser.formatSystemInfo(s);
        assertTrue(formatted.contains("TestSystem"));
        assertTrue(formatted.contains("Coords:"));
    }
}
