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
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import org.netbeans.modules.bugtracking.spi.IssueController;
import org.netbeans.modules.bugtracking.spi.IssueProvider;
import org.vx68k.bitbucket.api.BitbucketIssue;
import org.vx68k.netbeans.module.bitbucket.ui.BitbucketIssueController;

/**
 * Implementation of {@link IssueProvider} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketIssueProvider implements
    IssueProvider<BitbucketIssue>
{
    /**
     * Map for descriptors.
     */
    private final Map<BitbucketIssue, Descriptor> descriptors;

    /**
     * Initializes the object.
     */
    public BitbucketIssueProvider()
    {
        descriptors = new WeakHashMap<>();
    }

    /**
     * Returns the descriptor for an issue.
     *
     * @param issue an issue
     * @return the descriptor for an issue
     */
    Descriptor getDescriptor(final BitbucketIssue issue)
    {
        Descriptor value = descriptors.get(issue);
        if (value == null) {
            value = new Descriptor();
            descriptors.put(issue, value);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(final BitbucketIssue issue)
    {
        return String.format("##%d: %s", issue.getId(), issue.getTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTooltip(final BitbucketIssue issue)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getID(final BitbucketIssue issue)
    {
        return String.format("#%d", issue.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getSubtasks(final BitbucketIssue issue)
    {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSummary(final BitbucketIssue issue)
    {
        return issue.getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNew(final BitbucketIssue issue)
    {
        return "new".equals(issue.getState());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFinished(final BitbucketIssue issue)
    {
        // @todo Implement this method.
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean refresh(final BitbucketIssue issue)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComment(
        final BitbucketIssue i, final String string, final boolean bln)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachFile(
        final BitbucketIssue i, final File file, final String string,
        final boolean bln)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueController getController(final BitbucketIssue issue)
    {
        return new BitbucketIssueController(issue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPropertyChangeListener(
        final BitbucketIssue issue, final PropertyChangeListener listener)
    {
        Descriptor descriptor = getDescriptor(issue);
        descriptor.addPropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePropertyChangeListener(
        final BitbucketIssue issue, final PropertyChangeListener listener)
    {
        Descriptor descriptor = getDescriptor(issue);
        descriptor.removePropertyChangeListener(listener);
    }

    /**
     * Issue descriptor.
     */
    public final class Descriptor
    {
        /**
         * Property change support object.
         */
        private final PropertyChangeSupport support;

        /**
         * Initializes the object.
         */
        Descriptor()
        {
            this.support = new PropertyChangeSupport(this);
        }

        /**
         * Adds a property change listener for the issue.
         *
         * @param listener a property change listener
         */
        public void addPropertyChangeListener(
            final PropertyChangeListener listener)
        {
            support.addPropertyChangeListener(listener);
        }

        /**
         * Removes a property change listener for the issue.
         *
         * @param listener a property change listener
         */
        public void removePropertyChangeListener(
            final PropertyChangeListener listener)
        {
            support.removePropertyChangeListener(listener);
        }
    }
}
