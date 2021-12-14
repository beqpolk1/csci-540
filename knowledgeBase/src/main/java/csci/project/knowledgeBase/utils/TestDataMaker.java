package csci.project.knowledgeBase.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class TestDataMaker {
    public static List<HashMap<String, Object>> getCondData() {
        List<HashMap<String, Object>> ret = new ArrayList<>();

        HashMap<String, Object> newVals = new HashMap<>();
        newVals.put("hi_temp", 69);
        newVals.put("lo_temp", 42);
        newVals.put("avg_temp", 55);
        newVals.put("precip", false);
        newVals.put("precip_type", "none");
        newVals.put("cloud_cover", 40.2);
        newVals.put("wind_avg", 7);
        newVals.put("wind_gust", 13);
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("hi_temp", 37);
        newVals.put("lo_temp", 15);
        newVals.put("avg_temp", 22);
        newVals.put("precip", true);
        newVals.put("precip_type", "snow");
        newVals.put("cloud_cover", 89.0);
        newVals.put("wind_avg", 10);
        newVals.put("wind_gust", 20);
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("hi_temp", 87);
        newVals.put("lo_temp", 58);
        newVals.put("avg_temp", 73);
        newVals.put("precip", false);
        newVals.put("precip_type", "none");
        newVals.put("cloud_cover", 2.7);
        newVals.put("wind_avg", 5);
        newVals.put("wind_gust", 8);
        ret.add(newVals);

        return ret;
    }

    public static List<HashMap<String, Object>> getGearData(IdGen idGen) {
        List<HashMap<String, Object>> ret = new ArrayList<>();

        HashMap<String, Object> newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "clothing");
        newVals.put("par_type", null);
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "jacket");
        newVals.put("par_type", "clothing");
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "pants");
        newVals.put("par_type", "clothing");
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", "{\"precip\":true,\"precip_type\":[\"rain\", \"snow\", \"mix\"]}");
        newVals.put("type", "rain_jacket");
        newVals.put("par_type", "jacket");
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", "{\"precip\":true,\"precip_type\":[\"rain\", \"mix\"],\"hi_temp\":[90,50],\"lo_temp\":[60,35],\"avg_temp\":[80,40],\"wind_avg\":[20,0],\"wind_gust\":[40,0],\"cloud_cover\":[100,0]}");
        newVals.put("type", "rain_jacket");
        newVals.put("par_type", "jacket");
        newVals.put("name", "Marmot Precip");
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", "{\"precip\":true,\"precip_type\":[\"rain\", \"snow\", \"mix\"],\"hi_temp\":[80,25],\"lo_temp\":[60,15],\"avg_temp\":[75,20],\"wind_avg\":[30,0],\"wind_gust\":[50,0],\"cloud_cover\":[100,0]}");
        newVals.put("type", "rain_jacket");
        newVals.put("par_type", "jacket");
        newVals.put("name", "OR Foray");
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "insulated_jacket");
        newVals.put("par_type", "jacket");
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", "{\"precip\":false,\"precip_type\":[\"none\"],\"hi_temp\":[65,35],\"lo_temp\":[50,20],\"avg_temp\":[60,30],\"wind_avg\":[20,0],\"wind_gust\":[30,0],\"cloud_cover\":[100,0]}");
        newVals.put("type", "insulated_jacket");
        newVals.put("par_type", "jacket");
        newVals.put("name", "NF Thermoball");
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "lighting");
        newVals.put("par_type", null);
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "headlamp");
        newVals.put("par_type", "lighting");
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "headlamp");
        newVals.put("par_type", "lighting");
        newVals.put("name", "BD Spot");
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "backpack");
        newVals.put("par_type", null);
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "backpack_sm");
        newVals.put("par_type", "backpack");
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "backpack_med");
        newVals.put("par_type", "backpack");
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "backpack_sm");
        newVals.put("par_type", "backpack");
        newVals.put("name", "Osprey Daylite");
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "backpack_med");
        newVals.put("par_type", "backpack");
        newVals.put("name", "Osprey Kestral");
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "pole");
        newVals.put("par_type", null);
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "trek_pole");
        newVals.put("par_type", "pole");
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("conditions", null);
        newVals.put("type", "trek_pole");
        newVals.put("par_type", "pole");
        newVals.put("name", "BD Trail");
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        return ret;
    }

    public static List<HashMap<String, Object>> getActivityData(IdGen idGen) {
        List<HashMap<String, Object>> ret = new ArrayList<>();

        HashMap<String, Object> newVals = new HashMap<>();
        newVals.put("type", "outdoors");
        newVals.put("gear", new String[] { "first_aid_kit", "pocket_knife", "lighting", "clothing" });
        newVals.put("par_type", null);
        newVals.put("duration", null);
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("type", "hiking");
        newVals.put("gear", new String[] { "map", "backpack" });
        newVals.put("par_type", "outdoors");
        newVals.put("duration", null);
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("type", "hike_short");
        newVals.put("gear", new String[] { "backpack_sm" });
        newVals.put("par_type", "hiking");
        newVals.put("duration", 3);
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("type", "hike_short");
        newVals.put("gear", new String[] { "backpack_sm" });
        newVals.put("par_type", "hiking");
        newVals.put("duration", 2);
        newVals.put("name", "College M");
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("type", "hike_xlong");
        newVals.put("gear", new String[] { "headlamp", "backpack_med" });
        newVals.put("par_type", "hiking");
        newVals.put("duration", 12);
        newVals.put("name", null);
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        newVals = new HashMap<>();
        newVals.put("type", "hike_xlong");
        newVals.put("gear", new String[] { "trek_pole" });
        newVals.put("par_type", "hiking");
        newVals.put("duration", 12);
        newVals.put("name", "Bridger Ridge");
        newVals.put("id", idGen.getId());
        ret.add(newVals);

        return ret;
    }
}
