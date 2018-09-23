/*
 * BitbucketConnector.java - class BitbucketConnector
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

import org.netbeans.modules.bugtracking.api.Repository;
import org.netbeans.modules.bugtracking.spi.BugtrackingConnector;
import org.netbeans.modules.bugtracking.spi.BugtrackingSupport;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;

/**
 * Implementation of {@link BugtrackingConnector} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
@BugtrackingConnector.Registration(
    id = BitbucketConnector.ID, displayName = BitbucketConnector.DISPLAY_NAME,
    tooltip = BitbucketConnector.TOOLTIP,
    iconPath = "org/vx68k/netbeans/module/bitbucket/icon-16.png")
public class BitbucketConnector implements BugtrackingConnector
{
    /**
     * Identifier of the Bitbucket Cloud connector.
     */
    public static final String ID = "org.vx68k.netbeans.module.bitbucket";

    /**
     * Display name of the Bitbucket Cloud connector.
     */
    public static final String DISPLAY_NAME = "Bitbucket Cloud (by VX68k.org)";

    /**
     * Tooltip text of the Bitbucket Cloud connector.
     */
    public static final String TOOLTIP = "Bitbucket Cloud Task Repository";

    /**
     *
     */
    private final BugtrackingSupport<
        BitbucketRepository, BitbucketQuery, BitbucketIssue> support;

    /**
     * Constructs this object.
     */
    public BitbucketConnector()
    {
        support = new BugtrackingSupport<>(
            new BitbucketRepositoryProvider(ID),
            new BitbucketQueryProvider(),
            new BitbucketIssueProvider());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Repository createRepository()
    {
        return createRepository(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Repository createRepository(final RepositoryInfo info)
    {
        return support.createRepository(
            new BitbucketRepository(info), null, null, null, null);
    }
}
