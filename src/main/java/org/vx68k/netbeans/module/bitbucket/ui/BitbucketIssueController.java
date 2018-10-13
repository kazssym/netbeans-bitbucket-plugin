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
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private static final int COLUMNS = 60;

    /**
     * Number of rows in the description text area.
     */
    private static final int DESCRIPTION_ROWS = 20;

    /**
     * Issue adapter.
     */
    private final BitbucketIssueProvider.Adapter issueAdapter;

    /**
     * Property change support object.
     */
    private final PropertyChangeSupport support;

    /**
     * Visual component.
     */
    private JComponent component = null;

    /**
     * Heading.
     */
    private JLabel heading = null;

    /**
     * Text field for the summary.
     */
    private JTextField summary = null;

    /**
     * Text area for the description.
     */
    private JTextArea description = null;

    /**
     * {@code true} if and only if any property was changed.
     */
    private boolean changed = false;

    /**
     * Initializes the object.
     *
     * @param issueAdapter an issue adapter
     */
    public BitbucketIssueController(
        final BitbucketIssueProvider.Adapter issueAdapter)
    {
        this.issueAdapter = issueAdapter;
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Creates a new label for a component.
     *
     * @param text a label text
     * @param mnemonic a mnemonic character
     * @param forComponent a component for which a new label is
     * @return a new label.
     */
    protected static JLabel createLabel(
        final String text, final char mnemonic, final Component forComponent)
    {
        JLabel label = new JLabel(text);
        label.setDisplayedMnemonic(mnemonic);
        label.setLabelFor(forComponent);
        return label;
    }

    /**
     * Initializes the visual components.
     */
    private void initComponents()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        component = new JScrollPane(panel);
        component.setBorder(null);

        Font panelFont = panel.getFont();

        heading = new JLabel();
        heading.setFont(panelFont.deriveFont(2.0F * panelFont.getSize2D()));

        summary = new JTextField(COLUMNS);
        description = new JTextArea(DESCRIPTION_ROWS, COLUMNS);

        DocumentListener textChange = new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent event)
            {
                setChanged(true);
            }

            @Override
            public void removeUpdate(final DocumentEvent event)
            {
                setChanged(true);
            }

            @Override
            public void changedUpdate(final DocumentEvent event)
            {
            }
        };
        summary.getDocument().addDocumentListener(textChange);
        description.getDocument().addDocumentListener(textChange);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        c.insets = new Insets(INSET, INSET, INSET, INSET);

        c.gridy = 0;
        c.weighty = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(heading, c);

        c.gridy++;
        c.gridwidth = 1;
        c.weightx = 0.0;
        panel.add(createLabel("Summary:", 'S', summary), c);
        panel.add(summary, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        panel.add(new JLabel(), c);

        c.gridy++;
        c.gridwidth = 1;
        c.weightx = 0.0;
        panel.add(createLabel("Description:", 'D', description), c);
        panel.add(new JScrollPane(description), c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        panel.add(new JLabel(), c);

        c.gridy++;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.weighty = 1.0;
        panel.add(new JLabel(), c);

        panel.setMinimumSize(panel.getPreferredSize());
    }

    /**
     * Sets the value which indicate some change.
     *
     * @param newValue new value
     */
    protected void setChanged(final boolean newValue)
    {
        boolean oldValue = changed;
        changed = newValue;
        support.firePropertyChange("changed", oldValue, newValue);
        support.firePropertyChange(PROP_CHANGED, oldValue, newValue);
    }

    @Override
    public JComponent getComponent()
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
        heading.setText(issueAdapter.getDisplayName());
        summary.setText(issueAdapter.getSummary());
        description.setText(issueAdapter.getDescription());
        setChanged(false);
    }

    @Override
    public void closed()
    {
        issueAdapter.resetController();
    }

    @Override
    public boolean saveChanges()
    {
        JOptionPane.showMessageDialog(
            null, "Your changes cannot be saved yet.", "Not implemented",
            JOptionPane.INFORMATION_MESSAGE);
        return false;
    }

    @Override
    public boolean discardUnsavedChanges()
    {
        return true;
    }

    @Override
    public boolean isChanged()
    {
        return changed;
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener l)
    {
        support.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener l)
    {
        support.removePropertyChangeListener(l);
    }
}
