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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.IUploadFile;

import org.apache.commons.fileupload.DefaultFileItem;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Portion of a multi-part request representing an uploaded file.
 *
 *  @author Joe Panico
 *  @version $Id$
 *  @since 2.0.1
 *
 **/
public class UploadPart extends Object implements IUploadFile, IPart
{
    private static final Log LOG = LogFactory.getLog(UploadPart.class);

    FileItem _fileItem;

    public UploadPart(FileItem fileItem)
    {
        if (fileItem == null)
            throw new IllegalArgumentException(
                Tapestry.getString("invalid-null-parameter", "fileItem"));

        _fileItem = fileItem;
    }

    public static UploadPart newInstance(
        String path,
        String name,
        String contentType,
        int requestSize,
        int threshold)
    {
        FileItem fileItem =
            DefaultFileItem.newInstance(path, name, contentType, requestSize, threshold);

        if (fileItem == null)
            return null;

        return new UploadPart(fileItem);
    }

    public String getContentType()
    {
        return _fileItem.getContentType();
    }

    /**
     *  Leverages {@link File} to convert the full file path and extract
     *  the name.
     * 
     **/
    public String getFileName()
    {
        File file = new File(this.getFilePath());

        return file.getName();
    }

    /**
     *  @since 2.0.4
     * 
     **/

    public String getFilePath()
    {
        return _fileItem.getName();
    }

    public InputStream getStream()
    {
        try
        {
            return _fileItem.getInputStream();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "UploadPart.unable-to-open-content-file",
                    _fileItem.getName(),
                    _fileItem.getStoreLocation()),
                ex);
        }
    }

    /**
     *  Deletes the external content file, if one exists.
     * 
     **/

    public void cleanup()
    {
        _fileItem.delete();

        // unfortunately FileItem.delete() does not signal success, so
        // we rely on a little more internal knowledge than is ideal

        File file = _fileItem.getStoreLocation();
        if (file != null && file.exists())
            throw new ApplicationRuntimeException(
                Tapestry.getString("UploadPart.temporary-file-not-deleted", file));
    }

}
