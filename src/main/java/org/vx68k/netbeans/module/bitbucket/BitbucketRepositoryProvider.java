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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.netbeans.modules.bugtracking.spi.RepositoryProvider;
import org.vx68k.netbeans.module.bitbucket.ui.BitbucketRepositoryController;

/**
 *
 * @author Kaz Nishimura
 */
final class BitbucketRepositoryProvider implements
    RepositoryProvider<BitbucketRepository, BitbucketQuery, BitbucketIssue>
{
    /**
     * Identifier of the Bitbucket Cloud connector.
     */
    private final String connectorId;

    /**
     * Map for repository information object.
     */
    private final Map<BitbucketRepository, RepositoryInfo> infoMap;

    /**
     * Controller object.
     */
    private final BitbucketRepositoryController controller;

    /**
     * Constructs this object.
     *
     * @param id identifier of the connector
     */
    BitbucketRepositoryProvider(final String id)
    {
        connectorId = id;
        infoMap = new HashMap<>();
        controller = new BitbucketRepositoryController();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepositoryInfo getInfo(final BitbucketRepository repository)
    {
        if (!infoMap.containsKey(repository)) {
            String fullName = repository.getFullName();
            String displayName = repository.getDisplayName();
            infoMap.put(repository, new RepositoryInfo(
                fullName, connectorId, null, displayName, fullName));
        }
        return infoMap.get(repository);
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
    public void removed(final BitbucketRepository repository)
    {
        infoMap.remove(repository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepositoryController getController(
        final BitbucketRepository repository)
    {
        controller.setRepository(repository);
        return controller;
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
    public BitbucketQuery createQuery(final BitbucketRepository r)
    {
        return new BitbucketQuery();
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
    public Collection<BitbucketQuery> getQueries(final BitbucketRepository r)
    {
        return Collections.emptyList();
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
        repository.addPropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePropertyChangeListener(
        final BitbucketRepository repository,
        final PropertyChangeListener listener)
    {
        repository.removePropertyChangeListener(listener);
    }
}
