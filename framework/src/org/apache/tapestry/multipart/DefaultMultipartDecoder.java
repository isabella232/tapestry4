/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.multipart;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.IUploadFile;

/**
 *  Decodes the data in a <code>multipart/form-data</code> HTTP request, handling
 *  file uploads and multi-valued parameters.  After decoding, the class is used
 *  to access the parameter values.
 * 
 *  <p>This implementation is a thin wrapper around the Apache Jakarta
 *  <a href="http://jakarta.apache.org/commons/fileupload/">FileUpload</a>. 
 *  
 *  <p>Supports single valued parameters, multi-valued parameters and individual
 *  file uploads.  That is, for file uploads, each upload must be a unique parameter
 *  (that is all the {@link org.apache.tapestry.form.Upload} component needs).

 *
 *  @author Joe Panico
 *  @version $Id$
 *  @since 2.0.1
 *
 **/
public class DefaultMultipartDecoder implements IMultipartDecoder
{
    private static final Log LOG = LogFactory.getLog(DefaultMultipartDecoder.class);

    /**
     *  Request attribute key used to store the part map for this request.
     *  The part map is created in {@link #decode(HttpServletRequest)}.  By storing
     *  the part map in the request instead of an instance variable, DefaultMultipartDecoder
     *  becomes threadsafe (no client-specific state in instance variables).
     * 
     **/

    public static final String PART_MAP_ATTRIBUTE_NAME = "org.apache.tapestry.multipart.part-map";

    private int _maxSize = 10000000;
    private int _thresholdSize = 1024;
    private String _repositoryPath = System.getProperty("java.io.tmpdir");

    private static DefaultMultipartDecoder _shared;

    public static DefaultMultipartDecoder getSharedInstance()
    {
        if (_shared == null)
            _shared = new DefaultMultipartDecoder();

        return _shared;
    }

    public void setMaxSize(int maxSize)
    {
        _maxSize = maxSize;
    }

    public int getMaxSize()
    {
        return _maxSize;
    }

    public void setThresholdSize(int thresholdSize)
    {
        _thresholdSize = thresholdSize;
    }

    public int getThresholdSize()
    {
        return _thresholdSize;
    }

    public void setRepositoryPath(String repositoryPath)
    {
        _repositoryPath = repositoryPath;
    }

    public String getRepositoryPath()
    {
        return _repositoryPath;
    }

    public static boolean isMultipartRequest(HttpServletRequest request)
    {
        return FileUpload.isMultipartContent(request);
    }

    /**
     *  Invokes {@link IPart#cleanup()} on each part.
     * 
     **/
    public void cleanup(HttpServletRequest request)
    {
        Map partMap = getPartMap(request);

        Iterator i = partMap.values().iterator();
        while (i.hasNext())
        {
            IPart part = (IPart) i.next();
            part.cleanup();
        }
    }

    /**
     * Decodes the request, storing the part map (keyed on query parameter name, 
     * value is {@link IPart} into the request as an attribute.
     * 
     * @throws ApplicationRuntimeException if decode fails, for instance the
     * request exceeds getMaxSize()
     * 
     **/

    public void decode(HttpServletRequest request)
    {
        Map partMap = new HashMap();

        request.setAttribute(PART_MAP_ATTRIBUTE_NAME, partMap);

        // FileUpload is not quite threadsafe, so we create a new instance
        // for each request.

        FileUpload upload = new FileUpload();

        List parts = null;

        try
        {
            parts = upload.parseRequest(request, _thresholdSize, _maxSize, _repositoryPath);
        }
        catch (FileUploadException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("DefaultMultipartDecoder.unable-to-decode", ex.getMessage()),
                ex);
        }

        int count = Tapestry.size(parts);

        for (int i = 0; i < count; i++)
        {
            FileItem uploadItem = (FileItem) parts.get(i);

            if (uploadItem.isFormField())
            {
                String name = uploadItem.getFieldName();
                ValuePart valuePart = (ValuePart) partMap.get(name);
                if (valuePart != null)
                {
                    valuePart.add(uploadItem.getString());
                }
                else
                {
                    valuePart = new ValuePart(uploadItem.getString());
                    partMap.put(name, valuePart);
                }
            }
            else
            {
                UploadPart uploadPart = new UploadPart(uploadItem);

                partMap.put(uploadItem.getFieldName(), uploadPart);
            }
        }

    }

    public String getString(HttpServletRequest request, String name)
    {
        Map partMap = getPartMap(request);

        ValuePart part = (ValuePart) partMap.get(name);
        if (part != null)
            return part.getValue();

        return null;
    }

    public String[] getStrings(HttpServletRequest request, String name)
    {
        Map partMap = getPartMap(request);

        ValuePart part = (ValuePart) partMap.get(name);
        if (part != null)
            return part.getValues();

        return null;
    }

    public IUploadFile getUploadFile(HttpServletRequest request, String name)
    {
        Map partMap = getPartMap(request);

        return (IUploadFile) partMap.get(name);
    }

    private Map getPartMap(HttpServletRequest request)
    {
        return (Map) request.getAttribute(PART_MAP_ATTRIBUTE_NAME);
    }

}
