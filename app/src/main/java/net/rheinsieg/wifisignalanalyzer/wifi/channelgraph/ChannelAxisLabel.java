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

package net.rheinsieg.wifisignalanalyzer.wifi.channelgraph;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import net.rheinsieg.wifisignalanalyzer.MainContext;
import net.rheinsieg.wifisignalanalyzer.settings.Settings;
import net.rheinsieg.wifisignalanalyzer.wifi.band.WiFiBand;
import net.rheinsieg.wifisignalanalyzer.wifi.band.WiFiChannel;
import net.rheinsieg.wifisignalanalyzer.wifi.band.WiFiChannels;
import net.rheinsieg.wifisignalanalyzer.wifi.graphutils.GraphConstants;

import org.apache.commons.lang3.StringUtils;

class ChannelAxisLabel implements LabelFormatter {
    private final WiFiBand wiFiBand;
    private final Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;

    ChannelAxisLabel(@NonNull WiFiBand wiFiBand, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        this.wiFiBand = wiFiBand;
        this.wiFiChannelPair = wiFiChannelPair;
    }

    @Override
    public String formatLabel(double value, boolean isValueX) {
        String result = StringUtils.EMPTY;

        int valueAsInt = (int) (value + (value < 0 ? -0.5 : 0.5));
        if (isValueX) {
            result += findChannel(valueAsInt);
        } else {
            if (valueAsInt <= GraphConstants.MAX_Y && valueAsInt > GraphConstants.MIN_Y) {
                result += Integer.toString(valueAsInt);
            }
        }
        return result;
    }

    @Override
    public void setViewport(Viewport viewport) {
        // ignore
    }

    @NonNull
    private String findChannel(int value) {
        WiFiChannels wiFiChannels = wiFiBand.getWiFiChannels();
        WiFiChannel wiFiChannel = wiFiChannels.getWiFiChannelByFrequency(value, wiFiChannelPair);
        if (wiFiChannel == WiFiChannel.UNKNOWN) {
            return StringUtils.EMPTY;
        }

        int channel = wiFiChannel.getChannel();
        Settings settings = MainContext.INSTANCE.getSettings();
        String countryCode = settings.getCountryCode();
        if (!wiFiChannels.isChannelAvailable(countryCode, channel)) {
            return StringUtils.EMPTY;
        }
        return Integer.toString(channel);
    }

}
