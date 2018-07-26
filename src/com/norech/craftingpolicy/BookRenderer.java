package com.norech.craftingpolicy;

import org.bukkit.configuration.Configuration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookRenderer {

    @SuppressWarnings("unchecked")
    public static List<String> renderPages(Configuration config, List<String> bookPages) {
        String[] pagesData = new String[bookPages.size()];
        bookPages.toArray(pagesData);

        List<String> pages = new ArrayList<>();

        for (String pageData : pagesData) {
            pages.add(BookRenderer.renderPage(config, pageData).toJSONString());
        }

        return pages;
    }

    @SuppressWarnings("unchecked")
    public static JSONArray renderPage(Configuration config, String configData) {
        JSONArray json = new JSONArray();

        json.add("");

        JSONObject page = new JSONObject();
        page.put("text", configData);
        json.add(page);


        JSONObject lineJump = new JSONObject();
        lineJump.put("text", "\n\n");
        json.add(lineJump);


        JSONObject acceptButton = new JSONObject();
        acceptButton.put("text", "[" + config.getString("translations.acceptButton") + "]");
        acceptButton.put("bold", true);
        acceptButton.put("color", "dark_green");
        acceptButton.put("clickEvent", getClickEventJSON(
                "run_command",
                "/craftingpolicy:policy accept"
        ));
        json.add(acceptButton);

        JSONObject space = new JSONObject();
        space.put("text", " ");
        json.add(space);

        JSONObject denyButton = new JSONObject();
        denyButton.put("text", "[" + config.getString("translations.denyButton") + "]");
        denyButton.put("bold", true);
        denyButton.put("color", "dark_red");
        denyButton.put("clickEvent", getClickEventJSON(
                "run_command",
                "/craftingpolicy:policy deny"
        ));
        json.add(denyButton);

        return json;
    }

    @SuppressWarnings("unchecked")
    private static JSONObject getClickEventJSON(String action, String value) {
        JSONObject json = new JSONObject();

        json.put("action", action);
        json.put("value", value);

        return json;
    }
}
