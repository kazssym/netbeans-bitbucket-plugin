/*
 * BitbucketRepositoryProvider.java - class BitbucketRepositoryProvider
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.netbeans.modules.bugtracking.spi.RepositoryProvider;
import org.openide.util.NotImplementedException;
import org.vx68k.bitbucket.api.BitbucketIssue;
import org.vx68k.bitbucket.api.BitbucketRepository;
import org.vx68k.bitbucket.api.client.BitbucketClient;
import org.vx68k.netbeans.module.bitbucket.ui.BitbucketRepositoryController;

/**
 * Implementation of {@link RepositoryProvider} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketRepositoryProvider implements
    RepositoryProvider<BitbucketRepository, BitbucketQuery, BitbucketIssue>
{
    /**
     * Regular expression pattern for a full name.
     */
    public static final Pattern FULL_NAME_PATTERN =
        Pattern.compile("([^/]+)/([^/]+)");

    /**
     * Filter expression for open tasks.
     */
    private static final String OPEN_ISSUES_FILTER = "state <= \"open\"";

    /**
     * Map for descriptors.
     */
    private final Map<BitbucketRepository, Descriptor> descriptors;

    /**
     * Initializes the object.
     */
    protected BitbucketRepositoryProvider()
    {
        this.descriptors = new WeakHashMap<>();
    }

    /**
     * Returns the descriptor for a repository.
     *
     * @param repository repository
     * @return descriptor
     */
    protected Descriptor getDescriptor(final BitbucketRepository repository)
    {
        Descriptor value = descriptors.get(repository);
        if (value == null) {
            value = new Descriptor(repository);
            descriptors.put(repository, value);
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
        final BitbucketRepositoryProxy repository, final RepositoryInfo info)
    {
        String fullName = info.getUrl();
        Matcher matcher = FULL_NAME_PATTERN.matcher(fullName);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid repository name");
        }

        Descriptor descriptor = getDescriptor(repository);
        descriptor.setId(info.getID());
        descriptor.setDisplayName(info.getDisplayName());
        descriptor.setTooltip(info.getTooltip());

        BitbucketClient client = descriptor.getBitbucketClient();
        repository.setRealRepository(
            client.getRepository(matcher.group(1), matcher.group(2)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepositoryInfo getInfo(final BitbucketRepository repository)
    {
        Descriptor descriptor = getDescriptor(repository);

        RepositoryInfo value = null;
        if (descriptor.getId() != null) {
            value = new RepositoryInfo(
                descriptor.getId(), BitbucketConnector.ID,
                repository.getFullName(), descriptor.getDisplayName(),
                descriptor.getTooltip());
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getIcon(final BitbucketRepository r)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepositoryController getController(
        final BitbucketRepository repository)
    {
        Descriptor descriptor = getDescriptor(repository);
        return descriptor.getController();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removed(final BitbucketRepository repository)
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<BitbucketIssue> getIssues(
        final BitbucketRepository r, final String... strings)
    {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitbucketQuery createQuery(
        final BitbucketRepository repository)
    {
        // @todo Implement this method.
        throw new NotImplementedException();
    }

    /**
     * {@inheritDoc}
     * <p>This implementation always returns {@code null}.</p>
     */
    @Override
    public BitbucketIssue createIssue(final BitbucketRepository repository)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitbucketIssue createIssue(
        final BitbucketRepository r, final String string, final String string1)
    {
        // @todo Implement this method.
        throw new NotImplementedException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<BitbucketQuery> getQueries(
        final BitbucketRepository repository)
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
        final BitbucketRepository r, final String string)
    {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canAttachFiles(final BitbucketRepository r)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPropertyChangeListener(
        final BitbucketRepository repository,
        final PropertyChangeListener listener)
    {
        Descriptor descriptor = getDescriptor(repository);
        descriptor.addPropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePropertyChangeListener(
        final BitbucketRepository repository,
        final PropertyChangeListener listener)
    {
        Descriptor descriptor = getDescriptor(repository);
        descriptor.removePropertyChangeListener(listener);
    }

    /**
     * Descriptor of a repository.
     */
    public static final class Descriptor
    {
        /**
         * Weak reference to the repository.
         */
        private final WeakReference<BitbucketRepository> repository;

        /**
         * Bitbucket API client for the repository.
         */
        private final BitbucketClient bitbucketClient;

        /**
         * Property change support object.
         */
        private final PropertyChangeSupport support;

        /**
         * Identifier of the repository.
         */
        private String id = null;

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
        protected Descriptor(final BitbucketRepository repository)
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
        public BitbucketRepository getRepository()
        {
            return repository.get();
        }

        /**
         * Returns the Bitbucket API client for the repository.
         *
         * @return the Bitbucket API client for the repository.
         */
        public BitbucketClient getBitbucketClient()
        {
            return bitbucketClient;
        }

        /**
         * Returns the identifier of the repository.
         *
         * @return the identifier for the repository
         */
        public String getId()
        {
            return id;
        }

        /**
         * Sets the identifier for the repository.
         *
         * @param newValue a new value of the identifier
         */
        public void setId(final String newValue)
        {
            String oldValue = id;
            id = newValue;
            support.firePropertyChange("id", oldValue, newValue);
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
