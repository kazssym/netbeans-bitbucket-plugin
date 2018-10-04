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
     * Bitbucket Cloud connector.
     */
    private final BitbucketConnector connector;

    /**
     * Map for descriptors.
     */
    private final Map<BitbucketRepository, Descriptor> descriptors;

    /**
     * Initializes this object.
     *
     * @param connector value of the query provider
     */
    BitbucketRepositoryProvider(final BitbucketConnector connector)
    {
        this.connector = connector;
        descriptors = new WeakHashMap<>();
    }

    /**
     * Returns the descriptor for a repository.
     *
     * @param repository repository
     * @return descriptor
     */
    Descriptor getDescriptor(final BitbucketRepository repository)
    {
        Descriptor value = descriptors.get(repository);
        if (value == null) {
            value = new Descriptor();
            descriptors.put(repository, value);
        }
        return value;
    }

    /**
     * Returns a repository object for a {@link RepositoryInfo} object.
     *
     * @param info {@link RepositoryInfo} object
     * @return repository object
     */
    BitbucketRepository getRepository(final RepositoryInfo info)
    {
        BitbucketRepositoryProxy value = new BitbucketRepositoryProxy();
        if (info != null) {
            String fullName = info.getUrl();
            Matcher m = FULL_NAME_PATTERN.matcher(fullName);
            if (!m.matches()) {
                throw new IllegalArgumentException("Invalid full name");
            }
            value.setRepository(
                connector.getBitbucketClient().getRepository(
                    m.group(1), m.group(2)));

            Descriptor descriptor = getDescriptor(value);
            descriptor.setId(info.getID());
            descriptor.setDisplayName(info.getDisplayName());
            descriptor.setTooltip(info.getTooltip());
        }
        return value;
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
        return descriptor.getController(repository);
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
        return new BitbucketQuery(repository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitbucketIssue createIssue(final BitbucketRepository r)
    {
        // @todo Implement this method.
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
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<BitbucketQuery> getQueries(
        final BitbucketRepository repository)
    {
        BitbucketQueryProvider queryProvider = connector.getQueryProvider();

        List<BitbucketQuery> value = new ArrayList<>();
        value.add(queryProvider.getQuery(repository, "All Tasks"));
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
        return true;
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
        private WeakReference<RepositoryController> controller = null;

        /**
         * Initializes this object.
         */
        Descriptor()
        {
            this.bitbucketClient = new BitbucketClient();
            this.support = new PropertyChangeSupport(this);
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
         * @param repository a repository
         * @return controller object
         */
        public RepositoryController getController(
            final BitbucketRepository repository)
        {
            if (controller == null || controller.get() == null) {
                controller = new WeakReference<>(
                    new BitbucketRepositoryController(repository, this));
            }
            return controller.get();
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
