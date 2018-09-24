/*
 * BitbucketQueryProvider.java - class BitbucketQueryProvider
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

import org.netbeans.modules.bugtracking.spi.QueryController;
import org.netbeans.modules.bugtracking.spi.QueryProvider;

/**
 * Implementation of {@link QueryProvider} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
final class BitbucketQueryProvider implements
    QueryProvider<BitbucketQuery, BitbucketIssue>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(final BitbucketQuery query)
    {
        return query.getDisplayName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTooltip(final BitbucketQuery q)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QueryController getController(final BitbucketQuery q)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canRemove(final BitbucketQuery q)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(final BitbucketQuery q)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canRename(final BitbucketQuery q)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rename(final BitbucketQuery q, final String string)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIssueContainer(
        final BitbucketQuery q, final IssueContainer<BitbucketIssue> ic)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh(final BitbucketQuery q)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
