/*
 * BitbucketIssueTrackerProxy.java
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

import java.util.Collection;
import java.util.Collections;
import org.vx68k.bitbucket.api.BitbucketIssue;
import org.vx68k.bitbucket.api.BitbucketIssueTracker;
import org.vx68k.bitbucket.api.BitbucketRepository;

/**
 * Issue tracker proxy for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public class BitbucketIssueTrackerProxy implements BitbucketIssueTracker
{
    /**
     * Target issue tracker.
     */
    private BitbucketIssueTracker target;

    /**
     * Initializes the object.
     */
    public BitbucketIssueTrackerProxy()
    {
        this(null);
    }

    /**
     * Initializes the object with an initial value of the target issue
     * tracker.
     *
     * @param target an initial value of the target issue tracker
     */
    public BitbucketIssueTrackerProxy(final BitbucketIssueTracker target)
    {
        this.target = target;
    }

    /**
     * Sets the target issue tracker.
     *
     * @param newValue a new value of the target issue tracker
     */
    public final void setTarget(final BitbucketIssueTracker newValue)
    {
        target = newValue;
    }

    @Override
    public final BitbucketIssue getIssue(final int id)
    {
        BitbucketIssue value = null;
        if (target != null) {
            value = target.getIssue(id);
        }
        return value;
    }

    @Override
    public final Collection<BitbucketIssue> issues()
    {
        Collection<BitbucketIssue> value = Collections.emptyList();
        if (target != null) {
            value = target.issues();
        }
        return value;
    }

    @Override
    public final Collection<BitbucketIssue> issues(final String filter)
    {
        Collection<BitbucketIssue> value = Collections.emptyList();
        if (target != null) {
            value = target.issues(filter);
        }
        return value;
    }

    @Override
    public BitbucketRepository getRepository()
    {
        BitbucketRepository value = null;
        if (target != null) {
            value = target.getRepository();
        }
        return value;
    }
}
