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

package org.apache.tapestry.contrib.link;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.link.ILinkRenderer;

/**
 * An {@link ILinkRenderer} implementation that generates an HTML button.
 * This is particularly useful for implementing cancel buttons.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class ButtonLinkRenderer implements ILinkRenderer
{
    public static final ILinkRenderer SHARED_INSTANCE = new ButtonLinkRenderer();

    /**
     * @see org.apache.tapestry.link.ILinkRenderer#renderLink(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle, org.apache.tapestry.components.ILinkComponent)
     */
    public void renderLink(IMarkupWriter writer, IRequestCycle cycle, ILinkComponent component)
    {
        if (cycle.getAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME) != null)
        {
            String message = Tapestry.getMessage("AbstractLinkComponent.no-nesting");
            throw new ApplicationRuntimeException(message, component, null, null);
        }

        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, component);

        ILink link = component.getLink(cycle);

        writer.begin("button");
        writer.attribute("type", "button");

        if (component.isDisabled())
        {
            writer.attribute("disabled", "disabled");
        }

        String url = link.getURL(component.getAnchor(), true);
        String target = component.getTarget();
        String onclick = (target == null) ? getScript(url) : getScript(url, target);

        writer.attribute("onclick", onclick);

        component.renderAdditionalAttributes(writer, cycle);

        IMarkupWriter wrappedWriter = writer.getNestedWriter();

        component.renderBody(wrappedWriter, cycle);

        wrappedWriter.close();

        writer.end();

        cycle.removeAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);
    }

    /**
     * Generates the onclick event handler that opens the specified url in the current window.
     * @param url the url generated by this link
     * @return a JavaScript onclick event handler
     */
    protected String getScript(String url)
    {
        return "window.location='" + url + "'";
    }

    /**
     * Generates the onclick event handler that opens the specified url in the specified window or frame.
     * @param url the url generated by this link
     * @param target the name of the target window or frame
     * @return a JavaScript onclick event handler
     */
    protected String getScript(String url, String target)
    {
        return "window.open('" + url + "','" + target + "')";
    }
}