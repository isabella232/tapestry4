// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.services.impl;

import java.util.Collection;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.AbstractEngine;
import org.apache.tapestry.engine.IPageRecorder;

/**
 * Used by {@link org.apache.tapestry.services.impl.TestEngineFactory}.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class EngineFixture extends AbstractEngine
{

    protected void cleanupAfterRequest(IRequestCycle cycle)
    {

    }

    public Collection getActivePageNames()
    {
        return null;
    }

    public IPageRecorder getPageRecorder(String pageName, IRequestCycle cycle)
    {
        return null;
    }

    public void forgetPage(String name)
    {

    }

    public IPageRecorder createPageRecorder(String pageName, IRequestCycle cycle)
    {
        return null;
    }

}