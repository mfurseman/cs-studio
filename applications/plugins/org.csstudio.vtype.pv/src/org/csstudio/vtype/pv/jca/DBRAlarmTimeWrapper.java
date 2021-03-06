/*******************************************************************************
 * Copyright (c) 2014 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.vtype.pv.jca;

import gov.aps.jca.dbr.Severity;
import gov.aps.jca.dbr.TIME;
import gov.aps.jca.dbr.TimeStamp;

import org.epics.util.time.Timestamp;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Time;

/** Implement VType Alarm and Time for a DBR
 *  @author Kay Kasemir
 */
public class DBRAlarmTimeWrapper<T_DBR extends TIME> implements Alarm, Time 
{
    final protected T_DBR dbr;

    public DBRAlarmTimeWrapper(final T_DBR dbr)
    {
        this.dbr = dbr;
    }

    @Override
    public AlarmSeverity getAlarmSeverity()
    {
        if (dbr == null)
            return AlarmSeverity.NONE;

        if (dbr.getSeverity() == Severity.NO_ALARM)
            return AlarmSeverity.NONE;
        if (dbr.getSeverity() == Severity.MINOR_ALARM)
            return AlarmSeverity.MINOR;
        if (dbr.getSeverity() == Severity.MAJOR_ALARM)
            return AlarmSeverity.MAJOR;
        if (dbr.getSeverity() == Severity.INVALID_ALARM)
            return AlarmSeverity.INVALID;
        return AlarmSeverity.UNDEFINED;
    }

    @Override
    public String getAlarmName()
    {
        if (dbr == null)
            return "";
        return dbr.getStatus().getName();
    }

    @Override
    public Timestamp getTimestamp()
    {
        if (dbr == null)
            return Timestamp.now();
        final TimeStamp epics_time = dbr.getTimeStamp();
        return Timestamp.of(epics_time.secPastEpoch() + 631152000L,  (int) epics_time.nsec());
    }

    @Override
    public Integer getTimeUserTag()
    {
        return null;
    }

    @Override
    public boolean isTimeValid()
    {
        if (dbr == null)
            return false;
        final TimeStamp epics_time = dbr.getTimeStamp();
        return epics_time.secPastEpoch() > 0;
    }
}
