package org.csstudio.nams.configurator.beans;

import org.csstudio.nams.service.configurationaccess.localstore.declaration.filterActions.AlarmbearbeitergruppenFilterActionType;
import org.csstudio.nams.service.configurationaccess.localstore.declaration.filterActions.FilterActionType;

/**
 * (2,'SMS an Gruppe',NULL)
 * (3,'SMS an Gruppe Best.',NULL)
 * (5,'VMail an Gruppe',NULL)
 * (6,'VMail an Gruppe Best.',NULL)
 * (8,'EMail an Gruppe',NULL)
 * (9,'EMail an Gruppe Best.',NULL)
 *
 */
public class AlarmbearbeitergruppenFilterAction extends AbstractFilterAction<AlarmbearbeitergruppenFilterActionType> {

	public AlarmbearbeitergruppenFilterAction() {
		super(AlarmbearbeitergruppenFilterActionType.class);
	}

	public FilterActionType[] getFilterActionTypeValues() {
		return AlarmbearbeitergruppenFilterActionType.values();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		AlarmbearbeitergruppenFilterAction action = new AlarmbearbeitergruppenFilterAction();
		action.message = this.message;
		action.receiver = this.receiver;
		action.type = this.type;
		return action;
	}

}
