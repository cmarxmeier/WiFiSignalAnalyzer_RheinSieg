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

package net.rheinsieg.wifisignalanalyzer.navigation.items;

import android.view.View;

import net.rheinsieg.wifisignalanalyzer.about.AboutFragment;
import net.rheinsieg.wifisignalanalyzer.settings.SettingsFragment;
import net.rheinsieg.wifisignalanalyzer.vendor.VendorFragment;
import net.rheinsieg.wifisignalanalyzer.wifi.accesspoint.AccessPointsFragment;
import net.rheinsieg.wifisignalanalyzer.wifi.channelavailable.ChannelAvailableFragment;
import net.rheinsieg.wifisignalanalyzer.wifi.channelgraph.ChannelGraphFragment;
import net.rheinsieg.wifisignalanalyzer.wifi.channelrating.ChannelRatingFragment;
import net.rheinsieg.wifisignalanalyzer.wifi.timegraph.TimeGraphFragment;

public class NavigationItemFactory {
    public static final NavigationItem ACCESS_POINTS = new FragmentItem(new AccessPointsFragment());
    public static final NavigationItem CHANNEL_RATING = new FragmentItem(new ChannelRatingFragment());
    public static final NavigationItem CHANNEL_GRAPH = new FragmentItem(new ChannelGraphFragment());
    public static final NavigationItem TIME_GRAPH = new FragmentItem(new TimeGraphFragment());
    public static final NavigationItem EXPORT = new ExportItem();
    public static final NavigationItem CHANNEL_AVAILABLE = new FragmentItem(new ChannelAvailableFragment(), false);
    public static final NavigationItem VENDORS = new FragmentItem(new VendorFragment(), false, View.GONE);
    public static final NavigationItem SETTINGS = new FragmentItem(new SettingsFragment(), false, View.GONE);
    public static final NavigationItem ABOUT = new FragmentItem(new AboutFragment(), false, View.GONE);

    private NavigationItemFactory() {
        throw new IllegalStateException("Factory class");
    }
}
