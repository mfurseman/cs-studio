/**
 * 
 */
package org.csstudio.channel.widgets;

import gov.bnl.channelfinder.api.Channel;

import java.util.ArrayList;
import java.util.Collection;

import org.csstudio.csdata.ProcessVariable;
import org.csstudio.utility.pvmanager.widgets.ConfigurableWidget;
import org.csstudio.utility.pvmanager.widgets.ConfigurableWidgetAdaptable;

/**
 * @author shroffk
 * 
 */
public class ChannelViewerAdaptable implements ChannelAdaptable,
		ConfigurableWidgetAdaptable {

	private final Collection<Channel> channels;
	private final ConfigurableWidget configurableWidget;

	public ChannelViewerAdaptable(Collection<Channel> channels,
			ConfigurableWidget configurableWidget) {
		super();
		this.channels = channels;
		this.configurableWidget = configurableWidget;
	}

	@Override
	public Collection<ProcessVariable> toProcessVariables() {
		return toPVArray(channels);
	}

	@Override
	public Collection<Channel> toChannels() {
		return channels;
	}
	
	@Override
	public ConfigurableWidget toConfigurableWidget() {
		if(channels != null && !channels.isEmpty())
			return configurableWidget;
		else
			return null;
	}


	// TODO: this should go in a utility class
	public Collection<ProcessVariable> toPVArray(Collection<Channel> channels) {
		if (channels == null)
			return null;

		Collection<ProcessVariable> result = new ArrayList<ProcessVariable>();
		for (Channel channel : channels) {
			result.add(new ProcessVariable(channel.getName()));
		}
		return result;
	}

}
