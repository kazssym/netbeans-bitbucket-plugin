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
public class BitbucketRepositoryProxy implements BitbucketRepository
{
    /**
     * Real repository.
     */
    private BitbucketRepository realRepository;

    /**
     * Initializes the object.
     */
    public BitbucketRepositoryProxy()
    {
        this(null);
    }

    /**
     * Initializes the object with an initial value of the real repository.
     *
     * @param realRepository an initial value of the real repository
     */
    public BitbucketRepositoryProxy(final BitbucketRepository realRepository)
    {
        this.realRepository = realRepository;
    }

    /**
     * Sets the real repository.
     *
     * @param newValue a new value of the real repository
     */
    public final void setRealRepository(final BitbucketRepository newValue)
    {
        realRepository = newValue;
    }

    @Override
    public final String getSCM()
    {
        String value = null;
        if (realRepository != null) {
            value = realRepository.getSCM();
        }
        return value;
    }

    @Override
    public final BitbucketAccount getOwner()
    {
        BitbucketAccount value = null;
        if (realRepository != null) {
            value = realRepository.getOwner();
        }
        return value;
    }

    @Override
    public final String getName()
    {
        String value = null;
        if (realRepository != null) {
            value = realRepository.getName();
        }
        return value;
    }

    @Override
    public final UUID getUUID()
    {
        UUID value = null;
        if (realRepository != null) {
            value = realRepository.getUUID();
        }
        return value;
    }

    @Override
    public final String getFullName()
    {
        String value = null;
        if (realRepository != null) {
            value = realRepository.getFullName();
        }
        return value;
    }

    @Override
    public final String getDescription()
    {
        String value = null;
        if (realRepository != null) {
            value = realRepository.getDescription();
        }
        return value;
    }

    @Override
    public final BitbucketBranch getMainBranch()
    {
        BitbucketBranch value = null;
        if (realRepository != null) {
            value = realRepository.getMainBranch();
        }
        return value;
    }

    @Override
    public final boolean isPrivate()
    {
        boolean value = false;
        if (realRepository != null) {
            value = realRepository.isPrivate();
        }
        return value;
    }

    @Override
    public final Instant getCreated()
    {
        Instant value = null;
        if (realRepository != null) {
            value = realRepository.getCreated();
        }
        return value;
    }

    @Override
    public final Instant getUpdated()
    {
        Instant value = null;
        if (realRepository != null) {
            value = realRepository.getUpdated();
        }
        return value;
    }

    @Override
    public final BitbucketIssue getIssue(final int id)
    {
        BitbucketIssue value = null;
        if (realRepository != null) {
            value = realRepository.getIssue(id);
        }
        return value;
    }

    @Override
    public final Collection<BitbucketIssue> issues()
    {
        Collection<BitbucketIssue> value = Collections.emptyList();
        if (realRepository != null) {
            value = realRepository.issues();
        }
        return value;
    }

    @Override
    public final Collection<BitbucketIssue> issues(final String filter)
    {
        Collection<BitbucketIssue> value = Collections.emptyList();
        if (realRepository != null) {
            value = realRepository.issues(filter);
        }
        return value;
    }
}
