/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.component.text;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TextGradationsView extends JComponent {

	/** serialVersionUID */
	private static final long serialVersionUID = 5766991295487339962L;

	private static final int MARGIN = 5;

	private final JTextPane textArea;
	private final FontMetrics fontMetrics;

	private final Insets textInsets;

	private final int fontWidth;

	public TextGradationsView(final JTextPane textArea) {
		this.textArea = textArea;
		textInsets = textArea.getInsets();
		Font font = textArea.getFont();
		fontMetrics = getFontMetrics(font);
		fontWidth = fontMetrics.charWidth('m');

		textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				repaint();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				repaint();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		textArea.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				revalidate();
				repaint();
			}
		});

		Insets i = textArea.getInsets();
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
				BorderFactory.createEmptyBorder(MARGIN, i.left, MARGIN - 1, i.right)));
		setOpaque(true);
		setBackground(Color.WHITE);
		setFont(font);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(textArea.getWidth(), 14);
	}

	@Override
	public void paintComponent(final Graphics g) {
		g.setColor(getBackground());
		Rectangle clip = g.getClipBounds();
		g.fillRect(clip.x, clip.y, clip.width, clip.height);
		 System.out.println(String.format("clip %d %d %d %d", clip.x, clip.y,
		 clip.width, clip.height));

		g.setColor(getForeground());
		for (int x = 0; x <= clip.x + clip.width; x += fontWidth) {
			int i = x / fontWidth;
			if (0 == i % 10) {
				g.drawLine(x + textInsets.left, 2, x + textInsets.left, 14);
			} else if (5 == i % 10) {
				g.drawLine(x + textInsets.left, 6, x + textInsets.left, 14);
			} else {
				g.drawLine(x + textInsets.left, 10, x + textInsets.left, 14);
			}
		}
	}
}