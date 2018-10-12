/*
 * BitbucketQueryController.java
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

import java.awt.GridBagLayout;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.modules.bugtracking.spi.QueryController;
import org.openide.util.HelpCtx;

/**
 * Implementation of {@link QueryController} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketQueryController implements QueryController
{
    /**
     * Property change support.
     */
    private final PropertyChangeSupport support;

    /**
     * Visual component.
     */
    private JPanel component = null;

    /**
     * Initializes the object.
     */
    public BitbucketQueryController()
    {
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Initializes visual components.
     */
    private void initComponents()
    {
        component = new JPanel(new GridBagLayout());

        component.add(new JLabel("Not implemented."));

        component.setMinimumSize(component.getPreferredSize());
    }

    @Override
    public boolean providesMode(final QueryMode mode)
    {
        return false;
    }

    @Override
    public JComponent getComponent(final QueryMode mode)
    {
        if (component == null) {
            initComponents();
        }
        return component;
    }

    @Override
    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void opened()
    {
    }

    @Override
    public void closed()
    {
    }

    @Override
    public boolean saveChanges(final String name)
    {
        return false;
    }

    @Override
    public boolean discardUnsavedChanges()
    {
        return false;
    }

    @Override
    public boolean isChanged()
    {
        return false;
    }

    @Override
    public void addPropertyChangeListener(
        final PropertyChangeListener listener)
    {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(
        final PropertyChangeListener listener)
    {
        support.removePropertyChangeListener(listener);
    }
}
