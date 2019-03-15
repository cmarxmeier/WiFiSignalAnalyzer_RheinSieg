/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package net.rheinsieg.wifisignalanalyzer.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import net.rheinsieg.util.FileUtils;
import net.rheinsieg.wifisignalanalyzer.R;
import net.rheinsieg.wifisignalanalyzer.settings.Settings;
import net.rheinsieg.wifisignalanalyzer.location.LocationPrefsHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;


public class LocationFragment extends Fragment {
    private static final String YEAR_FORMAT = "yyyy";
    private static final String TAG = "LocationFragment";
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private TextView mAltitudeTextView;

    private Settings settings;
    public String latitude;
    public String longitude;
    public String altitude;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_content, container, false);

        setLocation(view);
        return view;
    }




    private void setLocation(View view) {
        // get latitude, longitude, altitude from prefs
        String longitude_key = "longitude";
        String latitude_key = "latitude";
        String altitude_key = "altitude";

        Context context = getActivity();
        String location=LocationPrefsHelper.getName();
        // log
        String preftxt = "Location-Values read from prefs:"+location;
        // Log.i(TAG,preftxt);

        // Split location into latitude,longitude,altitude
        StringTokenizer tokens = new StringTokenizer(location, "|");
        latitude = tokens.nextToken();
        longitude = tokens.nextToken();
        altitude = tokens.nextToken();
        String ctrl = "Splitted Location: 0->"+latitude+" 1->"+longitude+" 2->"+altitude;
        // Log.i(TAG,ctrl);

        // and set to view values
        setText(view, R.id.longitude_textview, longitude);
        setText(view, R.id.latitude_textview, latitude);
        setText(view, R.id.altitude_textview, altitude);

    }

    private void setText(View view, int id, String text) {
        TextView version = view.findViewById(id);
        if (version != null) {
            version.setText(text);
        }
    }


    private static class AlertDialogClickListener implements OnClickListener {
        private final Activity activity;
        private final int titleId;
        private final int resourceId;
        private final boolean isSmallFont;

        AlertDialogClickListener(@NonNull Activity activity, int titleId, int resourceId) {
            this(activity, titleId, resourceId, true);
        }

        AlertDialogClickListener(@NonNull Activity activity, int titleId, int resourceId, boolean isSmallFont) {
            this.activity = activity;
            this.titleId = titleId;
            this.resourceId = resourceId;
            this.isSmallFont = isSmallFont;
        }

        @Override
        public void onClick(View view) {
            if (!activity.isFinishing()) {
                String text = FileUtils.readFile(activity.getResources(), resourceId);
                AlertDialog alertDialog = new AlertDialog
                    .Builder(view.getContext())
                    .setTitle(titleId)
                    .setMessage(text)
                    .setNeutralButton(android.R.string.ok, new Close())
                    .create();
                alertDialog.show();
                if (isSmallFont) {
                    TextView textView = alertDialog.findViewById(android.R.id.message);
                    textView.setTextSize(8);
                }
            }
        }

        private static class Close implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }
    }

}
