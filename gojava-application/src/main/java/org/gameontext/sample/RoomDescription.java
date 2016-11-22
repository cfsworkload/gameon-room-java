package org.gameontext.sample;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.gameontext.sample.map.client.MapData;

/**
 * This is how our room is described.
 *  a) Use post-construct to go fill some of this in by asking the map
 *  b) Assign this dynamically on the fly as the room is used.
 */
public class RoomDescription {

    private final JsonObject EMPTY_COMMANDS = Json.createObjectBuilder().build();
    private final JsonArray EMPTY_INVENTORY = Json.createArrayBuilder().build();

    private String name = "nickName";
    private String fullName = "A room with no full name";
    private String description = "An undescribed room (or perhaps the data hasn't been fetched from the map)";

    private Map<String, String> commands = new HashMap<>();
    private JsonObject commandObj = null;

    private Set<String> items = new HashSet<>();
    private JsonArray itemObj = null;

    public RoomDescription() {}

    /**
     * Create a new room description based on data retrieved from the Map service
     * @param data Map data
     */
    public RoomDescription(MapData data) {
        updateData(data);
    }

    /**
     * Update the room description based on data retrieved from the Map service
     * @param data Map data
     */
    public void updateData(MapData data) {
        if ( data.getName() != null ) {
            this.name = data.getName();
        }

        if ( data.getFullName() != null ) {
            this.fullName = data.getFullName();
        }

        if ( data.getDescription() != null ) {
            this.description = data.getDescription();
        }
    }

    /**
     * @return The room's short name
     */
    public String getName() {
        return name;
    }

    /**
     * @return The room's long name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * The display name for a room can change at any time.
     * @param fullName A new display name for the room
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Custom commands are optional. Build/cache/return a JsonObject listing
     * commands and a description of what they do for use in location messages
     * @return JsonObject containing custom room commands. Never null.
     */
    public JsonObject getCommands() {
        JsonObject obj = commandObj;

        if ( commands.isEmpty()) {
            return EMPTY_COMMANDS;
        } else if ( obj == null) {
            JsonObjectBuilder newCommandObj = Json.createObjectBuilder();
            commands.entrySet().forEach(e -> { newCommandObj.add(e.getKey(), e.getValue()); });
            obj = commandObj = newCommandObj.build();
        }

        return obj;
    }

    public void addCommand(String command, String description) {
        if ( description == null ) {
            throw new IllegalArgumentException("description is required");
        }
        commands.put(command, description);
        commandObj = null;
    }

    public void removeCommand(String command) {
        commands.remove(command);
        commandObj = null;
    }

    /**
     * Room inventory objects are optional. Build/cache/return a JsonArray listing
     * items in the room for use in location messages.
     * @return JsonArray containing room inventory. Never null
     */
    public JsonArray getInventory() {
        JsonArray arr = itemObj;

        if ( items.isEmpty()) {
            return EMPTY_INVENTORY;
        } else if ( arr == null) {
            JsonArrayBuilder newItemArr = Json.createArrayBuilder();
            items.forEach(s -> { newItemArr.add(s); });
            arr = itemObj = newItemArr.build();
        }

        return arr;
    }

    public void addItem(String itemName) {
        items.add(itemName);
        itemObj = null;
    }

    public void removeItem(String itemName) {
        items.remove(itemName);
        itemObj = null;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("name=").append(name);
        s.append(", fullName=").append(fullName);
        s.append(", description=").append(description);
        s.append(", commands=").append(commands);
        s.append(", items=").append(items);

        return s.toString();
    }
}