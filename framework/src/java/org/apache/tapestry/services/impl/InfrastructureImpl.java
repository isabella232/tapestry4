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

import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.services.ComponentMessagesSource;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ObjectPool;
import org.apache.tapestry.services.RequestCycleFactory;
import org.apache.tapestry.services.RequestExceptionReporter;
import org.apache.tapestry.services.ResetEventCoordinator;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceMap;
import org.apache.tapestry.services.TemplateSource;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * Allows access to selected HiveMind services.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class InfrastructureImpl implements Infrastructure
{
    private IApplicationSpecification _applicationSpecification;

    private IPropertySource _applicationPropertySource;

    private ResetEventCoordinator _resetEventCoordinator;

    private ComponentMessagesSource _componentMessagesSource;

    private TemplateSource _templateSource;

    private ISpecificationSource _specificationSource;

    private ObjectPool _objectPool;

    private IPageSource _pageSource;

    private ClassResolver _classResolver;

    private DataSqueezer _dataSqueezer;

    private IScriptSource _scriptSource;

    private ServiceMap _serviceMap;

    private RequestExceptionReporter _requestExceptionReporter;

    private ResponseRenderer _responseRenderer;

    private LinkFactory _linkFactory;

    private RequestCycleFactory _requestCycleFactory;

    public IScriptSource getScriptSource()
    {
        return _scriptSource;
    }

    public void setScriptSource(IScriptSource scriptSource)
    {
        _scriptSource = scriptSource;
    }

    public DataSqueezer getDataSqueezer()
    {
        return _dataSqueezer;

    }

    public void setDataSqueezer(DataSqueezer dataSqueezer)
    {
        _dataSqueezer = dataSqueezer;
    }

    public void setApplicationPropertySource(IPropertySource source)
    {
        _applicationPropertySource = source;
    }

    public IPropertySource getApplicationPropertySource()
    {
        return _applicationPropertySource;
    }

    public ComponentMessagesSource getComponentMessagesSource()
    {
        return _componentMessagesSource;
    }

    public ResetEventCoordinator getResetEventCoordinator()
    {
        return _resetEventCoordinator;
    }

    public void setComponentMessagesSource(ComponentMessagesSource source)
    {
        _componentMessagesSource = source;
    }

    public void setResetEventCoordinator(ResetEventCoordinator coordinator)
    {
        _resetEventCoordinator = coordinator;
    }

    public TemplateSource getTemplateSource()
    {
        return _templateSource;
    }

    public void setTemplateSource(TemplateSource source)
    {
        _templateSource = source;
    }

    public ISpecificationSource getSpecificationSource()
    {
        return _specificationSource;
    }

    public void setSpecificationSource(ISpecificationSource source)
    {
        _specificationSource = source;
    }

    public ObjectPool getObjectPool()
    {
        return _objectPool;
    }

    public void setObjectPool(ObjectPool pool)
    {
        _objectPool = pool;
    }

    public IApplicationSpecification getApplicationSpecification()
    {
        return _applicationSpecification;
    }

    public void setApplicationSpecification(IApplicationSpecification specification)
    {
        _applicationSpecification = specification;
    }

    public IPageSource getPageSource()
    {
        return _pageSource;
    }

    public void setPageSource(IPageSource source)
    {
        _pageSource = source;
    }

    public ClassResolver getClassResolver()
    {
        return _classResolver;
    }

    public void setClassResolver(ClassResolver resolver)
    {
        _classResolver = resolver;
    }

    public ServiceMap getServiceMap()
    {
        return _serviceMap;
    }

    public void setServiceMap(ServiceMap serviceMap)
    {
        _serviceMap = serviceMap;
    }

    public RequestExceptionReporter getRequestExceptionReporter()
    {
        return _requestExceptionReporter;
    }

    public void setRequestExceptionReporter(RequestExceptionReporter requestExceptionReporter)
    {
        _requestExceptionReporter = requestExceptionReporter;
    }

    public ResponseRenderer getResponseRenderer()
    {
        return _responseRenderer;
    }

    public void setResponseRenderer(ResponseRenderer responseRenderer)
    {
        _responseRenderer = responseRenderer;
    }

    public LinkFactory getLinkFactory()
    {
        return _linkFactory;
    }

    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }

    public RequestCycleFactory getRequestCycleFactory()
    {
        return _requestCycleFactory;
    }

    public void setRequestCycleFactory(RequestCycleFactory requestCycleFactory)
    {
        _requestCycleFactory = requestCycleFactory;
    }
}