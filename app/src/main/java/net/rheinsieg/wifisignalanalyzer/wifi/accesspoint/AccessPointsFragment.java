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

package net.rheinsieg.wifisignalanalyzer.wifi.accesspoint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import net.rheinsieg.wifisignalanalyzer.MainContext;
import net.rheinsieg.wifisignalanalyzer.R;

public class AccessPointsFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private AccessPointsAdapter accessPointsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.access_points_content, container, false);

        swipeRefreshLayout = view.findViewById(R.id.accessPointsRefresh);
        swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        accessPointsAdapter = new AccessPointsAdapter(getActivity());
        ExpandableListView expandableListView = view.findViewById(R.id.accessPointsView);
        expandableListView.setAdapter(accessPointsAdapter);
        accessPointsAdapter.setExpandableListView(expandableListView);

        MainContext.INSTANCE.getScannerService().register(accessPointsAdapter);

        return view;
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        MainContext.INSTANCE.getScannerService().update();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onDestroy() {
        MainContext.INSTANCE.getScannerService().unregister(accessPointsAdapter);
        super.onDestroy();
    }

    AccessPointsAdapter getAccessPointsAdapter() {
        return accessPointsAdapter;
    }

    private class ListViewOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }
}
