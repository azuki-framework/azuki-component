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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;

import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

public class TextEditor extends AbstractTextEditor {

	/** serialVersionUID */
	private static final long serialVersionUID = 7284998526769136420L;

	//
	private static final Color lineColor1 = new Color(255, 255, 255);
	private static final Color lineColor2 = new Color(248, 248, 248);
	// private static final Color lineColor2 = new Color(248, 0, 248);

	// 行ハイライト
	private static final Color lineCaretColor = new Color(228, 238, 254);
	private final DefaultCaret caret;

	public TextEditor() {
		if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			Font font = new Font("ＭＳ ゴシック", Font.PLAIN, 16);
			setFont(font);
		} else {
			Font font = new Font(Font.MONOSPACED, Font.PLAIN, 16);
			setFont(font);
		}

		setEditorKit(new SmartEditorKit());

		// 行ハイライト
		caret = new DefaultCaret() {
			/** serialVersionUID */
			private static final long serialVersionUID = -5227694834003006790L;

			@Override
			protected synchronized void damage(Rectangle r) {
				if (r != null) {
					JTextComponent c = getComponent();
					x = 0;
					y = r.y;
					width = c.getSize().width;
					height = r.height;
					c.repaint();
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				setVisible(true);
				setSelectionVisible(true);
			}
		};
		caret.setBlinkRate(getCaret().getBlinkRate());
		setCaret(caret);
	}

	private int getLineAtPoint(int y) {
		Element root = getDocument().getDefaultRootElement();
		int pos = viewToModel(new Point(0, y));
		return root.getElementIndex(pos);
	}

	// 行ハイライト
	@Override
	protected void paintComponent(final Graphics g) {
		if (true) {
			Graphics2D g2 = (Graphics2D) g;

			Insets insets = getInsets();

			g2.setColor(getBackground());
			Rectangle clip = g2.getClipBounds();
			g2.fillRect(clip.x, clip.y, clip.width, clip.height);
			// System.out.println(String.format("clip %d %d %d %d", clip.x, clip.y, clip.width, clip.height));

			g2.setColor(getForeground());
			int base = clip.y;
			int start = getLineAtPoint(base);
			int end = getLineAtPoint(base + clip.height);
			int x = 0;
			int y = start * caret.height + insets.top;
			int w = getWidth();
			int h = caret.height;
			for (int i = start; i <= end; i++) {
				if (0 == i % 2) {
					g2.setColor(lineColor1);
				} else {
					g2.setColor(lineColor2);
				}
				g2.fillRect(x, y, w, h);
				y += h;
			}

			// ハイライト行
			y = caret.y;
			g2.setPaint(lineCaretColor);
			g2.fillRect(0, y, w, h);

		} else {
			Graphics2D g2 = (Graphics2D) g;
			Insets insets = getInsets();

			int y = insets.top;
			int h = caret.height;
			int w = getWidth();

			int i = 0;
			if (0 < h) {
				while (y < getHeight()) {
					if (0 == i % 2) {
						g2.setColor(lineColor1);
					} else {
						g2.setColor(lineColor2);
					}
					g2.fillRect(0, y, w, h);
					y += h;
					i++;
				}
			}
			// ハイライト行
			y = caret.y;
			g2.setPaint(lineCaretColor);
			g2.fillRect(0, y, w, h);
		}
		// System.out.println(String.format("x: %d; y: %d; width: %d; height: %d", getX(), getY(), getWidth(), getHeight()));
		super.paintComponent(g);
	}
}
