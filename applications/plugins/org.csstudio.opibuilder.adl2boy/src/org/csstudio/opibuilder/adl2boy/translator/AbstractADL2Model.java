package org.csstudio.opibuilder.adl2boy.translator;

import org.csstudio.opibuilder.model.AbstractPVWidgetModel;
import org.csstudio.opibuilder.model.AbstractWidgetModel;
import org.csstudio.utility.adlparser.fileParser.ADLWidget;
import org.csstudio.utility.adlparser.fileParser.widgetParts.ADLBasicAttribute;
import org.csstudio.utility.adlparser.fileParser.widgetParts.ADLControl;
import org.csstudio.utility.adlparser.fileParser.widgetParts.ADLMonitor;
import org.csstudio.utility.adlparser.fileParser.widgetParts.ADLObject;
import org.csstudio.utility.adlparser.fileParser.widgets.ADLAbstractWidget;
import org.eclipse.swt.graphics.RGB;

public abstract class AbstractADL2Model {
	AbstractWidgetModel widgetModel;
	RGB colorMap[] = new RGB[0];
	
	public AbstractADL2Model(final ADLWidget adlWidget, RGB colorMap[]) {
		this.colorMap = colorMap;
	}

	/**
	 * 
	 * @return
	 */
	abstract public AbstractWidgetModel getWidgetModel() ;

	/** set the properties contained in the ADL basic properties section in the 
	 * created widgetModel
	 * @param adlWidget
	 * @param widgetModel
	 */
	protected void setADLObjectProps(ADLAbstractWidget adlWidget, AbstractWidgetModel widgetModel){
		if (adlWidget.hasADLObject()){
			ADLObject adlObj=adlWidget.getAdlObject();
			widgetModel.setX(adlObj.getX());
			widgetModel.setY(adlObj.getY());
			widgetModel.setHeight(adlObj.getHeight());
			widgetModel.setWidth(adlObj.getWidth());
		}
		
	}

	/** set the properties contained in the ADL basic properties section in the 
	 * created widgetModel
	 * @param adlWidget
	 * @param widgetModel
	 */
	protected void setADLBasicAttributeProps(ADLAbstractWidget adlWidget, AbstractWidgetModel widgetModel, boolean colorForeground){
		if (adlWidget.hasADLBasicAttribute()){
			ADLBasicAttribute basAttr = adlWidget.getAdlBasicAttribute();
			System.out.println("Trying to load color " + basAttr.getClr() );
			if ((basAttr.getClr() != null) && (!(basAttr.getClr().equals(""))) ){
				if (colorForeground) {
					widgetModel.setForegroundColor(colorMap[Integer.parseInt(basAttr.getClr())]);
				}
				else {
					widgetModel.setBackgroundColor(colorMap[Integer.parseInt(basAttr.getClr())]);
				}
			}
		}
	}
	/** set the properties contained in the ADL basic properties section in the 
	 * created widgetModel
	 * @param adlWidget
	 * @param widgetModel
	 */
	protected void setADLControlProps(ADLAbstractWidget adlWidget, AbstractWidgetModel widgetModel){
		if (adlWidget.hasADLControl()){
			ADLControl control = adlWidget.getAdlControl();
			String foreClr = control.getForegroundColor();
			if ((foreClr != null) && (!(foreClr.equals(""))) ){
				widgetModel.setForegroundColor(colorMap[Integer.parseInt(foreClr)]);
			}
			String backClr = control.getBackgroundColor();
			if ((backClr != null) && (!(backClr.equals(""))) ){
				widgetModel.setBackgroundColor(colorMap[Integer.parseInt(backClr)]);
			}
			String channel = control.getChan();
			if ((channel != null) && (!(channel.equals(""))) ){
				widgetModel.setPropertyValue(AbstractPVWidgetModel.PROP_PVNAME, channel);
			}
		}
	}
	/** set the properties contained in the ADL basic properties section in the 
	 * created widgetModel
	 * @param adlWidget
	 * @param widgetModel
	 */
	protected void setADLMonitorProps(ADLAbstractWidget adlWidget, AbstractWidgetModel widgetModel){
		if (adlWidget.hasADLMonitor()){
			ADLMonitor monitor = adlWidget.getAdlMonitor();
			String foreClr = monitor.getForegroundColor();
			if ((foreClr != null) && (!(foreClr.equals(""))) ){
				widgetModel.setForegroundColor(colorMap[Integer.parseInt(foreClr)]);
			}
			String backClr = monitor.getBackgroundColor();
			if ((backClr != null) && (!(backClr.equals(""))) ){
				widgetModel.setBackgroundColor(colorMap[Integer.parseInt(backClr)]);
			}
			String channel = monitor.getChan();
			if ((channel != null) && (!(channel.equals(""))) ){
				widgetModel.setPropertyValue(AbstractPVWidgetModel.PROP_PVNAME, channel);
			}
		}
	}
}
