/*
 * BitbucketRepositoryController.java - class BitbucketRepositoryController
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.openide.util.HelpCtx;

/**
 * Implementation of {@link RepositoryController} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketRepositoryController
    implements RepositoryController
{
    /**
     * Layout of the visual component.
     */
    private final GridBagLayout layout;

    /**
     * Visual component.
     */
    private final JPanel component;

    /**
     * Change listeners.
     */
    private final Set<ChangeListener> changeListenerSet;

    /**
     * Constructs this object.
     */
    public BitbucketRepositoryController()
    {
        layout = new GridBagLayout();
        component = new JPanel(layout);
        changeListenerSet = new HashSet<>();
    }

    /**
     * Fires a change event.
     */
    protected void fireChange()
    {
        final ChangeEvent event = new ChangeEvent(this);
        changeListenerSet.forEach((listener) -> {
            listener.stateChanged(event);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getComponent()
    {
        return component;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populate()
    {
        component.removeAll();
        component.add(
            new JLabel("Hello, NetBeans!"), GridBagConstraints.RELATIVE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage()
    {
        return null;
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
    public void addChangeListener(final ChangeListener listener)
    {
        changeListenerSet.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeChangeListener(final ChangeListener listener)
    {
        changeListenerSet.remove(listener);
    }
}
