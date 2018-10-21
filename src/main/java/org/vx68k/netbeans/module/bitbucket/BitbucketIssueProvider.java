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
import java.lang.ref.WeakReference;
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
     * Map to adapters.
     */
    private final Map<BitbucketIssue, Adapter> adapterMap;

    /**
     * Initializes the object.
     */
    public BitbucketIssueProvider()
    {
        adapterMap = new WeakHashMap<>();
    }

    /**
     * Returns the adapter for a Bitbucket Cloud issue.
     *
     * @param issue a Bitbucket Cloud issue
     * @return the adapter for a Bitbucket Cloud issue
     */
    Adapter getAdapter(final BitbucketIssue issue)
    {
        Adapter value = adapterMap.get(issue);
        if (value == null) {
            value = new Adapter(issue);
            adapterMap.put(issue, value);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getID(final BitbucketIssue issue)
    {
        Adapter adapter = getAdapter(issue);
        return adapter.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(final BitbucketIssue issue)
    {
        Adapter adapter = getAdapter(issue);
        return adapter.getDisplayName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTooltip(final BitbucketIssue issue)
    {
        Adapter adapter = getAdapter(issue);
        return adapter.getTooltip();
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
        Adapter adapter = getAdapter(issue);
        return adapter.getSummary();
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
        Adapter adapter = getAdapter(issue);
        return adapter.getController();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPropertyChangeListener(
        final BitbucketIssue issue, final PropertyChangeListener l)
    {
        Adapter adapter = getAdapter(issue);
        adapter.addPropertyChangeListener(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePropertyChangeListener(
        final BitbucketIssue issue, final PropertyChangeListener l)
    {
        Adapter adapter = getAdapter(issue);
        adapter.removePropertyChangeListener(l);
    }

    /**
     * Issue adapter.
     */
    public static final class Adapter
    {
        /**
         * Weak reference to the Bitbucket Cloud issue.
         */
        private final WeakReference<BitbucketIssue> issue;

        /**
         * Property change support object.
         */
        private final PropertyChangeSupport support;

        /**
         * Controller for the issue.
         */
        private BitbucketIssueController controller = null;

        /**
         * Initializes the object.
         *
         * @param issue a Bitbucket Cloud issue
         */
        protected Adapter(final BitbucketIssue issue)
        {
            this.issue = new WeakReference<>(issue);
            this.support = new PropertyChangeSupport(this);
        }

        /**
         * Returns the Bitbucket Cloud issue.
         *
         * @return the Bitbucket Cloud issue
         */
        public BitbucketIssue getIssue() {
            return issue.get();
        }

        /**
         * Returns the identifier for the issue.
         *
         * @return the identifier for the issue
         */
        public String getId()
        {
            return Integer.toString(getIssue().getId());
        }

        /**
         * Returns the display name of the issue.
         *
         * @return the display name of the issue
         */
        public String getDisplayName()
        {
            return String.format(
                "#%d: %s", getIssue().getId(), getIssue().getTitle());
        }

        /**
         * Returns the tooltip text of the issue.
         *
         * @return the tooltip text of the issue
         */
        public String getTooltip()
        {
            return getIssue().getState();
        }

        /**
         * Returns the summary of the issue.
         *
         * @return the summary of the issue
         */
        public String getSummary()
        {
            return getIssue().getTitle();
        }

        /**
         * Returns the controller for the issue.
         *
         * @return the controller for the issue
         */
        public BitbucketIssueController getController()
        {
            if (controller == null) {
                controller = new BitbucketIssueController(this);
            }
            return controller;
        }

        /**
         * Resets the controller for the issue.
         */
        public void resetController()
        {
            controller = null;
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
