/*
 * BitbucketIssueProvider.java - class BitbucketIssueProvider
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

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import org.netbeans.modules.bugtracking.spi.IssueController;
import org.netbeans.modules.bugtracking.spi.IssueProvider;

/**
 *
 * @author Kaz Nishimura
 */
class BitbucketIssueProvider implements IssueProvider<BitbucketIssue>
{
    @Override
    public String getDisplayName(final BitbucketIssue i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTooltip(final BitbucketIssue i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getID(final BitbucketIssue i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<String> getSubtasks(final BitbucketIssue i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSummary(final BitbucketIssue i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isNew(final BitbucketIssue i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isFinished(final BitbucketIssue i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean refresh(final BitbucketIssue i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addComment(
        final BitbucketIssue i, final String string, final boolean bln)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void attachFile(
        final BitbucketIssue i, final File file, final String string,
        final boolean bln)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IssueController getController(final BitbucketIssue i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPropertyChangeListener(
        final BitbucketIssue i, final PropertyChangeListener pl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePropertyChangeListener(
        final BitbucketIssue i, final PropertyChangeListener pl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
