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

package net.rheinsieg.wifisignalanalyzer.freifunk.model;

import net.rheinsieg.wifisignalanalyzer.MainActivity;
import net.rheinsieg.wifisignalanalyzer.RobolectricUtil;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class FreifunkDBTest {
    private static final String FREIFUNK_NAME = "CISCO SYSTEMS INC";
    private static final String MAC_ADDRESS = "00:23:AB:8C:DF:10";
    private static final String FREIFUNK_NAME_INVALID = "XXXXX";
    private static final String MAC_ADDRESS_INVALID = "XX:XX:XX";
    private static final int FREIFUNK_SIZE = 17185;
    private static final int MACS_SIZE = 25579;

    private FreifunkDB fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new FreifunkDB(mainActivity.getResources());
    }

    @Test
    public void testFindFreifunkName() {
        // execute
        String actual = fixture.findFreifunkName(MAC_ADDRESS);
        // validate
        assertEquals(FREIFUNK_NAME, actual);
    }

    @Test
    public void testFindFreifunkNameWithNull() {
        // execute
        String actual = fixture.findFreifunkName(null);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testFindFreifunkNameWithInvalidMac() {
        // execute
        String actual = fixture.findFreifunkName(MAC_ADDRESS_INVALID);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testFindMacAddresses() {
        // setup
        int expectedSize = 842;
        // execute
        List<String> actual = fixture.findMacAddresses(FREIFUNK_NAME);
        // validate
        assertEquals(expectedSize, actual.size());

        assertEquals("00:00:0C", actual.get(0));
        assertEquals("FC:FB:FB", actual.get(expectedSize - 1));
        assertEquals("00:9A:D2", actual.get(expectedSize / 2));
    }

    @Test
    public void testFindMacAddressesWithNull() {
        // execute
        List<String> actual = fixture.findMacAddresses(null);
        // validate
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindMacAddressesWithInvalidName() {
        // execute
        List<String> actual = fixture.findMacAddresses(FREIFUNK_NAME_INVALID);
        // validate
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindFreifunk() {
        // execute
        List<String> actual = fixture.findFreifunk();
        // validate
        assertEquals(FREIFUNK_SIZE, actual.size());
    }

    @Test
    public void testFindFreifunkWithFreifunkFilter() {
        // execute
        List<String> actual = fixture.findFreifunk("1394 ");
        // validate
        assertEquals(2, actual.size());
        assertEquals("1394 PRINTER WORKING GROUP", actual.get(0));
        assertEquals("1394 TRADE ASSOCIATION", actual.get(1));
    }

    @Test
    public void testFindFreifunkWithMacFilter() {
        // execute
        List<String> actual = fixture.findFreifunk("00:A0:2");
        // validate
        assertEquals(16, actual.size());
        assertEquals("1394 TRADE ASSOCIATION", actual.get(0));
        assertEquals("TRANSITIONS RESEARCH CORP", actual.get(15));
    }

    @Test
    public void testGetFreifunk() {
        // execute & validate
        assertEquals(FREIFUNK_SIZE, fixture.getFreifunk().size());
    }

    @Test
    public void testGetMacs() {
        // execute & validate
        assertEquals(MACS_SIZE, fixture.getMacs().size());
    }

}