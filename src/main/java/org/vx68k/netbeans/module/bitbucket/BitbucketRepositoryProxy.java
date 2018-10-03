/*
 * BitbucketRepositoryProxy.java
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

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.vx68k.bitbucket.api.BitbucketAccount;
import org.vx68k.bitbucket.api.BitbucketBranch;
import org.vx68k.bitbucket.api.BitbucketIssue;
import org.vx68k.bitbucket.api.BitbucketRepository;

/**
 * Repository proxy for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketRepositoryProxy implements BitbucketRepository
{
    /**
     * Destination repository.
     */
    private BitbucketRepository repository = null;

    /**
     * Initializes the object.
     */
    BitbucketRepositoryProxy()
    {
    }

    /**
     * Initializes the object and sets the destination repository.
     *
     * @param repository the destination repository
     */
    BitbucketRepositoryProxy(final BitbucketRepository repository)
    {
        setRepository(repository);
    }

    /**
     * Sets the destination repository.
     *
     * @param newValue new value of the destination repository
     */
    public void setRepository(final BitbucketRepository newValue)
    {
        repository = newValue;
    }

    @Override
    public String getSCM()
    {
        String value = null;
        if (repository != null) {
            value = repository.getSCM();
        }
        return value;
    }

    @Override
    public BitbucketAccount getOwner()
    {
        BitbucketAccount value = null;
        if (repository != null) {
            value = repository.getOwner();
        }
        return value;
    }

    @Override
    public String getName()
    {
        String value = null;
        if (repository != null) {
            value = repository.getName();
        }
        return value;
    }

    @Override
    public UUID getUUID()
    {
        UUID value = null;
        if (repository != null) {
            value = repository.getUUID();
        }
        return value;
    }

    @Override
    public String getFullName()
    {
        String value = null;
        if (repository != null) {
            value = repository.getFullName();
        }
        return value;
    }

    @Override
    public String getDescription()
    {
        String value = null;
        if (repository != null) {
            value = repository.getDescription();
        }
        return value;
    }

    @Override
    public BitbucketBranch getMainBranch()
    {
        BitbucketBranch value = null;
        if (repository != null) {
            value = repository.getMainBranch();
        }
        return value;
    }

    @Override
    public boolean isPrivate()
    {
        boolean value = false;
        if (repository != null) {
            value = repository.isPrivate();
        }
        return value;
    }

    @Override
    public Instant getCreated()
    {
        Instant value = null;
        if (repository != null) {
            value = repository.getCreated();
        }
        return value;
    }

    @Override
    public Instant getUpdated()
    {
        Instant value = null;
        if (repository != null) {
            value = repository.getUpdated();
        }
        return value;
    }

    @Override
    public BitbucketIssue getIssue(final int id)
    {
        BitbucketIssue value = null;
        if (repository != null) {
            value = repository.getIssue(id);
        }
        return value;
    }

    @Override
    public Collection<BitbucketIssue> issues()
    {
        Collection<BitbucketIssue> value = Collections.emptyList();
        if (repository != null) {
            value = repository.issues();
        }
        return value;
    }
}
