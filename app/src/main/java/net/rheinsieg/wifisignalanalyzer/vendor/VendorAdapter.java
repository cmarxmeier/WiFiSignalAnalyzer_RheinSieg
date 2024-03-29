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

package net.rheinsieg.wifisignalanalyzer.vendor;

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
import net.rheinsieg.wifisignalanalyzer.vendor.model.VendorService;

class VendorAdapter extends ArrayAdapter<String> {
    private final VendorService vendorService;

    VendorAdapter(@NonNull Context context, @NonNull VendorService vendorService) {
        super(context, R.layout.vendor_details, vendorService.findVendors());
        this.vendorService = vendorService;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = MainContext.INSTANCE.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.vendor_details, parent, false);
        }
        String vendorName = getItem(position);
        view.<TextView>findViewById(R.id.vendor_name)
            .setText(vendorName);
        view.<TextView>findViewById(R.id.vendor_macs)
            .setText(TextUtils.join(", ", vendorService.findMacAddresses(vendorName)));
        return view;
    }

    void update(@NonNull String filter) {
        clear();
        addAll(vendorService.findVendors(filter));
    }

}
