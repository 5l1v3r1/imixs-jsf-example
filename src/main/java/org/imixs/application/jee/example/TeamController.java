/*******************************************************************************
 *  Imixs Workflow Technology
 *  Copyright (C) 2003, 2008 Imixs Software Solutions GmbH,  
 *  http://www.imixs.com
 *  
 *  This program is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU General Public License 
 *  as published by the Free Software Foundation; either version 2 
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 *  General Public License for more details.
 *  
 *  You can receive a copy of the GNU General Public
 *  License at http://www.gnu.org/licenses/gpl.html
 *  
 *  Contributors:  
 *  	Imixs Software Solutions GmbH - initial API and implementation
 *  	Ralph Soika
 *  
 *******************************************************************************/
package org.imixs.application.jee.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.exceptions.AccessDeniedException;
import org.imixs.workflow.jee.faces.workitem.DataController;
import org.imixs.workflow.jee.jpa.EntityIndex;

/**
 * This backing bean is an example how to interact with the EntityService to
 * manage ItemCollections as instances of Entities
 * <p>
 * The bean provides an ItemCollectionAdapter to simplify to access properties
 * of the ItemCollection from an JSP page.
 * <p>
 * 
 * @see team.xhtml, teamlist.xhtml
 * 
 * @author rsoika
 * 
 */
@javax.inject.Named("teamController")
@javax.enterprise.context.SessionScoped
public class TeamController extends DataController implements Serializable {


	private static final long serialVersionUID = 1L;

	@javax.enterprise.context.SessionScoped
	private ArrayList<SelectItem> teamSelection;
	
	
	

	public TeamController() {
		super();
		// set a default type
		setType("team");
	}

	/**
	 * The init method is used to add necessary indices to the entity index list
	 * if index still exists the method did change any data
	 */
	@PostConstruct
	public void init() {
		// create default owner index
		try {
			getEntityService().addIndex("namowner", EntityIndex.TYP_TEXT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	
	
	/**
	 * returns an arrayList of Selectitems with all team IDs
	 * 
	 * @return
	 */
	public ArrayList<SelectItem> getTeamSelection() {

		teamSelection = new ArrayList<SelectItem>();
		String sQuery = "SELECT wi FROM Entity AS wi " + " WHERE wi.type= '"
				+ getType() + "' ORDER BY wi.modified desc";

		List<ItemCollection> col = getEntityService().findAllEntities(sQuery,
				0, -1);

		for (ItemCollection aworkitem : col) {
			String sName = aworkitem.getItemValueString("txtName");
			String sID = aworkitem.getItemValueString("$UniqueID");
			teamSelection.add(new SelectItem(sID, sName));
		}

		return teamSelection;
	}

	
	
}
