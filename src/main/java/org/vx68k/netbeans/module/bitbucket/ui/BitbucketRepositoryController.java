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
import java.util.LinkedHashSet;
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
     * Repository to apply changes.
     */
    private final BitbucketRepositoryProvider.Descriptor descriptor;

    /**
     * Change listeners.
     */
    private final Set<ChangeListener> changeListeners;

    /**
     * Visual component.
     */
    private JComponent component = null;

    /**
     * Text field for the full name.
     */
    private JTextComponent repositoryNameField = null;

    /**
     * Text field for the display name.
     */
    private JTextComponent displayNameField = null;

    /**
     * Error message to show if values are not valid.
     */
    private String errorMessage = null;

    /**
     * Initializes the object.
     *
     * @param descriptor a repository descriptor
     */
    public BitbucketRepositoryController(
        final BitbucketRepositoryProvider.Descriptor descriptor)
    {
        this.descriptor = descriptor;
        this.changeListeners = new LinkedHashSet<>();
    }

    /**
     * Fires a change event.
     */
    protected void fireStateChanged()
    {
        final ChangeEvent event = new ChangeEvent(this);
        changeListeners.forEach((listener) -> listener.stateChanged(event));
    }

    /**
     * Initializes the visual components.
     */
    private void initComponents()
    {
        component = new JPanel(new GridBagLayout());
        repositoryNameField = new JTextField(TEXT_COLUMNS);
        displayNameField = new JTextField(TEXT_COLUMNS);

        DocumentListener textUpdate = new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent event)
            {
                fireStateChanged();
            }

            @Override
            public void removeUpdate(final DocumentEvent event)
            {
                fireStateChanged();
            }

            @Override
            public void changedUpdate(final DocumentEvent event)
            {
            }
        };
        repositoryNameField.getDocument().addDocumentListener(textUpdate);
        displayNameField.getDocument().addDocumentListener(textUpdate);

        // Labels

        JLabel repositoryNameLabel = new JLabel("Repository name: ");
        repositoryNameLabel.setLabelFor(repositoryNameField);
        repositoryNameLabel.setDisplayedMnemonic('R');

        JLabel displayNameLabel = new JLabel("Display name: ");
        displayNameLabel.setLabelFor(displayNameField);
        displayNameLabel.setDisplayedMnemonic('D');

        // Population

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        // The first row.
        c.gridy = 0;
        c.weighty = 0.0;
        c.weightx = 0.0;
        component.add(repositoryNameLabel, c);
        component.add(repositoryNameField, c);
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
    }

    /**
     * Releases the visual components.
     */
    private void releaseComponents()
    {
        displayNameField = null;
        repositoryNameField = null;
        component = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getComponent()
    {
        if (component == null) {
            initComponents();
        }
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
        repositoryNameField.setText(descriptor.getId());
        displayNameField.setText(descriptor.getDisplayName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid()
    {
        String repositoryName = repositoryNameField.getText().trim();
        if ("".equals(repositoryName)) {
            errorMessage = "Missing repository name";
            return false;
        }

        Matcher matcher = FULL_NAME_PATTERN.matcher(repositoryName);
        if (!matcher.matches()) {
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

        BitbucketRepositoryProxy repository =
            (BitbucketRepositoryProxy) descriptor.getRepository();

        String repositoryName = repositoryNameField.getText().trim();
        Matcher matcher = FULL_NAME_PATTERN.matcher(repositoryName);
        if (!matcher.matches()) {
            throw new IllegalStateException("Invalid repository name");
        }

        BitbucketClient client = descriptor.getBitbucketClient();
        BitbucketRepository realRepository =
            client.getRepository(matcher.group(1), matcher.group(2));
        repository.setRealRepository(realRepository);
        if (realRepository != null) {
            repositoryName = realRepository.getFullName();
        }
        descriptor.setId(repositoryName);

        String displayName = displayNameField.getText().trim();
        // If the display name is blank, the full name is copied.
        if ("".equals(displayName)) {
            displayName = repositoryName;
        }
        descriptor.setDisplayName(displayName);

        releaseComponents();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelChanges()
    {
        releaseComponents();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addChangeListener(final ChangeListener listener)
    {
        changeListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeChangeListener(final ChangeListener listener)
    {
        changeListeners.remove(listener);
    }
}
