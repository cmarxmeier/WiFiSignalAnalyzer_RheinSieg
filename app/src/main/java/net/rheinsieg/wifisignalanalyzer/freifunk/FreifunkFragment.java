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

package net.rheinsieg.wifisignalanalyzer.freifunk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import net.rheinsieg.util.TextUtils;
import net.rheinsieg.wifisignalanalyzer.MainContext;
import net.rheinsieg.wifisignalanalyzer.R;

public class FreifunkFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.freifunk_content, container, false);
        FreifunkAdapter freifunkAdapter = new FreifunkAdapter(getActivity(), MainContext.INSTANCE.getFreifunkService());
        setListAdapter(freifunkAdapter);

        SearchView searchView = view.findViewById(R.id.freifunkSearchText);
        searchView.setOnQueryTextListener(new Listener(freifunkAdapter));

        return view;
    }

    static class Listener implements OnQueryTextListener {
        private final FreifunkAdapter freifunkAdapter;

        Listener(@NonNull FreifunkAdapter freifunkAdapter) {
            this.freifunkAdapter = freifunkAdapter;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            freifunkAdapter.update(TextUtils.trim(newText));
            return true;
        }
    }

}
