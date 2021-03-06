package org.apache.maven.surefire.its.fixture;

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

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;

import java.io.File;
import java.util.List;

/**
 * A specialized verifier that enforces a standard use case for surefire IT's
 *
 * @author Kristian Rosenvold
 */
public class OutputValidator
{
    protected final Verifier verifier;
    
    protected final File baseDir;

    public OutputValidator( Verifier verifier )
    {
        this.verifier = verifier;
        this.baseDir = new File( verifier.getBasedir());

    }

    public OutputValidator verifyTextInLog( String text )
    {
        try
        {
            verifier.verifyTextInLog( text );
        }
        catch ( VerificationException e )
        {
            throw new SurefireVerifierException( e );
        }
        return this;
    }


    public OutputValidator verifyErrorFreeLog()
    {
        try
        {
            verifier.verifyErrorFreeLog();
        }
        catch ( VerificationException e )
        {
            throw new SurefireVerifierException( e );
        }
        return this;
    }

    public OutputValidator verifyErrorFree(int total)
    {
        try
        {
            verifier.verifyErrorFreeLog();
            this.assertTestSuiteResults( total, 0, 0, 0 );
            return this;
        }
        catch ( VerificationException e )
        {
            throw new SurefireVerifierException( e );
        }
    }

    public List<String> loadFile( File file, boolean hasCommand )
    {
        //noinspection unchecked
        try
        {
            //noinspection unchecked
            return verifier.loadFile( file, hasCommand );
        }
        catch ( VerificationException e )
        {
            throw new SurefireVerifierException( e );
        }
    }


    public String getBasedir()
    {
        return verifier.getBasedir();
    }

    /**
     * Returns a file, referenced from the extracted root (where pom.xml is located)
     *
     * @param path The subdirectory under basedir
     * @return A file
     */
    public File getSubFile( String path )
    {
        return new File( getBasedir(), path );
    }


    public String getArtifactPath( String org, String name, String version, String ext )
    {
        return verifier.getArtifactPath( org, name, version, ext );
    }

    
    public OutputValidator assertTestSuiteResults( int total, int errors, int failures, int skipped )
    {
        HelperAssertions.assertTestSuiteResults( total, errors, failures, skipped, baseDir );
        return this;
    }

    public TestFile getTargetFile( String fileName )
    {
        File targetDir = getSubFile( "target" );
        return new TestFile(new File( targetDir, fileName ), this);
    }


    public TestFile getSurefireReportsFile( String fileName )
    {
        File targetDir = getSubFile( "target/surefire-reports" );
        return new TestFile(new File( targetDir, fileName ), this);
    }

    public TestFile getSiteFile( String fileName )
    {
        File targetDir = getSubFile( "target/site" );
        return new TestFile(new File( targetDir, fileName ), this);
    }



    public File getBaseDir()
    {
        return baseDir;
    }

    @SuppressWarnings("unchecked")
    private List<String> getLog()
        throws VerificationException
    {
        return verifier.loadFile( verifier.getBasedir(), verifier.getLogFileName(), false );
    }

    public boolean stringsAppearInSpecificOrderInLog( String[] strings )
        throws VerificationException
    {
        int i = 0;
        for ( String line : getLog() )
        {
            if ( line.startsWith( strings[i] ) )
            {
                if ( i == strings.length - 1 )
                {
                    return true;
                }
                ++i;
            }
        }
        return false;
    }

}
