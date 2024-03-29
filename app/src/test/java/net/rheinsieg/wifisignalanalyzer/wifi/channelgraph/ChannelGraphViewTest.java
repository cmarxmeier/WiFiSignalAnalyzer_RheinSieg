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

import android.support.v4.util.Pair;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import net.rheinsieg.wifisignalanalyzer.MainContextHelper;
import net.rheinsieg.wifisignalanalyzer.RobolectricUtil;
import net.rheinsieg.wifisignalanalyzer.settings.Settings;
import net.rheinsieg.wifisignalanalyzer.settings.ThemeStyle;
import net.rheinsieg.wifisignalanalyzer.wifi.band.WiFiBand;
import net.rheinsieg.wifisignalanalyzer.wifi.band.WiFiChannel;
import net.rheinsieg.wifisignalanalyzer.wifi.graphutils.GraphConstants;
import net.rheinsieg.wifisignalanalyzer.wifi.graphutils.GraphLegend;
import net.rheinsieg.wifisignalanalyzer.wifi.graphutils.GraphViewWrapper;
import net.rheinsieg.wifisignalanalyzer.wifi.model.SortBy;
import net.rheinsieg.wifisignalanalyzer.wifi.model.WiFiConnection;
import net.rheinsieg.wifisignalanalyzer.wifi.model.WiFiData;
import net.rheinsieg.wifisignalanalyzer.wifi.model.WiFiDetail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ChannelGraphViewTest {
    private Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;
    private Settings settings;
    private GraphViewWrapper graphViewWrapper;
    private DataManager dataManager;
    private ChannelGraphView fixture;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();

        graphViewWrapper = mock(GraphViewWrapper.class);
        dataManager = mock(DataManager.class);

        settings = MainContextHelper.INSTANCE.getSettings();

        wiFiChannelPair = new Pair<>(WiFiChannel.UNKNOWN, WiFiChannel.UNKNOWN);
        fixture = new ChannelGraphView(WiFiBand.GHZ2, wiFiChannelPair);
        fixture.setGraphViewWrapper(graphViewWrapper);
        fixture.setDataManager(dataManager);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testUpdate() {
        // setup
        Set<WiFiDetail> newSeries = Collections.emptySet();
        List<WiFiDetail> wiFiDetails = Collections.emptyList();
        WiFiData wiFiData = new WiFiData(wiFiDetails, WiFiConnection.EMPTY, Collections.emptyList());
        when(dataManager.getNewSeries(wiFiDetails, wiFiChannelPair)).thenReturn(newSeries);
        withSettings();
        // execute
        fixture.update(wiFiData);
        // validate
        verify(dataManager).getNewSeries(wiFiDetails, wiFiChannelPair);
        verify(dataManager).addSeriesData(graphViewWrapper, newSeries, GraphConstants.MAX_Y);
        verify(graphViewWrapper).removeSeries(newSeries);
        verify(graphViewWrapper).updateLegend(GraphLegend.RIGHT);
        verify(graphViewWrapper).setVisibility(View.VISIBLE);
        verifySettings();
    }

    private void verifySettings() {
        verify(settings).getSortBy();
        verify(settings, times(2)).getChannelGraphLegend();
        verify(settings, times(2)).getWiFiBand();
        verify(settings, times(2)).getGraphMaximumY();
        verify(settings).getThemeStyle();
    }

    private void withSettings() {
        when(settings.getChannelGraphLegend()).thenReturn(GraphLegend.RIGHT);
        when(settings.getSortBy()).thenReturn(SortBy.CHANNEL);
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ2);
        when(settings.getGraphMaximumY()).thenReturn(GraphConstants.MAX_Y);
        when(settings.getThemeStyle()).thenReturn(ThemeStyle.DARK);
    }

    @Test
    public void testGetGraphView() {
        // setup
        GraphView expected = mock(GraphView.class);
        when(graphViewWrapper.getGraphView()).thenReturn(expected);
        // execute
        GraphView actual = fixture.getGraphView();
        // validate
        assertEquals(expected, actual);
        verify(graphViewWrapper).getGraphView();
    }
}