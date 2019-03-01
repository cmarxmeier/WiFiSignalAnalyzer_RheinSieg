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

package net.rheinsieg.wifisignalanalyzer.wifi.filter.adapter;

import android.support.annotation.NonNull;

import net.rheinsieg.wifisignalanalyzer.R;
import net.rheinsieg.wifisignalanalyzer.settings.Settings;
import net.rheinsieg.wifisignalanalyzer.wifi.band.WiFiBand;

import java.util.Set;

public class WiFiBandAdapter extends EnumFilterAdapter<WiFiBand> {

    WiFiBandAdapter(@NonNull Set<WiFiBand> values) {
        super(WiFiBand.class, values);
    }

    @Override
    public int getColor(@NonNull WiFiBand object) {
        return contains(object) ? R.color.connected : R.color.icons_color;
    }

    @Override
    public void save(@NonNull Settings settings) {
        settings.saveWiFiBands(getValues());
    }
}
