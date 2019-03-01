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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.rheinsieg.wifisignalanalyzer.MainContext;
import net.rheinsieg.wifisignalanalyzer.R;
import net.rheinsieg.wifisignalanalyzer.freifunk.model.FreifunkService;

class FreifunkAdapter extends ArrayAdapter<String> {
    private final FreifunkService freifunkService;

    FreifunkAdapter(@NonNull Context context, @NonNull FreifunkService freifunkService) {
        super(context, R.layout.freifunk_details, freifunkService.findFreifunk());
        this.freifunkService = freifunkService;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = MainContext.INSTANCE.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.freifunk_details, parent, false);
        }
        String freifunkName = getItem(position);
        view.<TextView>findViewById(R.id.freifunk_name)
            .setText(freifunkName);
        view.<TextView>findViewById(R.id.freifunk_macs)
            .setText(TextUtils.join(", ", freifunkService.findMacAddresses(freifunkName)));
        return view;
    }

    void update(@NonNull String filter) {
        clear();
        addAll(freifunkService.findFreifunk(filter));
    }

}
