/* 
 * Copyright (c) 2006 Stiftung Deutsches Elektronen-Synchroton, 
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS. 
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND 
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE 
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR 
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE. 
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, 
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION, 
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS 
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY 
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */
package org.csstudio.sds.components.ui.internal.figures;

import org.csstudio.sds.ui.figures.IBorderEquippedWidget;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A rectangle figure.
 * 
 * @author Sven Wende
 * 
 */
public final class RefreshableRectangleFigure extends RectangleFigure implements IAdaptable {
	/**
	 * The fill grade (0 - 100%).
	 */
	private double _fillGrade = 100;
	
	/**
	 * The orientation (horizontal==true | vertical==false).
	 */
	private boolean _orientationHorizontal = true;

	/**
	 * A border adapter, which covers all border handlings.
	 */
	private IBorderEquippedWidget _borderAdapter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void fillShape(final Graphics graphics) {
		Rectangle figureBounds = getBounds();

		//

		graphics.setBackgroundColor(getBackgroundColor());
		graphics.fillRectangle(getBounds());
		graphics.setBackgroundColor(getForegroundColor());
		Rectangle fillRectangle;
		if (_orientationHorizontal) {
			int newW = (int) Math.round(figureBounds.width * (getFill() / 100));
			fillRectangle = new Rectangle(figureBounds.x,figureBounds.y,newW,figureBounds.height);
		} else {
			int newH = (int) Math.round(figureBounds.height * (getFill() / 100));
			fillRectangle = new Rectangle(figureBounds.x,figureBounds.y+figureBounds.height-newH,figureBounds.width,newH);
		}
		//new Rectangle(figureBounds.getLocation(),new Dimension(newW, figureBounds.height))
		graphics.fillRectangle(fillRectangle);
	}

	/**
	 * This method is a tribute to unit tests, which need a way to test the
	 * performance of the figure implementation. Implementors should produce
	 * some random changes and refresh the figure, when this method is called.
	 * 
	 */
	public void randomNoiseRefresh() {
		setFill(Math.random() * 100);
		repaint();
	}

	/**
	 * Sets the fill grade.
	 * 
	 * @param fill
	 *            the fill grade.
	 */
	public void setFill(final double fill) {
		_fillGrade = fill;
	}

	/**
	 * Gets the fill grade.
	 * 
	 * @return the fill grade
	 */
	public double getFill() {
		return _fillGrade;
	}
	
	/**
	 * Sets the orientation (horizontal==true | vertical==false).
	 * 
	 * @param horizontal
	 *            The orientation.
	 */
	public void setOrientation(final boolean horizontal) {
		_orientationHorizontal = horizontal;
	}

	/**
	 * Gets the orientation (horizontal==true | vertical==false).
	 * 
	 * @return boolean
	 * 				The orientation
	 */
	public boolean getOrientation() {
		return _orientationHorizontal;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getAdapter(final Class adapter) {
		if (adapter == IBorderEquippedWidget.class) {
			if(_borderAdapter==null) {
				_borderAdapter = new BorderAdapter(this);
			}
			return _borderAdapter;
		}
		return null;
	}

}
