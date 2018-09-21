/*
 * BitbucketRepository.java
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
import java.util.Set;

/**
 * Repository data for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
final class BitbucketRepository
{
    /**
     * Property change listeners.
     */
    private final Set<PropertyChangeListener> propertyChangeListenerSet;

    /**
     * Constructs this objects.
     */
    public BitbucketRepository()
    {
        propertyChangeListenerSet = new HashSet<>();
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
}
