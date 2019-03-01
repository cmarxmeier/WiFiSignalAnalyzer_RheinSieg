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

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.rheinsieg.wifisignalanalyzer.MainActivity;
import net.rheinsieg.wifisignalanalyzer.MainContextHelper;
import net.rheinsieg.wifisignalanalyzer.R;
import net.rheinsieg.wifisignalanalyzer.RobolectricUtil;
import net.rheinsieg.wifisignalanalyzer.freifunk.model.FreifunkService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
public class FreifunkAdapterTest {
    private static final String FREIFUNK_NAME1 = "N1";
    private static final String FREIFUNK_NAME2 = "N2";
    private static final String FREIFUNK_NAME3 = "N3";

    private MainActivity mainActivity;
    private FreifunkService freifunkService;
    private List<String> freifunkers;
    private FreifunkAdapter fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        freifunkService = MainContextHelper.INSTANCE.getFreifunkService();

        freifunkers = Arrays.asList(FREIFUNK_NAME1, FREIFUNK_NAME2, FREIFUNK_NAME3);
        when(freifunkService.findFreifunk()).thenReturn(freifunkers);

        fixture = new FreifunkAdapter(mainActivity, freifunkService);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testConstructor() {
        assertEquals(freifunkers.size(), fixture.getCount());
        assertEquals(freifunkers.get(0), fixture.getItem(0));
        assertEquals(freifunkers.get(1), fixture.getItem(1));
        assertEquals(freifunkers.get(2), fixture.getItem(2));
        verify(freifunkService).findFreifunk();
    }

    @Test
    public void testGetView() {
        // setup
        when(freifunkService.findMacAddresses(FREIFUNK_NAME2)).thenReturn(Arrays.asList("VALUE1", "VALUE2", "VALUE3"));
        String expected = "VALUE1, VALUE2, VALUE3";
        ViewGroup viewGroup = mainActivity.findViewById(android.R.id.content);
        // execute
        View actual = fixture.getView(1, null, viewGroup);
        // validate
        assertNotNull(actual);

        assertEquals(FREIFUNK_NAME2, actual.<TextView>findViewById(R.id.freifunk_name).getText().toString());
        assertEquals(expected, actual.<TextView>findViewById(R.id.freifunk_macs).getText().toString());

        verify(freifunkService).findMacAddresses(FREIFUNK_NAME2);

        verify(freifunkService, never()).findFreifunkName(FREIFUNK_NAME1);
        verify(freifunkService, never()).findFreifunkName(FREIFUNK_NAME3);
    }

}