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

import static org.vx68k.netbeans.module.bitbucket.BitbucketRepositoryProvider
    .FULL_NAME_PATTERN;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.openide.util.HelpCtx;
import org.vx68k.bitbucket.api.BitbucketRepository;
import org.vx68k.bitbucket.api.client.BitbucketClient;
import org.vx68k.netbeans.module.bitbucket.BitbucketRepositoryProvider;
import org.vx68k.netbeans.module.bitbucket.BitbucketRepositoryProxy;

/**
 * Implementation of {@link RepositoryController} for Bitbucket Cloud.
 *
 * @author Kaz Nishimura
 */
public final class BitbucketRepositoryController implements
    RepositoryController
{
    /**
     * Number of columns in text fields.
     */
    private static final int TEXT_COLUMNS = 20;

    /**
     * Bitbucket Cloud repository.
     */
    private final BitbucketRepository repository;

    /**
     * Repository to apply changes.
     */
    private final BitbucketRepositoryProvider.Descriptor descriptor;

    /**
     * Visual component.
     */
    private final JComponent component;

    /**
     * Text field for the full name.
     */
    private final JTextComponent fullNameField;

    /**
     * Text field for the display name.
     */
    private final JTextComponent displayNameField;

    /**
     * Error message to show if values are not valid.
     */
    private String errorMessage = null;

    /**
     * Change listeners.
     */
    private final Set<ChangeListener> changeListenerSet;

    /**
     * Initializes the object.
     *
     * @param repository a repository
     * @param descriptor a repository descriptor
     */
    public BitbucketRepositoryController(
        final BitbucketRepository repository,
        final BitbucketRepositoryProvider.Descriptor descriptor)
    {
        this.repository = repository;
        this.descriptor = descriptor;
        component = new JPanel(new GridBagLayout());
        fullNameField = new JTextField(TEXT_COLUMNS);
        displayNameField = new JTextField(TEXT_COLUMNS);
        changeListenerSet = new HashSet<>();

        initComponents();
    }

    /**
     * Initializes visual components.
     */
    private void initComponents()
    {
        JLabel fullNameLabel = new JLabel("Repository name: ");
        fullNameLabel.setLabelFor(fullNameField);
        fullNameLabel.setDisplayedMnemonic('R');

        JLabel displayNameLabel = new JLabel("Display name: ");
        displayNameLabel.setLabelFor(displayNameField);
        displayNameLabel.setDisplayedMnemonic('D');

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        // The first row.
        c.gridy = 0;
        c.weighty = 0.0;
        c.weightx = 0.0;
        component.add(fullNameLabel, c);
        component.add(fullNameField, c);
        c.weightx = 1.0;
        component.add(new JLabel(), c);
        // The second row.
        c.gridy++;
        c.weightx = 0.0;
        component.add(displayNameLabel, c);
        component.add(displayNameField, c);
        c.weightx = 1.0;
        component.add(new JLabel(), c);
        // The last row to fill the rest of the vertical space.
        c.gridy++;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weighty = 1.0;
        component.add(new JLabel(), c);

        DocumentListener textUpdate = new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent event)
            {
                fireChange();
            }

            @Override
            public void removeUpdate(final DocumentEvent event)
            {
                fireChange();
            }

            @Override
            public void changedUpdate(final DocumentEvent event)
            {
            }
        };
        fullNameField.getDocument().addDocumentListener(textUpdate);
        displayNameField.getDocument().addDocumentListener(textUpdate);
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
    public void populate()
    {
        fullNameField.setEditable(
            repository instanceof BitbucketRepositoryProxy);
        fullNameField.setText(repository.getFullName());
        displayNameField.setText(descriptor.getDisplayName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid()
    {
        String fullName = fullNameField.getText().trim();
        if ("".equals(fullName)) {
            errorMessage = "Missing repository name";
            return false;
        }
        if (!FULL_NAME_PATTERN.matcher(fullName).matches()) {
            errorMessage = "Invalid repository name format";
            return false;
        }
        errorMessage = null;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyChanges()
    {
        assert isValid();

        String fullName = fullNameField.getText().trim();
        String displayName = displayNameField.getText().trim();
        // If the display name is blank, the full name is copied.
        if ("".equals(displayName)) {
            displayName = fullName;
        }

        if (repository instanceof BitbucketRepositoryProxy) {
            BitbucketRepositoryProxy proxy =
                (BitbucketRepositoryProxy) repository;
            Matcher matcher = FULL_NAME_PATTERN.matcher(fullName);
            if (matcher.matches()) {
                BitbucketClient client = descriptor.getBitbucketClient();
                proxy.setRepository(
                    client.getRepository(matcher.group(1), matcher.group(2)));
            }
            if (repository.getFullName() != null) {
                throw new RuntimeException("Repository not found");
            }
        }
        descriptor.setDisplayName(displayName);
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
