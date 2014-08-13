/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.opibuilder.script;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.csstudio.opibuilder.OPIBuilderPlugin;
import org.csstudio.opibuilder.editparts.AbstractBaseEditPart;
import org.csstudio.opibuilder.editparts.DisplayEditpart;
import org.csstudio.opibuilder.util.ConsoleService;
import org.csstudio.opibuilder.util.ResourceUtil;
import org.csstudio.simplepv.IPV;
import org.csstudio.simplepv.IPVListener;
import org.csstudio.ui.util.thread.UIBundlingThread;
import org.eclipse.core.runtime.IPath;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

/**
 * The script store help to store the compiled script for afterward executions.
 * This is the abstract script store implementation for BOY script execution. All script stores
 * in BOY should implement this abstract class with a specific script engine. 
 * The store must be disposed manually when it is not needed.
 * @author Xihui Chen
 *
 */
public abstract class AbstractScriptStore implements IScriptStore{	
	
	private IPath absoluteScriptPath;

	private String errorSource;

	private Map<IPV, IPVListener> pvListenerMap;

	private boolean errorInScript;

	volatile boolean unRegistered = false;


	/**
	 * A map to see if a PV was triggered before, this is used to skip the first trigger.
	 */
	private Map<IPV, Boolean> pvTriggeredMap;

	private boolean triggerSuppressed = false;
	
	private ScriptData scriptData;
	private AbstractBaseEditPart editPart;
	private IPV[] pvArray;
	
	public AbstractScriptStore(final ScriptData scriptData, final AbstractBaseEditPart editpart,
			final IPV[] pvArray) throws Exception {		
		
		this.scriptData = scriptData;
		this.editPart = editpart;
		this.pvArray = pvArray;
		
		if(!(scriptData instanceof RuleScriptData) && !scriptData.isEmbedded()){			
			absoluteScriptPath = scriptData.getPath();
			if(!absoluteScriptPath.isAbsolute()){
				absoluteScriptPath = ResourceUtil.buildAbsolutePath(
						editpart.getWidgetModel(), absoluteScriptPath);
				if(!ResourceUtil.isExsitingFile(absoluteScriptPath, true)){
					//search from OPI search path
					absoluteScriptPath = ResourceUtil.getFileOnSearchPath(scriptData.getPath(), true);
					if(absoluteScriptPath == null)
						throw new FileNotFoundException(scriptData.getPath().toString());
				}
			}
		}
		
		initScriptEngine();
		
		errorInScript = false;
		errorSource =(scriptData instanceof RuleScriptData ?
				((RuleScriptData)scriptData).getRuleData().getName() : scriptData.getPath().toString())
				+ " on " +
						editpart.getWidgetModel().getName() ;
		

		if(scriptData instanceof RuleScriptData){
			compileString(((RuleScriptData)scriptData).getScriptString());
		}else if(scriptData.isEmbedded())
			compileString(scriptData.getScriptText());
		else{			
			//read file
			InputStream inputStream = ResourceUtil.pathToInputStream(absoluteScriptPath, false);

			//compile
			compileInputStream(inputStream);
			inputStream.close();
		}		


		pvListenerMap = new HashMap<IPV, IPVListener>();
		pvTriggeredMap = new HashMap<IPV, Boolean>();

		IPVListener suppressPVListener = new IPVListener.Stub() {

			public synchronized void valueChanged(IPV pv) {
				if (triggerSuppressed && checkPVsConnected(scriptData, pvArray)) {
					executeScriptInUIThread(pv);
					triggerSuppressed = false;
				}
			}
			
		};

		IPVListener triggerPVListener = new IPVListener.Stub() {
			public synchronized void valueChanged(IPV pv) {

				// skip the first trigger if it is needed.
				if (scriptData.isSkipPVsFirstConnection()
						&& !pvTriggeredMap.get(pv)) {
					pvTriggeredMap.put(pv, true);
					return;
				}

				// execute script only if all input pvs are connected
				if (pvArray.length > 1) {
					if (!checkPVsConnected(scriptData, pvArray)) {
						triggerSuppressed = true;
						return;

					}
				}

				executeScriptInUIThread(pv);
			}
			
		};
		//register pv listener
		int i=0;
		for(IPV pv : pvArray){
			if(pv == null)
				continue;
			if(!scriptData.getPVList().get(i++).trigger){
				//execute the script if it was suppressed.
				pv.addListener(suppressPVListener);
				pvListenerMap.put(pv, suppressPVListener);
				continue;
			};
			pvTriggeredMap.put(pv, false);
			pv.addListener(triggerPVListener);
			pvListenerMap.put(pv, triggerPVListener);

		}
	}

	/**Initialize the script engine.
	 * @param editpart
	 * @param pvArray
	 */
	protected abstract void initScriptEngine() throws Exception ;
	
	/**Compile string with script engine.
	 * @param string
	 * @throws Exception 
	 */
	protected abstract void compileString(String string) throws Exception;

	/**Compile InputStream with script engine. The stream will be closed by this method.
	 * @param reader
	 * @throws Exception
	 */
	protected abstract void compileInputStream(InputStream s) throws Exception;
	
	/**
	 * Execute the script with script engine.
	 * @param triggerPV  the PV that triggers this execution.
	 */
	protected abstract void execScript(final IPV triggerPV) throws Exception;
	
	private void executeScriptInUIThread(final IPV triggerPV) {
		Display display = editPart.getRoot().getViewer().getControl().getDisplay();
		UIBundlingThread.getInstance().addRunnable(display, new Runnable() {
			public void run() {
				if ((!scriptData.isStopExecuteOnError() || !errorInScript) && !unRegistered) {
					try {
						execScript(triggerPV);
					} catch (Exception e) {
						errorInScript = true;
						final String notExecuteWarning = "\nThe script or rule will not be executed afterwards. " +
								"You can change this setting in script dialog.";
						final String message = NLS
								.bind("Error in {0}.{1}\n{2}",
										new String[]{errorSource, 
										 !scriptData.isStopExecuteOnError()? "" : notExecuteWarning, //$NON-NLS-1$
												 e.toString()});
						ConsoleService.getInstance().writeError(message);
						OPIBuilderPlugin.getLogger().log(Level.WARNING, message, e);
					}
				}
			}
		});
	}

	private boolean checkPVsConnected(ScriptData scriptData, IPV[] pvArray){
		if(!scriptData.isCheckConnectivity())
			return true;
		for(IPV pv : pvArray){
			if(!pv.isConnected())
				return false;
		}
		return true;

	}

	public void unRegister() {
		unRegistered = true;
		for(Entry<IPV, IPVListener> entry :  pvListenerMap.entrySet()){
			entry.getKey().removeListener(entry.getValue());
		}
	}

	/**
	 * @return the scriptData
	 */
	public ScriptData getScriptData() {
		return scriptData;
	}

	/**
	 * @return the editPart
	 */
	public AbstractBaseEditPart getEditPart() {
		return editPart;
	}
	
	/**
	 * @return the display editPart
	 */
	public DisplayEditpart getDisplayEditPart() {		
		if(getEditPart().isActive())
			return (DisplayEditpart)(getEditPart().getViewer().getContents());
		return null;
	}
	

	/**
	 * @return the pvArray
	 */
	public IPV[] getPvArray() {
		return pvArray;
	}

	public IPath getAbsoluteScriptPath() {
		return absoluteScriptPath;
	}
	
	
	
}
