/*
 * BitbucketRepositoryProvider.java
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

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.netbeans.modules.bugtracking.spi.RepositoryProvider;
import org.openide.util.NotImplementedException;
import org.vx68k.bitbucket.api.BitbucketIssue;
import org.vx68k.bitbucket.api.BitbucketIssueTracker;
import org.vx68k.bitbucket.api.client.BitbucketClient;
import org.vx68k.netbeans.module.bitbucket.ui.BitbucketRepositoryController;

/**
 * Implementation of {@link RepositoryProvider} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketRepositoryProvider implements
    RepositoryProvider<
        BitbucketIssueTrackerProxy, BitbucketQuery, BitbucketIssue>
{
    /**
     * Regular expression pattern for a full name.
     */
    public static final Pattern REPOSITORY_NAME_PATTERN =
        Pattern.compile("([^/]+)/([^/]+)");

    /**
     * Filter expression for open tasks.
     */
    private static final String OPEN_ISSUES_FILTER = "state <= \"open\"";

    /**
     * Map for descriptors.
     */
    private final Map<BitbucketIssueTrackerProxy, Adapter> adapterMap;

    /**
     * Initializes the object.
     */
    protected BitbucketRepositoryProvider()
    {
        this.adapterMap = new WeakHashMap<>();
    }

    /**
     * Returns the descriptor for a repository.
     *
     * @param repository repository
     * @return descriptor
     */
    protected Adapter getAdapter(final BitbucketIssueTrackerProxy repository)
    {
        Adapter value = adapterMap.get(repository);
        if (value == null) {
            value = new Adapter(repository);
            adapterMap.put(repository, value);
        }
        return value;
    }

    /**
     * Sets the properties of a Bitbucket Cloud repository according to a
     * {@link RepositoryInfo} object.
     *
     * @param repository a Bitbucket Cloud repository
     * @param info a {@link RepositoryInfo} object
     */
    protected void setInfo(
        final BitbucketIssueTrackerProxy repository, final RepositoryInfo info)
    {
        Adapter adapter = getAdapter(repository);

        adapter.setFullName(info.getUrl());
        adapter.setDisplayName(info.getDisplayName());
        adapter.setTooltip(info.getTooltip());

        repository.setTarget(adapter.getIssueTracker());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepositoryInfo getInfo(final BitbucketIssueTrackerProxy repository)
    {
        Adapter adapter = getAdapter(repository);

        RepositoryInfo value = null;
        if (adapter.getFullName() != null) {
            value = new RepositoryInfo(
                adapter.getFullName(), BitbucketConnector.ID,
                adapter.getFullName(), adapter.getDisplayName(),
                adapter.getTooltip());
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getIcon(final BitbucketIssueTrackerProxy repository)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removed(final BitbucketIssueTrackerProxy repository)
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<BitbucketIssue> getIssues(
        final BitbucketIssueTrackerProxy repository, final String... ids)
    {
        return Arrays.stream(ids)
            .map((id) -> repository.getIssue(Integer.parseInt(id)))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitbucketQuery createQuery(
        final BitbucketIssueTrackerProxy repository)
    {
        return new BitbucketQuery(repository);
    }

    /**
     * {@inheritDoc}
     * <p>This implementation always returns {@code null}.</p>
     */
    @Override
    public BitbucketIssue createIssue(
        final BitbucketIssueTrackerProxy repository)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitbucketIssue createIssue(
        final BitbucketIssueTrackerProxy repository, final String summary,
        final String description)
    {
        // @todo Implement this method.
        throw new NotImplementedException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<BitbucketQuery> getQueries(
        final BitbucketIssueTrackerProxy repository)
    {
        List<BitbucketQuery> value = new ArrayList<>();

        BitbucketQuery allIssues = new BitbucketQuery(repository);
        allIssues.setDisplayName("All Tasks");
        value.add(allIssues);

        BitbucketQuery openIssues = new BitbucketQuery(repository);
        openIssues.setDisplayName("Open Tasks");
        openIssues.setFilter(OPEN_ISSUES_FILTER);
        value.add(openIssues);

        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<BitbucketIssue> simpleSearch(
        final BitbucketIssueTrackerProxy repository, final String criteria)
    {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canAttachFiles(final BitbucketIssueTrackerProxy repository)
    {
        return false; // @todo This should be true.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepositoryController getController(
        final BitbucketIssueTrackerProxy repository)
    {
        Adapter adapter = getAdapter(repository);
        return adapter.getController();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPropertyChangeListener(
        final BitbucketIssueTrackerProxy repository,
        final PropertyChangeListener listener)
    {
        Adapter adapter = getAdapter(repository);
        adapter.addPropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePropertyChangeListener(
        final BitbucketIssueTrackerProxy repository,
        final PropertyChangeListener listener)
    {
        Adapter adapter = getAdapter(repository);
        adapter.removePropertyChangeListener(listener);
    }

    /**
     * Repository adapter.
     */
    public static final class Adapter
    {
        /**
         * Weak reference to the repository.
         */
        private final WeakReference<BitbucketIssueTrackerProxy> repository;

        /**
         * Bitbucket API client for the repository.
         */
        private final BitbucketClient bitbucketClient;

        /**
         * Property change support object.
         */
        private final PropertyChangeSupport support;

        /**
         * Full name of the repository.
         */
        private String fullName = null;

        /**
         * Display name for the repository.
         */
        private String displayName = null;

        /**
         * Tooltip text for the repository.
         */
        private String tooltip = null;

        /**
         * Controller object.
         */
        private RepositoryController controller = null;

        /**
         * Initializes the object.
         *
         * @param repository a repository
         */
        protected Adapter(final BitbucketIssueTrackerProxy repository)
        {
            this.repository = new WeakReference<>(repository);
            this.bitbucketClient = new BitbucketClient();
            this.support = new PropertyChangeSupport(this);
        }

        /**
         * Returns the repository.
         *
         * @return the repository
         */
        public BitbucketIssueTrackerProxy getRepository()
        {
            return repository.get();
        }

        /**
         * Returns the identifier of the repository.
         *
         * @return the identifier for the repository
         */
        public String getFullName()
        {
            return fullName;
        }

        /**
         * Sets the identifier for the repository.
         *
         * @param newValue a new value of the identifier
         */
        public void setFullName(final String newValue)
        {
            String oldValue = fullName;
            fullName = newValue;
            support.firePropertyChange("fullName", oldValue, newValue);
        }

        /**
         * Returns the display name for the repository.
         *
         * @return the display name for the repository
         */
        public String getDisplayName()
        {
            return displayName;
        }

        /**
         * Sets the display name for the repository.
         *
         * @param newValue a new value of the display name
         */
        public void setDisplayName(final String newValue)
        {
            String oldValue = displayName;
            displayName = newValue;
            support.firePropertyChange("displayName", oldValue, newValue);
        }

        /**
         * Returns the tooltip text for the repository.
         *
         * @return the tooltip text for the repository
         */
        public String getTooltip()
        {
            return tooltip;
        }

        /**
         * Sets the tooltip text for the repository.
         *
         * @param newValue a new value of the tooltip text
         */
        public void setTooltip(final String newValue)
        {
            String oldValue = tooltip;
            tooltip = newValue;
            support.firePropertyChange("tooltip", oldValue, newValue);
        }

        public BitbucketIssueTracker getIssueTracker()
        {
            Matcher m = REPOSITORY_NAME_PATTERN.matcher(fullName);
            if (!m.matches()) {
                throw new IllegalArgumentException("Invalid repository name");
            }

            return (BitbucketIssueTracker) // @todo Remove this cast.
                bitbucketClient.getRepository(m.group(1), m.group(2));
        }

        /**
         * Returns the controller object for a repository.
         *
         * @return controller object
         */
        public RepositoryController getController()
        {
            if (controller == null) {
                controller = new BitbucketRepositoryController(this);
            }
            return controller;
        }

        /**
         * Reset the controller object.
         */
        public void resetController()
        {
            controller = null;
        }

        /**
         * Adds a property change listener.
         *
         * @param listener property change listener to add
         */
        public void addPropertyChangeListener(
            final PropertyChangeListener listener)
        {
            support.addPropertyChangeListener(listener);
        }

        /**
         * Removes a property change listener.
         *
         * @param listener property change listener to remove
         */
        public void removePropertyChangeListener(
            final PropertyChangeListener listener)
        {
            support.removePropertyChangeListener(listener);
        }
    }
}
