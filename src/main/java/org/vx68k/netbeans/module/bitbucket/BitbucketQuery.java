/*
 * BitbucketQuery.java - class BitbucketQuery
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
import org.vx68k.bitbucket.api.BitbucketRepository;

/**
 * Query for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
final class BitbucketQuery
{
    /**
     * Repository where the query searches for issues.
     */
    private BitbucketRepository repository = null;

    /**
     * Sets the repository where the query searches for issues.
     *
     * @param newValue a new value of the repository where the query finds
     * issues
     */
    public void setRepository(final BitbucketRepository newValue)
    {
        repository = newValue;
    }

    /**
     * Returns a {@link Collection} view of the issues found.
     *
     * @return a {@link Collection} view of the issues found
     */
    public Collection<BitbucketIssue> issues()
    {
        // @todo Implement this method.
        return Collections.emptyList();
    }
}
