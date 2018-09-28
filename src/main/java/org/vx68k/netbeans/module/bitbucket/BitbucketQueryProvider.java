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

import java.util.Map;
import java.util.WeakHashMap;
import org.netbeans.modules.bugtracking.spi.QueryController;
import org.netbeans.modules.bugtracking.spi.QueryProvider;

/**
 * Implementation of {@link QueryProvider} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketQueryProvider implements
    QueryProvider<BitbucketQuery, BitbucketIssue>
{
    /**
     * Bitbucket Cloud connector.
     */
    private final BitbucketConnector connector;

    /**
     * Map for descriptors.
     */
    private final Map<BitbucketQuery, Descriptor> descriptors;

    /**
     * Initializes this object while preventing public instantiation.
     *
     * @param connectorInit value of the Bitbucket Cloud connector
     */
    BitbucketQueryProvider(final BitbucketConnector connectorInit)
    {
        connector = connectorInit;
        descriptors = new WeakHashMap<>();
    }

    /**
     * Returns the descriptor for a query.
     *
     * @param query query
     * @return descriptor
     */
    Descriptor getDescriptor(final BitbucketQuery query)
    {
        Descriptor value = descriptors.get(query);
        if (value == null) {
            value = new Descriptor();
            descriptors.put(query, value);
        }
        return value;
    }

    /**
     * Returns a query for a display name.
     *
     * @param displayName display name
     * @return query
     */
    BitbucketQuery getQuery(final String displayName)
    {
        BitbucketQuery value = new BitbucketQuery();
        Descriptor descriptor = getDescriptor(value);
        descriptor.setDisplayName(displayName);
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(final BitbucketQuery query)
    {
        Descriptor descriptor = getDescriptor(query);
        return descriptor.getDisplayName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTooltip(final BitbucketQuery query)
    {
        Descriptor descriptor = getDescriptor(query);
        return descriptor.getTooltip();
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
    public boolean canRemove(final BitbucketQuery query)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(final BitbucketQuery query)
    {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canRename(final BitbucketQuery query)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rename(final BitbucketQuery query, final String newName)
    {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIssueContainer(
        final BitbucketQuery query,
        final IssueContainer<BitbucketIssue> newValue)
    {
        Descriptor descriptor = getDescriptor(query);
        descriptor.setIssueContainer(newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh(final BitbucketQuery query)
    {
        Descriptor descriptor = getDescriptor(query);
        descriptor.refresh();
    }

    /**
     * Descriptor for a query.
     */
    public final class Descriptor
    {
        /**
         * Display name.
         */
        private String displayName = null;

        /**
         * Tooltip text.
         */
        private String tooltip = null;

        /**
         * Issue container object.
         */
        private IssueContainer<BitbucketIssue> issueContainer;

        /**
         * Prevents public initialization.
         */
        Descriptor()
        {
        }

        /**
         * Returns the display name.
         *
         * @return display name
         */
        public String getDisplayName()
        {
            return displayName;
        }

        /**
         * Sets the display name.
         *
         * @param newValue new value of the display name
         */
        public void setDisplayName(final String newValue)
        {
            displayName = newValue;
        }

        /**
         * Returns the tooltip text.
         *
         * @return tooltip text
         */
        public String getTooltip()
        {
            return tooltip;
        }

        /**
         * Sets the tooltip text.
         *
         * @param newValue new value of the tooltip text
         */
        public void setTooltip(final String newValue)
        {
            tooltip = newValue;
        }

        /**
         * Sets the issue container object.
         *
         * @param newValue new value of the issue container object
         */
        public void setIssueContainer(
            final IssueContainer<BitbucketIssue> newValue)
        {
            issueContainer = newValue;
        }

        /**
         * Refreshes the query.
         */
        public void refresh()
        {
            if (issueContainer != null) {
                issueContainer.refreshingStarted();
                // @todo Add code.
                issueContainer.refreshingFinished();
            }
        }
    }
}
