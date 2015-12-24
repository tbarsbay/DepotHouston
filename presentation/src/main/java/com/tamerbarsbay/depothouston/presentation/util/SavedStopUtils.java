package com.tamerbarsbay.depothouston.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamerbarsbay.depothouston.presentation.model.SavedStopGroupModel;
import com.tamerbarsbay.depothouston.presentation.model.SavedStopModel;

import java.util.ArrayList;

public class SavedStopUtils {

    private static final String LOG_TAG = "SavedStopUtils";

    private static final String PREFS_SAVED_STOPS = "prefs_saved_stops";

    public static void createGroup(@NonNull Context context,
                                   @NonNull String groupName) {
        ArrayList<SavedStopGroupModel> savedGroups = getSavedStopGroups(context);
        SavedStopGroupModel newGroup = new SavedStopGroupModel(savedGroups.size(), groupName, new ArrayList<SavedStopModel>());
        savedGroups.add(newGroup);
        saveGroupsToSharedPreferences(context, savedGroups);
    }

    public static boolean doesGroupExist(@NonNull Context context,
                                         @NonNull String groupName) {
        ArrayList<SavedStopGroupModel> groups = getSavedStopGroups(context);
        for (SavedStopGroupModel group : groups) {
            if (groupName.equals(group.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStopSaved(@NonNull Context context,
                                      @NonNull String stopId) {
        ArrayList<SavedStopGroupModel> groups = getSavedStopGroups(context);
        for (SavedStopGroupModel group : groups) {
            if (group.containsSavedStop(stopId)) {
                return true;
            }
        }
        return false;
    }

    public static void saveStopToGroup(@NonNull Context context,
                                          @NonNull String groupName,
                                          @NonNull SavedStopModel stop) {
        if (stop.getName() == null || stop.getStopId() == null) {
            throw new IllegalArgumentException("Stop cannot have null name or id");
        }

        // If the group exists, add this stop to it
        ArrayList<SavedStopGroupModel> savedGroups = getSavedStopGroups(context);
        for (SavedStopGroupModel group : savedGroups) {
            if (group.getName().equals(groupName)) {
                // Found the group, see if it already contains this stop
                if (group.containsSavedStop(stop.getStopId())) {
                    return;
                }

                stop.setObjectId(group.generateNewChildId());
                group.addStop(stop);

                // Serialize the groups back to string
                saveGroupsToSharedPreferences(context, savedGroups);
                return;
            }
        }

        // If the group doesn't already exist, create it
        SavedStopGroupModel newGroup = new SavedStopGroupModel(savedGroups.size(), groupName, new ArrayList<SavedStopModel>());
        newGroup.addStop(stop);
        savedGroups.add(newGroup);
        saveGroupsToSharedPreferences(context, savedGroups);
    }

    public static void removeGroup(Context context, int groupPosition) {
        ArrayList<SavedStopGroupModel> groups = getSavedStopGroups(context);
        if (groupPosition < 0 || groupPosition >= groups.size()) return;
        groups.remove(groupPosition);
        saveGroupsToSharedPreferences(context, groups);
    }

    public static void removeStop(Context context, int groupPosition, int stopPosition) {
        ArrayList<SavedStopGroupModel> groups = getSavedStopGroups(context);
        if (groupPosition < 0 || groupPosition >= groups.size()) return;
        SavedStopGroupModel group = groups.get(groupPosition);
        if (stopPosition < 0 || stopPosition >= group.getStops().size()) return;
        group.getStops().remove(stopPosition);
        saveGroupsToSharedPreferences(context, groups);
    }

    private static void saveGroupsToSharedPreferences(@NonNull Context context, @NonNull ArrayList<SavedStopGroupModel> savedGroups) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String savedStopsString = new Gson().toJson(savedGroups);
        if (savedStopsString != null) {
            sp.edit().putString(PREFS_SAVED_STOPS, savedStopsString).apply();
        }
    }

    public static ArrayList<SavedStopGroupModel> getSavedStopGroups(@NonNull Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String savedGroupsString = sp.getString(PREFS_SAVED_STOPS, null);
        if (savedGroupsString != null) {
            ArrayList<SavedStopGroupModel> savedGroups =
                    new Gson().fromJson(
                            savedGroupsString,
                            new TypeToken<ArrayList<SavedStopGroupModel>>(){}.getType());
            if (savedGroups != null) {
                return savedGroups;
            }
        }
        return new ArrayList<SavedStopGroupModel>();
    }

    public static ArrayList<String> getGroupNamesArray(@NonNull Context context) {
        ArrayList<String> groupNames = new ArrayList<String>();
        ArrayList<SavedStopGroupModel> savedGroups = getSavedStopGroups(context);
        for (SavedStopGroupModel savedGroup : savedGroups) {
            groupNames.add(savedGroup.getName());
        }
        return groupNames;
    }

    public static SavedStopModel getSavedStop(@NonNull Context context, int groupRank, int childRank) {
        SavedStopGroupModel group = getSavedStopGroups(context).get(groupRank);
        if (group != null && group.getStops() != null) {
            try {
                return group.getStops().get(childRank);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int getGroupCount(@NonNull Context context) {
        ArrayList<SavedStopGroupModel> savedGroups = getSavedStopGroups(context);
        return savedGroups != null ? savedGroups.size() : 0;
    }

    public static int getChildCount(@NonNull Context context, int groupRank) {
        SavedStopGroupModel savedGroup = getSavedStopGroups(context).get(groupRank);
        if (savedGroup != null && savedGroup.getStops() != null) {
            return savedGroup.getStops().size();
        }
        return 0;
    }

    public static int getGroupId(@NonNull Context context, int groupRank) {
        SavedStopGroupModel group = getSavedStopGroups(context).get(groupRank);
        return group == null ? 0 : group.getId();
    }

    public static int getChildId(@NonNull Context context, int groupRank, int childPosition) {
        SavedStopModel stop = getSavedStop(context, groupRank, childPosition);
        return stop == null ? 0 : stop.getObjectId();
    }

    public static void moveSavedGroup(@NonNull Context context, int fromGroupRank, int toGroupRank) {
        Log.d("SavedStopUtils", "moveSavedGroup: from " + fromGroupRank + " to " + toGroupRank); //TODO temp

        ArrayList<SavedStopGroupModel> savedGroups = getSavedStopGroups(context);
        SavedStopGroupModel group = savedGroups.get(fromGroupRank);
        if (group == null || !savedGroups.remove(group)) {
            // Something went wrong //TODO handle with a custom exception?
            Log.d(LOG_TAG, "moveSavedGroup: error");
            return;
        }

        // If we reach here, a group was found and removed from the list of groups.
        // Now re-add it in the new position.
        savedGroups.add(toGroupRank, group); //TODO try this but it may have to be minus one or something
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
            ArrayList<SavedStopGroupModel> savedGroups = getSavedStopGroups(context);
            SavedStopGroupModel fromGroup = savedGroups.get(fromGroupRank);
            SavedStopGroupModel toGroup = savedGroups.get(toGroupRank);
            if (fromGroup == null || toGroup == null) {
                // Error //TODO handle
                Log.d(LOG_TAG, "moveSavedStop: error");
                return;
            }
            SavedStopModel stop = fromGroup.getStops().get(fromChildRank);
            if (stop != null && fromGroup.getStops().remove(stop)) {
                // Successfully removed stop from old group, now add to new group
                toGroup.getStops().add(toChildRank, stop); //TODO error handle out of bounds
                //TODO change to just group interface method?

                Log.d("SavedStopUtils", "Successfully moving stop"); //TODO temp
                saveGroupsToSharedPreferences(context, savedGroups);
            }
        } else {
            // Moving the stop within the same group
            ArrayList<SavedStopGroupModel> savedGroups = getSavedStopGroups(context);
            SavedStopGroupModel group = savedGroups.get(fromGroupRank);
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
