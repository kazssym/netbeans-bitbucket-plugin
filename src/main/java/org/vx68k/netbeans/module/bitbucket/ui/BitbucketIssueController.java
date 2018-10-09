/*
 * BitbucketIssueController.java
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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.netbeans.modules.bugtracking.spi.IssueController;
import org.openide.util.HelpCtx;
import org.vx68k.netbeans.module.bitbucket.BitbucketIssueProvider;

/**
 * Implementation of {@link IssueController} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketIssueController implements IssueController
{
    /**
     * Length of insets.
     */
    private static final int INSET = 4;

    /**
     * Number of columns in a text field or text area.
     */
    private static final int TEXT_COLUMNS = 60;

    /**
     * Number of rows in a text area.
     */
    private static final int TEXT_ROWS = 20;

    /**
     * Issue descriptor.
     */
    private final BitbucketIssueProvider.Descriptor descriptor;

    /**
     * Property change support object.
     */
    private final PropertyChangeSupport support;

    /**
     * Base visual component.
     */
    private JComponent component = null;

    /**
     * Heading.
     */
    private JLabel heading = null;

    /**
     * Text field for the summary.
     */
    private JTextField summaryField = null;

    /**
     * Text area for the description.
     */
    private JTextArea descriptionArea = null;

    /**
     * Initializes the object.
     *
     * @param descriptor a Bitbucket Cloud issue
     */
    public BitbucketIssueController(
        final BitbucketIssueProvider.Descriptor descriptor)
    {
        this.descriptor = descriptor;
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Initializes the visual components.
     */
    private void initComponents()
    {
        component = new JPanel(new GridBagLayout());
        component.setBackground(Color.WHITE);

        Font f = component.getFont();
        heading = new JLabel(descriptor.getDisplayName());
        heading.setFont(f.deriveFont(2.0F * f.getSize2D()));

        summaryField = new JTextField(descriptor.getSummary(), TEXT_COLUMNS);
        summaryField.setEditable(false);

        descriptionArea = new JTextArea("Sample", TEXT_ROWS, TEXT_COLUMNS);
        descriptionArea.setEditable(false);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        c.insets = new Insets(INSET, INSET, INSET, INSET);

        JLabel summaryLabel = new JLabel("Summary:");
        summaryLabel.setLabelFor(summaryField);
        summaryLabel.setDisplayedMnemonic('S');

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setLabelFor(descriptionArea);
        descriptionLabel.setDisplayedMnemonic('D');

        c.gridy = 0;
        c.gridwidth = 2;
        c.weighty = 0.0;
        c.weightx = 0.0;
        component.add(heading, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        component.add(new JLabel(), c);

        c.gridy++;
        c.gridwidth = 1;
        c.weightx = 0.0;
        component.add(summaryLabel, c);
        component.add(summaryField, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        component.add(new JLabel(), c);

        c.gridy++;
        c.gridwidth = 1;
        c.weightx = 0.0;
        component.add(descriptionLabel, c);
        component.add(new JScrollPane(descriptionArea), c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        component.add(new JLabel(), c);

        c.gridy++;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.weighty = 1.0;
        component.add(new JLabel(), c);

        component.setMinimumSize(component.getPreferredSize());
    }

    @Override
    public JComponent getComponent()
    {
        if (component == null) {
            initComponents();
        }

        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(null);
        return scrollPane;
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
    public boolean saveChanges()
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
