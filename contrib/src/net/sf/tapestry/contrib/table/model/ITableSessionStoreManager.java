//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.contrib.table.model;

import java.io.Serializable;

import net.sf.tapestry.IRequestCycle;

/**
 * An interface responsible for determining <b>where</b> the session state 
 * will be saved between requests.
 *  
 * @version $Id$
 * @author mindbridge
 */
public interface ITableSessionStoreManager
{
	/**
	 * Method saveState saves the session sate
	 * @param objCycle the current request cycle
	 * @param objState the session state to be saved
	 */
	void saveState(IRequestCycle objCycle, Serializable objState);
	/**
	 * Method loadState loads the session state
	 * @param objCycle the current request cycle
	 * @return Object the loaded sessions state
	 */
	Serializable loadState(IRequestCycle objCycle);
}
