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

package net.rheinsieg.wifisignalanalyzer.wifi.timegraph;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.rheinsieg.wifisignalanalyzer.MainContext;
import net.rheinsieg.wifisignalanalyzer.R;
import net.rheinsieg.wifisignalanalyzer.wifi.graphutils.GraphViewAdd;

import org.apache.commons.collections4.IterableUtils;

public class TimeGraphFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private TimeGraphAdapter timeGraphAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_content, container, false);

        swipeRefreshLayout = view.findViewById(R.id.graphRefresh);
        swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        timeGraphAdapter = new TimeGraphAdapter();
        addGraphViews(swipeRefreshLayout, timeGraphAdapter);

        MainContext.INSTANCE.getScannerService().register(timeGraphAdapter);

        return view;
    }

    private void addGraphViews(View view, TimeGraphAdapter timeGraphAdapter) {
        IterableUtils.forEach(timeGraphAdapter.getGraphViews(),
            new GraphViewAdd(view.findViewById(R.id.graphFlipper)));
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
        MainContext.INSTANCE.getScannerService().unregister(timeGraphAdapter);
        super.onDestroy();
    }

    TimeGraphAdapter getTimeGraphAdapter() {
        return timeGraphAdapter;
    }

    private class ListViewOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }

}
