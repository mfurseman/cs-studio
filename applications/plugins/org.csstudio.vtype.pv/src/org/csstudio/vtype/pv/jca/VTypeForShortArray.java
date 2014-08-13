/*******************************************************************************
 * Copyright (c) 2014 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.vtype.pv.jca;

import gov.aps.jca.dbr.DBR_TIME_Short;
import gov.aps.jca.dbr.GR;

import java.util.List;

import org.epics.util.array.ArrayInt;
import org.epics.util.array.ArrayShort;
import org.epics.util.array.ListInt;
import org.epics.util.array.ListShort;
import org.epics.vtype.ArrayDimensionDisplay;
import org.epics.vtype.VShortArray;
import org.epics.vtype.VTypeToString;
import org.epics.vtype.ValueUtil;

/** Wrap DBR as VType
 *
 *  <p>Based on ideas from org.epics.pvmanager.jca, Gabriele Carcassi
 *  @author Kay Kasemir
 */
public class VTypeForShortArray extends DBRAlarmTimeDisplayWrapper<DBR_TIME_Short> implements VShortArray
{
    public VTypeForShortArray(final GR metadata, final DBR_TIME_Short dbr)
    {
        super(metadata, dbr);
    }

    @Override
    public List<ArrayDimensionDisplay> getDimensionDisplay()
    {
        return ValueUtil.defaultArrayDisplay(this);
    }

    @Override
    public ListInt getSizes()
    {
        return new ArrayInt(dbr.getShortValue().length);
    }

    @Override
    public ListShort getData()
    {
        return new ArrayShort(dbr.getShortValue());
    }

    @Override
    public String toString()
    {
        return VTypeToString.toString(this);
    }
}
