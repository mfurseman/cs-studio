/*******************************************************************************
 * Copyright (c) 2013 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.opibuilder.converter.model;

/**
 * @author Lei Hu, Xihui Chen
 *
 */
public class Edm_menuMuxClass extends EdmWidget {

	
	@EdmAttributeAn @EdmOptionalAn private EdmColor onColor;
	@EdmAttributeAn @EdmOptionalAn private EdmColor offColor;
	@EdmAttributeAn @EdmOptionalAn private EdmColor inconsistentColor;
	@EdmAttributeAn @EdmOptionalAn private EdmColor topShadowColor;
	@EdmAttributeAn @EdmOptionalAn private EdmColor botShadowColor;
	
	@EdmAttributeAn @EdmOptionalAn private String controlPv;
	@EdmAttributeAn @EdmOptionalAn private String onLabel;
	@EdmAttributeAn @EdmOptionalAn private String offLabel;
	@EdmAttributeAn @EdmOptionalAn private String buttonType;
	@EdmAttributeAn @EdmOptionalAn private int controlBitsPos;
	
	@EdmAttributeAn @EdmOptionalAn private EdmMultiStrings symbolTag;
	@EdmAttributeAn @EdmOptionalAn private EdmMultiStrings value0;
	
	

	public Edm_menuMuxClass(EdmEntity genericEntity) throws EdmException {
		super(genericEntity);
	}

	
	public String getButtonType() {
		return buttonType;
	}
	public int getControlBitsPos() {
		return controlBitsPos;
	}
	
	public EdmColor getOnColor() {
		return onColor;
	}
	public EdmColor getOffColor() {
		return offColor;
	}
	public EdmColor getInconsistentColor() {
		return inconsistentColor;
	}
	public EdmColor getTopShadowColor() {
		return topShadowColor;
	}
	public EdmColor getBotShadowColor() {
		return botShadowColor;
	}

	public final String getControlPv() {
		return controlPv;
	}	

	public final String getOnLabel() {
		System.out.println("on label is" + onLabel);
		return onLabel;
	}	
	public final String getOffLabel() {
		return offLabel;
	}	

	public final EdmMultiStrings getSymbolTag() {
		return symbolTag;
	}

}