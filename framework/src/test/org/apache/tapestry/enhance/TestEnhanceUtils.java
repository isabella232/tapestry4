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

package org.apache.tapestry.enhance;

import java.util.Map;

import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestEnhanceUtils extends HiveMindTestCase
{
    public void testTypeUnspecifiedWithNoExistingProperty()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType("wilma");
        opc.setReturnValue(null);

        replayControls();

        Class result = EnhanceUtils.extractPropertyType(op, "wilma", null);

        assertEquals(Object.class, result);

        verifyControls();
    }

    public void testTypeUnspecifiedButExistingProperty()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType("fred");
        opc.setReturnValue(Map.class);

        replayControls();

        Class result = EnhanceUtils.extractPropertyType(op, "fred", null);

        assertEquals(Map.class, result);

        verifyControls();
    }

    public void testTypeSpecified()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.convertTypeName("int[]");
        opc.setReturnValue(int[].class);

        op.validateProperty("betty", int[].class);

        replayControls();

        Class result = EnhanceUtils.extractPropertyType(op, "betty", "int[]");

        assertEquals(int[].class, result);

        verifyControls();

    }
}