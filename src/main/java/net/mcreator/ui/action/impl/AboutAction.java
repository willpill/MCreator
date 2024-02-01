/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2020 Pylo and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.mcreator.ui.action.impl;

import net.mcreator.Launcher;
import net.mcreator.element.GeneratableElement;
import net.mcreator.io.FileIO;
import net.mcreator.io.OS;
import net.mcreator.ui.MCreatorApplication;
import net.mcreator.ui.action.ActionRegistry;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.component.util.ComponentUtils;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.SVG;
import net.mcreator.ui.laf.themes.Theme;
import net.mcreator.util.DesktopUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AboutAction extends BasicAction {

	public AboutAction(ActionRegistry actionRegistry) {
		super(actionRegistry, L10N.t("action.about"), evt -> showDialog(actionRegistry.getMCreator()));
	}

	public static void showDialog(Window parent) {
		Object[] options = { L10N.t("dialog.about.option.website"), L10N.t("dialog.about.option.support"),
				L10N.t("dialog.about.option.eula"), L10N.t("dialog.about.option.third_party_licenses"),
				L10N.t("dialog.about.option.donate") };

		JPanel logoPanel = new JPanel(new BorderLayout(24, 24));
		logoPanel.add("North", new JLabel(SVG.getAppIcon(128, 128)));
		logoPanel.add("Center", new JLabel(SVG.getBuiltIn("logo", 250, (int) (250 * (63 / 350.0)))));
		logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));

		JLabel aboutLabel = L10N.label("dialog.about.message", Launcher.version.major, Launcher.version.getFullString(),
				(MCreatorApplication.isInternet ?
						MCreatorApplication.WEB_API.getUpdateInfo().getLatestMajor() :
						L10N.t("common.not_applicable")), GeneratableElement.formatVersion, OS.getSystemBits(),
				OS.getArchitecture(), OS.getBundledJVMBits());

		JComponent dialogPanel = PanelUtils.westAndCenterElement(
				PanelUtils.pullElementUp(PanelUtils.centerInPanel(logoPanel)), aboutLabel, 48, 48);
		dialogPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 32));

		int n = JOptionPane.showOptionDialog(parent, dialogPanel, L10N.t("dialog.about.title"),
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (n == 0) {
			DesktopUtils.browseSafe(MCreatorApplication.SERVER_DOMAIN + "/");
		} else if (n == 1) {
			DesktopUtils.browseSafe(MCreatorApplication.SERVER_DOMAIN + "/support");
		} else if (n == 2) {
			showLicenseWindow(parent);
		} else if (n == 3) {
			try {
				Desktop.getDesktop().open(new File("./license"));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(parent, L10N.t("dialog.about.third_party.message"),
						L10N.t("dialog.about.third_party.title"), JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (n == 4) {
			DesktopUtils.browseSafe(MCreatorApplication.SERVER_DOMAIN + "/donate");
		}
	}

	public static void showLicenseWindow(Window parent) {
		JTextArea licenseText = new JTextArea();
		JScrollPane gradlesp = new JScrollPane(licenseText);
		licenseText.setEditable(false);
		licenseText.setLineWrap(true);
		licenseText.setFont(Theme.current().getConsoleFont());
		ComponentUtils.deriveFont(licenseText, 12);
		licenseText.setWrapStyleWord(true);
		licenseText.setText(FileIO.readFileToString(new File("./LICENSE.txt")));
		gradlesp.setPreferredSize(new Dimension(570, 570));
		gradlesp.getHorizontalScrollBar().setValue(0);
		gradlesp.getVerticalScrollBar().setValue(0);
		licenseText.setCaretPosition(0);
		JOptionPane.showOptionDialog(parent, gradlesp, L10N.t("dialog.about.eula.title"), JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { L10N.t("common.close") }, L10N.t("common.close"));
	}

}
