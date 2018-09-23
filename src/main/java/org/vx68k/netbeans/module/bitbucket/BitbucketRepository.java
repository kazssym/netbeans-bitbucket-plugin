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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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
    private final String id;

    /**
     * Full name of the repository.
     */
    private String fullName = null;

    /**
     * Display name of the repository.
     */
    private String displayName = null;

    /**
     * Property change listeners.
     */
    private final Set<PropertyChangeListener> propertyChangeListenerSet;

    /**
     * Constructs this objects.
     */
    BitbucketRepository()
    {
        this(null);
    }

    /**
     * Constructs this objects.
     *
     * @param info repository information
     */
    BitbucketRepository(final RepositoryInfo info)
    {
        propertyChangeListenerSet = new HashSet<>();
        if (info != null) {
            id = info.getID();
            fullName = info.getUrl();
            displayName = info.getDisplayName();
        }
        else {
            id = UUID.randomUUID().toString();
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
        if (!Objects.equals(fullName, value)) {
            firePropertyChange("fullName", fullName, value);
            fullName = value;
        }
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

        if (!Objects.equals(displayName, realValue)) {
            firePropertyChange("displayName", displayName, realValue);
            displayName = realValue;
        }
    }

    /**
     * Fires a property change event.
     *
     * @param name name of the property whose value is being changed
     * @param oldValue old value of the property
     * @param newValue new value of the property
     */
    protected void firePropertyChange(
        final String name, final Object oldValue, final Object newValue)
    {
        final PropertyChangeEvent event =
            new PropertyChangeEvent(this, name, oldValue, newValue);
        propertyChangeListenerSet.forEach((listener) -> {
            listener.propertyChange(event);
        });
    }

    /**
     * Adds a property change listener.
     *
     * @param listener property change listener to add
     */
    public void addPropertyChangeListener(
        final PropertyChangeListener listener)
    {
        propertyChangeListenerSet.add(listener);
    }

    /**
     * Removes a property change listener.
     *
     * @param listener property change listener to remove
     */
    public void removePropertyChangeListener(
        final PropertyChangeListener listener)
    {
        propertyChangeListenerSet.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        int value = getClass().hashCode();
        value ^= Objects.hashCode(id);
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object)
    {
        if (this != object) {
            if (object == null || object.getClass() != getClass()) {
                return false;
            }

            BitbucketRepository other = (BitbucketRepository) object;
            if (!Objects.equals(id, other.id)) {
                return false;
            }
        }
        return true;
    }
}
