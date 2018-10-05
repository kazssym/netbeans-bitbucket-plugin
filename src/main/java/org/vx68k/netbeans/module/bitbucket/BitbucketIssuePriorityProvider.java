/*
 * BitbucketIssuePriorityProvider.java
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

import org.netbeans.modules.bugtracking.spi.IssuePriorityInfo;
import org.netbeans.modules.bugtracking.spi.IssuePriorityProvider;
import org.vx68k.bitbucket.api.BitbucketIssue;

/**
 * Implementation of {@link IssuePriorityProvider} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketIssuePriorityProvider implements
    IssuePriorityProvider<BitbucketIssue>
{
    /**
     * System-wide priority information.
     */
    private final IssuePriorityInfo[] priorityInfos;

    /**
     * Initializes the object.
     */
    BitbucketIssuePriorityProvider()
    {
        this.priorityInfos = new IssuePriorityInfo[] {
            new IssuePriorityInfo("blocker", "blocker", null),
            new IssuePriorityInfo("critical", "critical", null),
            new IssuePriorityInfo("major", "major", null),
            new IssuePriorityInfo("minor", "minor", null),
            new IssuePriorityInfo("trivial", "trivial", null),
        };
    }

    @Override
    public String getPriorityID(BitbucketIssue issue)
    {
        return issue.getPriority();
    }

    @Override
    public IssuePriorityInfo[] getPriorityInfos()
    {
        return priorityInfos;
    }
}
