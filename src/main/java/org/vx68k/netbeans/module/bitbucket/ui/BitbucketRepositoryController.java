/*
 * BitbucketRepositoryController.java
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

package org.vx68k.netbeans.module.bitbucket.ui;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.openide.util.HelpCtx;

/**
 * {@link RepositoryController} implementation for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public class BitbucketRepositoryController implements RepositoryController
{
    /**
     * Change listeners.
     */
    private final Set<ChangeListener> changeListenerSet;

    /**
     * Fires a change event.
     *
     * @param event change event to fire
     */
    protected void fire(final ChangeEvent event)
    {
        changeListenerSet.forEach((listener) -> {
            listener.stateChanged(event);
        });
    }

    /**
     * Constructs this object.
     */
    public BitbucketRepositoryController()
    {
        changeListenerSet = new HashSet<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getComponent()
    {
        return new JPanel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HelpCtx getHelpCtx()
    {
        return null; // @todo Check the interface.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populate()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage()
    {
        return "Not implemented.";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyChanges()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelChanges()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addChangeListener(ChangeListener listener)
    {
        changeListenerSet.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeChangeListener(ChangeListener listener)
    {
        changeListenerSet.remove(listener);
    }
}
