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

package net.mcreator.ui.workspace.selector;

import net.mcreator.generator.GeneratorFlavor;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.themes.Theme;
import net.mcreator.util.StringUtils;
import net.mcreator.util.image.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

class RoundedPanel extends JPanel {
	public RoundedPanel(LayoutManager layout) {
		super(layout);
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(getBackground());
		g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
	}
}

class RecentWorkspacesRenderer extends RoundedPanel implements ListCellRenderer<RecentWorkspaceEntry> {
	private final JLabel nameLabel = new JLabel();
	private final JLabel pathLabel = new JLabel();
	private final JLabel iconLabel = new JLabel();

	RecentWorkspacesRenderer() {
		super(new BorderLayout(7, 0));

		setBackground(Theme.current().getSecondAltBackgroundColor());

		nameLabel.setFont(getFont().deriveFont(17.0f).deriveFont(Font.BOLD));
		pathLabel.setFont(getFont().deriveFont(11.0f));

		add("West", iconLabel);
		add("Center", PanelUtils.northAndCenterElement(nameLabel, pathLabel));
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends RecentWorkspaceEntry> list,
			RecentWorkspaceEntry value, int index, boolean isSelected, boolean cellHasFocus) {

		String path = value.getPath().getParentFile().getAbsolutePath().replace("\\", "/");

		nameLabel.setForeground(isSelected ? Color.WHITE : Theme.current().getForegroundColor());
		pathLabel.setForeground(isSelected ? Color.WHITE : Theme.current().getAltForegroundColor());

		setToolTipText(L10N.t("dialog.workspace_selector.recent_workspace", value.getName(), path,
				Objects.requireNonNullElse(value.getMCRVersion(), L10N.t("common.not_applicable"))));
		setBorder(BorderFactory.createEmptyBorder(6, 12, 8, 6));

		if (value.getType() != GeneratorFlavor.UNKNOWN) {
			BufferedImage bufferedIcon = ImageUtils.toBufferedImage(value.getType().getIcon().getImage());
			iconLabel.setIcon (
					isSelected ? new ImageIcon(bufferedIcon) : new ImageIcon(ImageUtils.darken(bufferedIcon)));
			nameLabel.setText(StringUtils.abbreviateString(value.getName(), 17));
			pathLabel.setText(StringUtils.abbreviateStringInverse(path, 26));
		} else {
			iconLabel.setIcon(null);
			nameLabel.setText(StringUtils.abbreviateString(value.getName(), 19));
			pathLabel.setText(StringUtils.abbreviateStringInverse(path, 34));
		}

		if (isSelected) {
			setBackground(Theme.current().getInterfaceAccentColor());
		} else {
			setBackground(Theme.current().getSecondAltBackgroundColor());
		}

		return this;
	}

}
