/*
 * BitbucketRepositoryProxy.java - class BitbucketRepositoryProxy
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
import java.util.Iterator;
import java.util.UUID;
import org.vx68k.bitbucket.api.BitbucketAccount;
import org.vx68k.bitbucket.api.BitbucketBranch;
import org.vx68k.bitbucket.api.BitbucketIssue;
import org.vx68k.bitbucket.api.BitbucketRepository;

/**
 * Proxy for a Bitbucket Cloud repository.
 *
 * @author Kaz Nishimura
 */
final class BitbucketRepositoryProxy implements BitbucketRepository
{
    /**
     * Destination repository.
     */
    private BitbucketRepository repository = null;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BitbucketAccount getOwner()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UUID getUUID()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getFullName()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDescription()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BitbucketBranch getMainBranch()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isPrivate()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Instant getCreated()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Instant getUpdated()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<BitbucketIssue> getIssues()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
