package com.tamerbarsbay.depothouston.presentation.util;

import android.content.Context;
import android.content.pm.PackageManager;

import com.tamerbarsbay.depothouston.presentation.model.ChangelogItemModel;

import java.util.ArrayList;

public class SettingsUtils {

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static ArrayList<ChangelogItemModel> getChangelogItems() {
        ArrayList<ChangelogItemModel> changes = new ArrayList<ChangelogItemModel>();
        changes.add(new ChangelogItemModel("1.0", "January 8, 2016", "- Initial launch!"));
        return changes;
    }

}
