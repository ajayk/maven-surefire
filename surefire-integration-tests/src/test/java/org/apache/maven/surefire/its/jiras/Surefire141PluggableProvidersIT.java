package org.apache.maven.surefire.its.jiras;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.surefire.its.fixture.SurefireIntegrationTestCase;

/**
 * SUREFIRE-613 Asserts proper test counts when running in parallel
 *
 * @author Kristian Rosenvold
 */
public class Surefire141PluggableProvidersIT
    extends SurefireIntegrationTestCase
{
    public void testParallelBuildResultCount()
        throws Exception
    {
        unpack( "surefire-141-pluggableproviders/test-provider" ).executeInstall();

        unpack( "surefire-141-pluggableproviders/test" ).setEOption().executeTest()
        .verifyTextInLog( "Using configured provider org.apache.maven.surefire.testprovider.TestProvider" )
        .verifyTextInLog( "Using configured provider org.apache.maven.surefire.junit.JUnit3Provider" )
        .verifyErrorFreeLog();
    }
}