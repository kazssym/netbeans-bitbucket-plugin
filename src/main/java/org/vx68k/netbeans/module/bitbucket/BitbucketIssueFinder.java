/*
 * BitbucketIssueFinder.java
 * Copyright (C) 2018 Kaz Nishimura
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package org.vx68k.netbeans.module.bitbucket;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.netbeans.modules.bugtracking.spi.IssueFinder;

/**
 * Implementation of {@link IssueFinder} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketIssueFinder implements IssueFinder
{
    /**
     * Pattern to match an issue string.
     */
    private static final Pattern ISSUE_PATTERN = Pattern.compile("#(\\d+)");

    /**
     * Initialized the object while not allowing public instantiation.
     */
    protected BitbucketIssueFinder()
    {
    }

    @Override
    public int[] getIssueSpans(final CharSequence text)
    {
        Matcher matcher = ISSUE_PATTERN.matcher(text);

        List<Integer> spans = new ArrayList<>();
        while (matcher.find()) {
            spans.add(matcher.start());
            spans.add(matcher.end());
        }
        return spans.stream().mapToInt((i) -> i.intValue()).toArray();
    }

    @Override
    public String getIssueId(final String text)
    {
        Matcher matcher = ISSUE_PATTERN.matcher(text);

        String value = null;
        if (matcher.matches()) {
            value = matcher.group(1);
        }
        return value;
    }
}
