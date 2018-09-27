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
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.netbeans.modules.bugtracking.spi.RepositoryProvider;
import org.vx68k.bitbucket.api.BitbucketRepository;
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
     * Identifier of the Bitbucket Cloud connector.
     */
    private final String connectorId;

    /**
     * Map for descriptors.
     */
    private final Map<BitbucketRepository, Descriptor> descriptors;

    /**
     * Initializes this object.
     *
     * @param connectorIdValue value of the identifier of the Bitbucket Cloud
     * connector
     */
    BitbucketRepositoryProvider(final String connectorIdValue)
    {
        connectorId = connectorIdValue;
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
        if (!descriptors.containsKey(repository)) {
            descriptors.put(repository, new Descriptor());
        }
        return descriptors.get(repository);
    }

    /**
     * Returns a repository object for a {@link RepositoryInfo} object.
     *
     * @param info {@link RepositoryInfo} object
     * @return repository object
     */
    BitbucketRepository getRepository(final RepositoryInfo info)
    {
        BitbucketRepository repository = new BitbucketRepositoryProxy();
        if (info != null) {
            Descriptor descriptor = getDescriptor(repository);
            descriptor.setId(info.getID());
            descriptor.setFullName(info.getUrl());
            descriptor.setDisplayName(info.getDisplayName());
        }
        return repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepositoryInfo getInfo(final BitbucketRepository repository)
    {
        RepositoryInfo value = null;
        Descriptor descriptor = getDescriptor(repository);
        if (descriptor.getId() != null) {
            value = new RepositoryInfo(
                descriptor.getId(), connectorId, descriptor.getFullName(),
                descriptor.getDisplayName(), descriptor.getFullName());
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
        return new BitbucketQuery(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitbucketIssue createIssue(final BitbucketRepository r)
    {
        return new BitbucketIssue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitbucketIssue createIssue(
        final BitbucketRepository r, final String string, final String string1)
    {
        return new BitbucketIssue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<BitbucketQuery> getQueries(
        final BitbucketRepository repository)
    {
        return Collections.singletonList(new BitbucketQuery("All Tasks"));
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
    public final class Descriptor
    {
        /**
         * Property change support object.
         */
        private final PropertyChangeSupport support;

        /**
         * Controller object.
         */
        private BitbucketRepositoryController controller = null;

        /**
         * Identifier for the repository.
         */
        private String id = null;

        /**
         * Full name for the repository.
         */
        private String fullName = null;

        /**
         * Display name for the repository.
         */
        private String displayName = null;

        /**
         * Initializes this object.
         */
        Descriptor()
        {
            support = new PropertyChangeSupport(this);
        }

        /**
         * Returns the controller object for the repository.
         *
         * @return controller object
         */
        public BitbucketRepositoryController getController()
        {
            if (controller == null) {
                controller = new BitbucketRepositoryController(this);
            }
            return controller;
        }

        /**
         * Returns the identifier for the repository.
         *
         * @return identifier
         */
        public String getId()
        {
            return id;
        }

        /**
         * Sets the identifier for the repository to a {@link String} value.
         *
         * @param newValue new value of the identifier
         */
        public void setId(final String newValue)
        {
            String oldValue = id;
            id = newValue;
            support.firePropertyChange("id", oldValue, newValue);
        }

        /**
         * Returns the full name for the repository.
         *
         * @return full name
         */
        public String getFullName()
        {
            return fullName;
        }

        /**
         * Sets the full name for the repository to a {@link String} value.
         *
         * @param newValue new value of the full name
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
         * @return display name
         */
        public String getDisplayName()
        {
            return displayName;
        }

        /**
         * Sets the display name for the repository to a {@link String} value.
         *
         * @param newValue new value of the display name
         */
        public void setDisplayName(final String newValue)
        {
            String oldValue = displayName;
            displayName = newValue;
            support.firePropertyChange("displayName", oldValue, newValue);
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
