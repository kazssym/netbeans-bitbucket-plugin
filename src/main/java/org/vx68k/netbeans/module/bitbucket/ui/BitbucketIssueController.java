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

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
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
    private static final int INSET = 5;

    /**
     * Issue adapter.
     */
    private final BitbucketIssueProvider.Adapter issueAdapter;

    /**
     * Issue viewer.
     */
    private final Viewer viewer;

    /**
     * Issue editor.
     */
    private final Editor editor;

    /**
     * Property change support object.
     */
    private final PropertyChangeSupport support;

    /**
     * Component for the heading.
     */
    private final JLabel heading = new JLabel();

    /**
     * Component for the state.
     */
    private final JLabel state = new JLabel();

    /**
     * Root component.
     */
    private final JScrollPane component = new JScrollPane();

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
        this.viewer = new Viewer();
        this.editor = new Editor();
        this.support = new PropertyChangeSupport(this);

        initComponent();
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
     * Initializes the root component.
     */
    private void initComponent()
    {
        component.setBorder(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        Font baseFont = panel.getFont();
        heading.setFont(baseFont.deriveFont(2.0F * baseFont.getSize2D()));
        state.setForeground(Color.WHITE);
        state.setFont(baseFont.deriveFont(Font.BOLD));

        Box actionBox = new Box(BoxLayout.LINE_AXIS);
        actionBox.setBorder(
            BorderFactory.createEmptyBorder(INSET, INSET, INSET, INSET));
        actionBox.setOpaque(true);
        actionBox.setBackground(Color.GRAY);
        actionBox.add(state);

        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.add(viewer.getComponent());
        cardPanel.add(editor.getComponent());

        // Adding subcomponents to the main panel.

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        c.insets = new Insets(INSET, INSET, INSET, INSET);

        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        panel.add(heading, c);
        c.gridwidth = 1;

        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 0);
        panel.add(actionBox, c);

        c.weighty = 1.0;
        panel.add(cardPanel, c);

        panel.setMinimumSize(panel.getPreferredSize());

        component.setViewportView(panel);
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
        heading.setText(String.format(
            "Issue #%d: %s", issue.getId(), issue.getTitle()));
        state.setText(issue.getState().toUpperCase());
        viewer.update(issue);
        editor.update(issue);
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

    /**
     * Issue viewer.
     */
    protected final class Viewer
    {
        /**
         * Component for the reporter.
         */
        private final JLabel reporter = new JLabel();

        /**
         * Component for the title.
         */
        private final JLabel title = new JLabel();

        /**
         * Component for the description.
         */
        private final JLabel description = new JLabel();

        /**
         * Root component.
         */
        private final JPanel component = new JPanel(new GridBagLayout());

        /**
         * Initializes the object.
         */
        public Viewer()
        {
            initComponent();
        }

        /**
         * Initializes the root component.
         */
        private void initComponent()
        {
            component.setBackground(Color.WHITE);

            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.BASELINE_LEADING;
            c.insets = new Insets(INSET, INSET, INSET, INSET);

            c.gridx = 0;
            c.gridwidth = 1;
            c.weightx = 0.0;
            component.add(new JLabel("Reported by:"), c);
            c.gridx++;
            c.weightx = 1.0;
            component.add(reporter, c);

            c.gridx = 0;
            c.gridwidth = 1;
            c.weightx = 0.0;
            component.add(new JLabel("Title:"), c);
            c.gridx++;
            c.weightx = 1.0;
            component.add(title, c);

            c.gridx = 0;
            c.gridwidth = 2;
            c.weightx = 1.0;
            component.add(description, c);

            c.gridx = 0;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weighty = 1.0;
            c.fill = GridBagConstraints.BOTH;
            component.add(new JLabel(), c);
        }

        /**
         * Returns the root component of the viewer.
         *
         * @return the root component of the viewer
         */
        public JComponent getComponent()
        {
            return component;
        }

        /**
         * Updates the contents.
         *
         * @param issue an issue
         */
        public void update(final BitbucketIssue issue)
        {
            title.setText(issue.getTitle());
            reporter.setText(issue.getReporter().getDisplayName());
            description.setText(
                "<html>" + issue.getContent().getHtml() + "</html>");
        }
    }

    /**
     * Issue editor.
     */
    protected final class Editor
    {
        /**
         * Component for the title.
         */
        private final JTextField title = new JTextField();

        /**
         * Component for the description.
         */
        private final JTextArea description =
            new JTextArea(DESCRIPTION_ROWS, 0);

        /**
         * Component for the kind.
         */
        private final JComboBox<String> kind =
            new JComboBox<>(BitbucketIssueProvider.KINDS);

        /**
         * Component for the priority.
         */
        private final JComboBox<String> priority =
            new JComboBox<>(BitbucketIssueProvider.PRIORITIES);

        /**
         * Root component.
         */
        private final JPanel component = new JPanel(new GridBagLayout());

        /**
         * Initializes the object.
         */
        public Editor()
        {
            title.setPreferredSize(new Dimension(
                COMMON_WIDTH, title.getPreferredSize().height));
            description.setPreferredSize(new Dimension(
                COMMON_WIDTH, description.getPreferredSize().height));
            kind.setPreferredSize(new Dimension(
                COMMON_WIDTH, kind.getPreferredSize().height));
            priority.setPreferredSize(new Dimension(
                COMMON_WIDTH, priority.getPreferredSize().height));

            DocumentListener changeOnDocument = new DocumentListener() {
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
            title.getDocument().addDocumentListener(changeOnDocument);
            description.getDocument().addDocumentListener(changeOnDocument);

            ActionListener changeOnAction = (event) -> setChanged(true);
            kind.addActionListener(changeOnAction);
            priority.addActionListener(changeOnAction);

            initComponent();
        }

        /**
         * Initializes the root component.
         */
        private void initComponent()
        {
            component.setBackground(Color.WHITE);

            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.BASELINE_LEADING;
            c.insets = new Insets(INSET, INSET, INSET, INSET);

            c.gridx = 0;
            c.weightx = 0.0;
            component.add(createLabel("Title:", 'T', title), c);
            c.gridx++;
            c.weightx = 1.0;
            c.fill = GridBagConstraints.HORIZONTAL;
            component.add(title, c);

            c.gridx = 0;
            c.weightx = 0.0;
            c.fill = GridBagConstraints.NONE;
            component.add(createLabel("Description:", 'D', description), c);
            c.gridx++;
            c.weightx = 1.0;
            c.fill = GridBagConstraints.HORIZONTAL;
            component.add(new JScrollPane(description), c);
            c.fill = GridBagConstraints.NONE;

            c.gridx = 0;
            c.weightx = 0.0;
            component.add(createLabel("Kind:", 'K', kind), c);
            c.gridx++;
            c.weightx = 1.0;
            component.add(kind, c);

            c.gridx = 0;
            c.weightx = 0.0;
            component.add(createLabel("Priority:", 'P', priority), c);
            c.gridx++;
            c.weightx = 1.0;
            component.add(priority, c);

            c.gridx = 0;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weightx = 1.0;
            c.weighty = 1.0;
            component.add(new JPanel(), c);
        }

        /**
         * Returns the root component of the editor.
         *
         * @return the root component of the editor
         */
        public JComponent getComponent()
        {
            return component;
        }

        /**
         * Updates the contents.
         *
         * @param issue an issue
         */
        public void update(final BitbucketIssue issue)
        {
            title.setText(issue.getTitle());
            description.setText(issue.getContent().getRaw());
            kind.setSelectedItem(issue.getKind());
            priority.setSelectedItem(issue.getPriority());
        }
    }
}
