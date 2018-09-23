/*
 * BitbucketRepository.java - class BitbucketRepository
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;

/**
 * Repository data for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketRepository
{
    /**
     * Identifier of the repository.
     */
    private String id = null;

    /**
     * Full name of the repository.
     */
    private String fullName = null;

    /**
     * Display name of the repository.
     */
    private String displayName = null;

    /**
     * Property change support.
     */
    private PropertyChangeSupport support;

    /**
     * Constructs this objects.
     *
     * @param info repository information
     */
    BitbucketRepository(final RepositoryInfo info)
    {
        support = new PropertyChangeSupport(this);

        if (info != null) {
            id = info.getID();
            fullName = info.getUrl();
            displayName = info.getDisplayName();
        }
    }

    /**
     * Returns the identifier.
     *
     * @return the identifier
     */
    public String getId()
    {
        return id;
    }

    /**
     * Sets the identifier.
     *
     * @param value {@link String} value to which the identifier shall be set
     */
    public void setId(final String value)
    {
        String oldValue = id;
        id = value;
        support.firePropertyChange("id", oldValue, id);
    }

    /**
     * Returns the full name of the repository.
     *
     * @return the full name
     */
    public String getFullName()
    {
        return fullName;
    }

    /**
     * Sets the full name of the repository to a {@link String} value.
     *
     * @param value {@link String} value to which the full name shall be set
     */
    public void setFullName(final String value)
    {
        String oldValue = fullName;
        fullName = value;
        support.firePropertyChange("fullName", oldValue, fullName);
    }

    /**
     * Returns the display name of the repository.
     *
     * @return the display name
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Sets the display name of the repository to a {@link String} value.
     *
     * @param value {@link String} value to which the display name shall be set
     */
    public void setDisplayName(final String value)
    {
        String realValue = value;
        if (realValue == null || "".equals(realValue)) {
            realValue = fullName;
        }

        String oldValue = displayName;
        displayName = realValue;
        support.firePropertyChange("displayName", oldValue, displayName);
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
