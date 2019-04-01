package com.cyprias.chunkspawnerlimiter.debug;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sarhatabaot
 */
public class Debug {
    public static String HashMap(HashMap<String, ArrayList<Entity>> map){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,ArrayList<Entity>> entry: map.entrySet()){
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(ArrayList(entry.getValue()));
            sb.append("\n");
        }
        return StringUtils.removeEnd(sb.toString(),"\n");
    }

    public static String HashMapKeys(HashMap<String, ArrayList<Entity>> map){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,ArrayList<Entity>> entry: map.entrySet()){
            sb.append(entry.getKey());
            sb.append(",");
        }
        return StringUtils.removeEnd(sb.toString(),",");
    }

    private static String ArrayList(ArrayList<Entity> list){
        StringBuilder sb = new StringBuilder();
        for(Entity entity : list){
            sb.append(entity.getName());
            sb.append(", ");
        }
        return StringUtils.removeEnd(sb.toString(),", ");
    }

    public static String Entities(Entity[] entities){
        StringBuilder sb = new StringBuilder();
        for (Entity entity: entities){
            sb.append(entity.getType().name());
            sb.append(", ");
        }
        return StringUtils.removeEnd(sb.toString(),", ");
    }
}
