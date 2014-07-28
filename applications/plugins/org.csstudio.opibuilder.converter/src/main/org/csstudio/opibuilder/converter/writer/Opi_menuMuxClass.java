/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.opibuilder.converter.writer;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.csstudio.opibuilder.converter.model.EdmString;
import org.csstudio.opibuilder.converter.model.Edm_menuMuxClass;

/**
 * XML conversion class for Edm_menuMuxClass
 * @author Matevz
 */
public class Opi_menuMuxClass extends OpiWidget {

	private static Logger log = Logger.getLogger("org.csstudio.opibuilder.converter.writer.Opi_menuMuxClass");
	private static final String typeId = "combo";
	private static final String name = "EDM menu mux";
	private static final String version = "1.0";

	/**
	 * Converts the Edm_menuMuxClass to OPI  widget XML.  
	 */
	public Opi_menuMuxClass(Context con, Edm_menuMuxClass r) {
		super(con, r);
		setTypeId(typeId);
		setName(name);
		setVersion(version);
		
		if(r.getControlPv()!=null)
		{
			new OpiString(widgetContext, "pv_name", convertPVName(r.getControlPv()));
			System.out.println(convertPVName(r.getControlPv()));
			new OpiBoolean(widgetContext, "items_from_pv", true);
		}
		
		log.debug("Edm_menuMuxClass written.");

	}

}

