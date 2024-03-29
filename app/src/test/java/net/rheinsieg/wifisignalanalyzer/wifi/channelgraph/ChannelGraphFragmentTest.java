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

import net.rheinsieg.wifisignalanalyzer.MainContextHelper;
import net.rheinsieg.wifisignalanalyzer.RobolectricUtil;
import net.rheinsieg.wifisignalanalyzer.wifi.scanner.ScannerService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class ChannelGraphFragmentTest {

    private ScannerService scanner;
    private ChannelGraphFragment fixture;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();

        scanner = MainContextHelper.INSTANCE.getScannerService();

        fixture = new ChannelGraphFragment();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testOnCreateView() {
        // execute
        SupportFragmentTestUtil.startFragment(fixture);
        // validate
        assertNotNull(fixture);
        verify(scanner).update();
        verify(scanner).register(fixture.getChannelGraphAdapter());
    }

    @Test
    public void testOnResume() {
        // setup
        SupportFragmentTestUtil.startFragment(fixture);
        // execute
        fixture.onResume();
        // validate
        verify(scanner, times(2)).update();
    }

    @Test
    public void testOnDestroy() {
        // setup
        SupportFragmentTestUtil.startFragment(fixture);
        // execute
        fixture.onDestroy();
        // validate
        verify(scanner).unregister(fixture.getChannelGraphAdapter());
    }
}