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

import android.content.res.Resources;
import android.support.annotation.NonNull;

import net.rheinsieg.util.FileUtils;
import net.rheinsieg.wifisignalanalyzer.R;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class FreifunkDB implements FreifunkService {
    private final Resources resources;
    private final Map<String, List<String>> freifunker;
    private final Map<String, String> macs;
    private boolean loaded;

    FreifunkDB(@NonNull Resources resources) {
        this.resources = resources;
        this.freifunker = new TreeMap<>();
        this.macs = new TreeMap<>();
        this.loaded = false;
    }

    @NonNull
    @Override
    public String findFreifunkName(String address) {
        String result = getMacs().get(FreifunkUtils.clean(address));
        return result == null ? StringUtils.EMPTY : result;
    }

    @NonNull
    @Override
    public List<String> findMacAddresses(String freifunkName) {
        if (StringUtils.isBlank(freifunkName)) {
            return new ArrayList<>();
        }
        List<String> results = getFreifunk().get(freifunkName);
        return results == null ? new ArrayList<>() : results;
    }

    @NonNull
    @Override
    public List<String> findFreifunk() {
        return new ArrayList<>(getFreifunk().keySet());
    }

    @NonNull
    @Override
    public List<String> findFreifunk(@NonNull String filter) {
        Predicate<String> predicate =
            PredicateUtils.anyPredicate(new StringContains(filter), new MacContains(filter));
        return new ArrayList<>(CollectionUtils.select(getFreifunk().keySet(), predicate));
    }

    @NonNull
    Map<String, List<String>> getFreifunk() {
        load(resources);
        return freifunker;
    }

    @NonNull
    Map<String, String> getMacs() {
        load(resources);
        return macs;
    }

    private void load(@NonNull Resources resources) {
        if (!loaded) {
            loaded = true;
            String[] lines = FileUtils.readFile(resources, R.raw.freifunk).split("\n");
            for (String freifunk : lines) {
                if (freifunk != null) {
                    String[] parts = freifunk.split("\\|");
                    if (parts.length == 2) {
                        List<String> addresses = new ArrayList<>();
                        String name = parts[0];
                        freifunker.put(name, addresses);
                        int length = parts[1].length();
                        for (int i = 0; i < length; i += FreifunkUtils.MAX_SIZE) {
                            String mac = parts[1].substring(i, i + FreifunkUtils.MAX_SIZE);
                            addresses.add(FreifunkUtils.toMacAddress(mac));
                            macs.put(mac, name);
                        }
                    }
                }
            }
        }
    }

    private class StringContains implements Predicate<String> {
        private final String filter;

        private StringContains(@NonNull String filter) {
            this.filter = filter;
        }

        @Override
        public boolean evaluate(String object) {
            return object.contains(filter);
        }
    }

    private class MacContains implements Predicate<String> {
        private final String filter;

        private MacContains(@NonNull String filter) {
            this.filter = filter;
        }

        @Override
        public boolean evaluate(String object) {
            return IterableUtils.matchesAny(findMacAddresses(object), new StringContains(filter));
        }
    }

}
