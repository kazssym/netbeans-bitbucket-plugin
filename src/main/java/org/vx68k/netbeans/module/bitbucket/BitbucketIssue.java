/*
 * BitbucketIssue.java
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Issue descriptor for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketIssue
{
    /**
     * Support object for {@link PropertyChangeListener}s.
     */
    private final PropertyChangeSupport support;

    /**
     * Construct this object.
     */
    BitbucketIssue()
    {
        support = new PropertyChangeSupport(this);
    }

    /**
     * Adds a listener for {@link java.beans.PropertyChangeEvent}s.
     *
     * @param listener {@link PropertyChangeListener} object to add
     */
    public void addPropertyChangeListener(
        final PropertyChangeListener listener)
    {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a listener for {@link java.beans.PropertyChangeEvent}s.
     *
     * @param listener {@link PropertyChangeListener} object to remove
     */
    public void removePropertyChangeListener(
        final PropertyChangeListener listener)
    {
        support.removePropertyChangeListener(listener);
    }
}
