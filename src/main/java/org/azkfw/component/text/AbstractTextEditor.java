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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

/**
 * このクラスは、テキストエディタの基底クラスです。
 * 
 * @author Kawakicchi
 */
public class AbstractTextEditor extends JTextPane {

	/** serialVersionUID */
	private static final long serialVersionUID = -699085174071789314L;

	public AbstractTextEditor() {
		setOpaque(false);

		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		setFont(font);

		boolean tabFlag = false;
		if (tabFlag) {
			FontMetrics fontMetrics = getFontMetrics(font);
			int charWidth = fontMetrics.charWidth('m');
			int tabLength = charWidth * 4;
			TabStop[] tabs = new TabStop[100];
			for (int j = 0; j < tabs.length; j++) {
				tabs[j] = new TabStop((j + 1) * tabLength);
			}
			TabSet tabSet = new TabSet(tabs);
			SimpleAttributeSet atrTabSpace = new SimpleAttributeSet();
			StyleConstants.setTabSet(atrTabSpace, tabSet);

			setStyledDocument(new DefaultStyledDocument());
			getStyledDocument().setParagraphAttributes(0, getDocument().getLength(), atrTabSpace, false);
		}

		setEditorKit(new NoWrapEditorKit());

		getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				// System.out.println(String.format("remove"));
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// System.out.println(String.format("insert"));
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// System.out.println("change");
			}
		});

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// System.out.println("keyTyped");
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// System.out.println("keyReleased");
				doChangeText();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// System.out.println("keyPressed");
			}
		});
	}

	public void setText(final String text) {
		super.setText(text);
		doChangeText();
	}

	protected void doChangeText() {
	}
}
