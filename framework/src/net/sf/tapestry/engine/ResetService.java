package net.sf.tapestry.engine;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.Tapestry;

/**
 *  ServiceLink used to discard all cached data (templates, specifications, et cetera).
 *  This is primarily used during development.  It could be a weakness of a Tapestry
 *  application, making it susceptible to denial of service attacks, which is why
 *  it is disabled by default.  The link generated by the ResetService redisplays the
 *  current page after discarding all data.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *  @see net.sf.tapestry.IEngine#isResetServiceEnabled()
 * 
 **/

public class ResetService extends AbstractService
{

    public Gesture buildGesture(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        if (Tapestry.size(parameters) != 0)
            throw new IllegalArgumentException(Tapestry.getString("service-no-parameters", RESET_SERVICE));

        String[] context = new String[1];
        context[0] = component.getPage().getName();

        return assembleGesture(cycle, RESET_SERVICE, context, null, true);
    }

    public String getName()
    {
        return RESET_SERVICE;
    }

    public boolean service(IEngineServiceView engine, IRequestCycle cycle, ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException
    {
        String[] context = getServiceContext(cycle.getRequestContext());

        if (Tapestry.size(context) != 1)
            throw new ApplicationRuntimeException(Tapestry.getString("service-single-parameter", RESET_SERVICE));

        String pageName = context[0];

        if (engine.isResetServiceEnabled())
            engine.clearCachedData();

        IPage page = cycle.getPage(pageName);

        page.validate(cycle);

        cycle.setPage(page);

        // Render the same page (that contained the reset link).

        engine.renderResponse(cycle, output);

        return true;
    }

}