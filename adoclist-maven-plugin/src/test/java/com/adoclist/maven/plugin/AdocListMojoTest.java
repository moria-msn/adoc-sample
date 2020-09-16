package com.adoclist.maven.plugin;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdocListMojoTest {

    private AdocListMojo mojo = new AdocListMojo();

    @Rule
    public TemporaryFolder outputFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void test_execute() throws MojoExecutionException {
        mojo.setConfigFile("src/test/resources/config/config.json");
        mojo.execute();

    }

    @Test
    public void test_executeWithError() throws MojoExecutionException {
        mojo.setConfigFile("config-error-syntax.json");
        try {
            mojo.execute();
            assertTrue(false);
        } catch (MojoExecutionException e) {
            assertTrue(true);
        }
    }

    @Test
    public void test_executeWithError2() {
        mojo.setConfigFile("config-error-noConfigs.json");
        try {
            mojo.execute();
            assertTrue(false);
        } catch (MojoExecutionException e) {
            assertTrue(true);
        }
    }
}
