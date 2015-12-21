package com.tamerbarsbay.depothouston.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamerbarsbay.depothouston.presentation.model.SavedGroupModel;
import com.tamerbarsbay.depothouston.presentation.model.SavedStopModel;

import java.util.ArrayList;

public class SavedStopUtils {

    private static final String PREFS_SAVED_STOPS = "prefs_saved_stops";

    public static void saveStopToGroup(@NonNull Context context,
                                          @NonNull String groupName,
                                          @NonNull SavedStopModel stop) {
        if (stop.getName() == null || stop.getId() == null) {
            throw new IllegalArgumentException("Stop cannot have null name or id");
        }

        Log.d("SavedStopUtils", "saveStopToGroup"); //TODO temp
        //TODO check if stop is already in group

        ArrayList<SavedGroupModel> savedGroups = getSavedStopGroups(context);
        for (SavedGroupModel group : savedGroups) {
            if (group.getName().equals(groupName)) {
                Log.d("SavedStopUtils", "saveStopToGroup: found group"); //TODO temp
                // Found the group, add the stop here
                stop.setRank(group.getNumStops());
                group.addStop(stop);

                // Serialize the groups back to string
                saveGroupsToSharedPreferences(context, savedGroups);
                return;
            }
        }

        // The group was not found, so create it!
        SavedGroupModel newGroup = new SavedGroupModel(groupName, savedGroups.size(), new ArrayList<SavedStopModel>());
        Log.d("SavedStopUtils", "saveStopToGroup: creating new group"); //TODO temp
        newGroup.addStop(stop);
        savedGroups.add(newGroup);
        saveGroupsToSharedPreferences(context, savedGroups);
    }

    private static void saveGroupsToSharedPreferences(@NonNull Context context, @NonNull ArrayList<SavedGroupModel> savedGroups) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String savedStopsString = new Gson().toJson(savedGroups);
        if (savedStopsString != null) {
            Log.d("SavedStopUtils", "saveGroupsToPrefs savedStopString not null"); //TODO temp
            sp.edit().putString(PREFS_SAVED_STOPS, savedStopsString).apply();
        } else {
            Log.d("SavedStopUtils", "saveGroupsToPrefs savedStopString null"); //TODO temp
        }
        //TODO handle if string is null?
    }

    public static ArrayList<SavedGroupModel> getSavedStopGroups(@NonNull Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String savedGroupsString = sp.getString(PREFS_SAVED_STOPS, null);
        if (savedGroupsString != null) {
            ArrayList<SavedGroupModel> savedGroups =
                    new Gson().fromJson(
                            savedGroupsString,
                            new TypeToken<ArrayList<SavedGroupModel>>(){}.getType());
            if (savedGroups != null) {
                return savedGroups;
            }
        }
        return new ArrayList<SavedGroupModel>();
    }

    public static SavedGroupModel getGroupByName(@NonNull Context context,
                                                 @NonNull String name) {
        return getGroupByName(getSavedStopGroups(context), name);
    }

    /**
     * Returns a {@link SavedGroupModel} from a given list of groups that matches a certain name, or null
     * if one could not be found.
     * @param groups
     * @param name
     * @return The group if one was found, or null if one wasn't
     */
    public static SavedGroupModel getGroupByName(@NonNull ArrayList<SavedGroupModel> groups,
                                                 @NonNull String name) {
        for (SavedGroupModel savedGroup : groups) {
            if (name.equals(savedGroup.getName())) {
                return savedGroup;
            }
        }
        return null;
    }

    public static SavedGroupModel getGroupByRank(@NonNull Context context, int rank) {
        return getGroupByRank(getSavedStopGroups(context), rank);
    }

    public static SavedGroupModel getGroupByRank(@NonNull ArrayList<SavedGroupModel> groups,
                                                 int rank) {
        for (SavedGroupModel savedGroup : groups) {
            if (savedGroup.getRank() == rank) {
                return savedGroup;
            }
        }
        return null;
    }

    public static SavedStopModel getSavedStop(@NonNull Context context, int groupRank, int childRank) {
        SavedGroupModel group = getGroupByRank(context, groupRank);
        if (group != null && group.getStops() != null) {
            for (SavedStopModel stop : group.getStops()) {
                if (stop.getRank() == childRank) {
                    return stop;
                }
            }
        }
        return null;
    }

    public static int getGroupCount(@NonNull Context context) {
        ArrayList<SavedGroupModel> savedGroups = getSavedStopGroups(context);
        return savedGroups != null ? savedGroups.size() : 0;
    }

    public static int getChildCount(@NonNull Context context, int groupRank) {
        SavedGroupModel savedGroup = getGroupByRank(context, groupRank);
        if (savedGroup != null && savedGroup.getStops() != null) {
            return savedGroup.getStops().size();
        }
        return 0;
    }

    public static int getGroupId(int groupRank) {
        return groupRank; //TODO TEST
    }

    public static int getChildId(int groupRank, int childPosition) {
        return 0; //TODO example has generateChildId which increments children
    }

    public static void moveSavedGroup(@NonNull Context context, int fromGroupRank, int toGroupRank) {
        Log.d("SavedStopUtils", "moveSavedGroup: from " + fromGroupRank + " to " + toGroupRank); //TODO temp

        ArrayList<SavedGroupModel> savedGroups = getSavedStopGroups(context);
        SavedGroupModel group = getGroupByRank(savedGroups, fromGroupRank);
        if (group == null || !savedGroups.remove(group)) {
            // Something went wrong //TODO handle with a custom exception?
            return;
        }

        // If we reach here, a group was found and removed from the list of groups.
        // Now re-add it in the new position.
        savedGroups.add(toGroupRank, group); //TODO try this but it may have to be minus one or something
        //TODO update group rank? i think its not needed
        //TODO catch indexoutofbounds exception?? handle with custom exception?
        Log.d("SavedStopUtils", "Successfully moving group"); //TODO temp
        saveGroupsToSharedPreferences(context, savedGroups);
    }

    public static void moveSavedStop(@NonNull Context context, int fromGroupRank,
                                     int fromChildRank, int toGroupRank,
                                     int toChildRank) {
        Log.d("SavedStopUtils", "moveSavedStop: from " + fromGroupRank + "," + fromChildRank + " to " + toGroupRank + "," + toChildRank); //TODO temp
        if (fromGroupRank == toGroupRank && fromChildRank == toChildRank) {
            return;
        }

        if (fromGroupRank != toGroupRank) {
            // Moving the stop to a different group, simple remove and add
            ArrayList<SavedGroupModel> savedGroups = getSavedStopGroups(context);
            SavedGroupModel fromGroup = savedGroups.get(fromGroupRank);
            SavedGroupModel toGroup = savedGroups.get(toGroupRank);
            if (fromGroup == null || toGroup == null) {
                // Error //TODO handle
                return;
            }
            SavedStopModel stop = fromGroup.getStops().get(fromChildRank); //TODO fix
            if (stop != null && fromGroup.getStops().remove(stop)) {
                // Successfully removed stop from old group, now add to new group
                toGroup.getStops().add(toChildRank, stop); //TODO error handle out of bounds
                //TODO change to just group interface method?

                Log.d("SavedStopUtils", "Successfully moving stop"); //TODO temp
                saveGroupsToSharedPreferences(context, savedGroups);
            }
        } else {
            // Moving the stop within the same group
            ArrayList<SavedGroupModel> savedGroups = getSavedStopGroups(context);
            SavedGroupModel group = savedGroups.get(fromGroupRank);
            SavedStopModel stop = group.getStops().get(fromChildRank);
            if (stop != null && group.getStops().remove(stop)) {
                // Stop successfully removed, add it to the new position
                group.getStops().add(toChildRank, stop); //TODO fix, might need minus one?
                //TODO check for out of bounds
                Log.d("SavedStopUtils", "Successfully moving stop"); //TODO temp
                saveGroupsToSharedPreferences(context, savedGroups);
            }
        }
    }

}
