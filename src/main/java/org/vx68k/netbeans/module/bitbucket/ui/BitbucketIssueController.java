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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.modules.bugtracking.spi.IssueController;
import org.openide.util.HelpCtx;
import org.vx68k.bitbucket.api.BitbucketIssue;
import org.vx68k.netbeans.module.bitbucket.BitbucketIssueProvider;

/**
 * Implementation of {@link IssueController} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketIssueController implements IssueController
{
    /**
     * Number of columns in a text field or text area.
     */
    private static final int COLUMNS = 60;

    /**
     * Number of rows in the description text area.
     */
    private static final int DESCRIPTION_ROWS = 10;

    /**
     * Common preferred width of subcomponents.
     */
    private static final int COMMON_WIDTH = 160;

    /**
     * Length of insets.
     */
    private static final int INSET = 4;

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
     * Label for the heading.
     */
    private JLabel heading = null;

    /**
     * Tool bar for actions.
     */
    private JToolBar actions = null;

    /**
     * Label for the state.
     */
    private JLabel state = null;

    /**
     * Text field for the title.
     */
    private JTextField title = null;

    /**
     * Text area for the description.
     */
    private JTextArea description = null;

    /**
     * Combo box for the kind.
     */
    private JComboBox<String> kind = null;

    /**
     * Combo box for the priority.
     */
    private JComboBox<String> priority = null;

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

        actions = new JToolBar();
        actions.setFloatable(false);
        actions.add(new AbstractAction("Resolve") {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
            }
        }).setEnabled(false);

        state = new JLabel();
        title = new JTextField(COLUMNS);
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
        title.getDocument().addDocumentListener(textChange);
        description.getDocument().addDocumentListener(textChange);

        kind = new JComboBox<>(BitbucketIssueProvider.KINDS);
        kind.setPreferredSize(
            new Dimension(COMMON_WIDTH, kind.getPreferredSize().height));
        priority = new JComboBox<>(BitbucketIssueProvider.PRIORITIES);
        priority.setPreferredSize(
            new Dimension(COMMON_WIDTH, priority.getPreferredSize().height));

        ActionListener toChangeOnAction = (event) -> setChanged(true);
        kind.addActionListener(toChangeOnAction);
        priority.addActionListener(toChangeOnAction);

        // Adding subcomponents to the main panel.

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        c.insets = new Insets(INSET, INSET, INSET, INSET);

        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(heading, c);

        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        panel.add(actions, c);

        c.gridy++;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("State:"), c);
        c.weightx = 1.0;
        panel.add(state, c);

        c.gridy++;
        c.weightx = 0.0;
        panel.add(createLabel("Summary:", 'S', title), c);
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(title, c);

        c.gridy++;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        panel.add(createLabel("Description:", 'D', description), c);
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JScrollPane(description), c);

        c.gridy++;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        panel.add(createLabel("Kind:", 'K', kind), c);
        c.weightx = 1.0;
        panel.add(kind, c);

        c.gridy++;
        c.weightx = 0.0;
        panel.add(createLabel("Priority:", 'P', priority), c);
        c.weightx = 1.0;
        panel.add(priority, c);

        c.gridy++;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
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
        BitbucketIssue issue = issueAdapter.getIssue();
        heading.setText(issueAdapter.getDisplayName());
        state.setText(issue.getState().toUpperCase());
        title.setText(issue.getTitle());
        description.setText(issue.getContent().getRaw());
        kind.setSelectedItem(issue.getKind());
        priority.setSelectedItem(issue.getPriority());
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
            null, "Changes cannot be saved yet.", "Not implemented",
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
