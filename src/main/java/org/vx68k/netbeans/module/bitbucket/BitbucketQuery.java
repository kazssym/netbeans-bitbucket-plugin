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

/**
 * Query descriptor for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
final class BitbucketQuery
{
    /**
     * Display name of the query.
     */
    private String displayName;

    /**
     * Constructs this object.
     *
     * @param displayNameValue initial value for the display name
     */
    BitbucketQuery(final String displayNameValue)
    {
        displayName = displayNameValue;
    }

    /**
     * Returns the display name of the query.
     *
     * @return the display name
     */
    String getDisplayName()
    {
        return displayName;
    }

    /**
     * Sets the display name of the query to a {@link String} value.
     *
     * @param newValue new value for the display name
     */
    void setDisplayName(final String newValue)
    {
        displayName = newValue;
    }
}
