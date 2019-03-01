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

import net.rheinsieg.wifisignalanalyzer.MainContextHelper;
import net.rheinsieg.wifisignalanalyzer.RobolectricUtil;
import net.rheinsieg.wifisignalanalyzer.freifunk.model.FreifunkService;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(RobolectricTestRunner.class)
public class FreifunkFragmentTest {

    private FreifunkFragment fixture;
    private FreifunkService freifunkService;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();

        freifunkService = MainContextHelper.INSTANCE.getFreifunkService();

        fixture = new FreifunkFragment();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testOnCreateView() {
        // setup
        when(freifunkService.findFreifunk()).thenReturn(Collections.emptyList());
        // execute
        SupportFragmentTestUtil.startFragment(fixture);
        // validate
        assertNotNull(fixture);
        verify(freifunkService).findFreifunk();
    }

    @Test
    public void testListenerOnQueryTextChange() {
        // setup
        String values = "     ABS       ADF      ";
        String expected = "ABS ADF";
        FreifunkAdapter freifunkAdapter = mock(FreifunkAdapter.class);
        FreifunkFragment.Listener fixture = new FreifunkFragment.Listener(freifunkAdapter);
        // execute
        boolean actual = fixture.onQueryTextChange(values);
        // verify
        assertTrue(actual);
        verify(freifunkAdapter).update(expected);
    }

    @Test
    public void testListenerOnQueryTextChangeWithNull() {
        // setup
        FreifunkAdapter freifunkAdapter = mock(FreifunkAdapter.class);
        FreifunkFragment.Listener fixture = new FreifunkFragment.Listener(freifunkAdapter);
        // execute
        boolean actual = fixture.onQueryTextChange(null);
        // verify
        assertTrue(actual);
        verify(freifunkAdapter).update(StringUtils.EMPTY);
    }


}