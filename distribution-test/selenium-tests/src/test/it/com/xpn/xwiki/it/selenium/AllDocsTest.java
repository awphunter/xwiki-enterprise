/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xpn.xwiki.it.selenium;

import junit.framework.Assert;
import junit.framework.Test;

import com.xpn.xwiki.it.selenium.framework.AbstractXWikiTestCase;
import com.xpn.xwiki.it.selenium.framework.ColibriSkinExecutor;
import com.xpn.xwiki.it.selenium.framework.XWikiTestSuite;

/**
 * Verify the table view for AllDocs wiki document.
 * 
 * @version $Id$
 */
public class AllDocsTest extends AbstractXWikiTestCase
{
    public static Test suite()
    {
        XWikiTestSuite suite = new XWikiTestSuite("Verify the table view for AllDocs wiki document");
        suite.addTestSuite(AllDocsTest.class, ColibriSkinExecutor.class);
        return suite;
    }

    /**
     * This method makes the following tests :
     * <ul>
     * <li>Validate presence of "Actions" column in table view for administrator.</li>
     * <li>Validate absence of "Actions" column for users without administration rights.</li>
     * <li>Validate input suggest for Page field.</li>
     * <li>Validate input suggest for Space field.</li>
     * <li>Validate input suggest for Last Author field.</li>
     * <li>Validate Copy link action.</li>
     * <li>Validate Rename link action.</li>
     * <li>Validate Delete link action.</li>
     * <li>Validate Rights link action.</li>
     * </ul>
     */
    public void testTableViewActions()
    {
        // Validate absence of "Actions" column for users without administration rights and verify there are
        // elements in the table
        open("Main", "AllDocs");
        // We verify we have a least 3 pages displayed
        waitForTextContains("//span[@class='xwiki-livetable-pagination-content']", "1 2 3");
        if (isAuthenticated()) {
            logout();
        }
        assertElementNotPresent("//td[text()='Actions']");

        // Validate presence of "Actions" column in table view for administrator.
        loginAsAdmin();
        open("Main", "AllDocs");
        waitForTextContains("//span[@class='xwiki-livetable-pagination-content']", "1 2 3");
        assertElementPresent("//th[normalize-space(text())='Actions']");

        // Validate input suggest for Page field.
        getSelenium().typeKeys("xpath=//input[@name='doc.name']", "Treeview");
        // Note: We wait on the pagination result since it's the last element updated and it's done after the
        // table rows have been updated so this allows us to wait on it. In the code below "1" represents the
        // displayed pages.
        waitForTextPresent("//span[@class='xwiki-livetable-pagination-content']", "1");
        assertElementPresent("//td[contains(@class, 'doc_name')]/a[text()='Treeview']");

        // Validate input suggest for Space field.
        open("Main", "AllDocs");
        waitForTextContains("//span[@class='xwiki-livetable-pagination-content']", "1 2 3");
        getSelenium().typeKeys("xpath=//input[@name='doc.space']", "XWiki");
        getSelenium().typeKeys("xpath=//input[@name='doc.name']", "treeview");
        waitForTextPresent("//span[@class='xwiki-livetable-pagination-content']", "1");
        assertElementPresent("//td[contains(@class, 'doc_name')]/a[text()='Treeview']");

        // Validate input suggest for Last Author field.
        open("Main", "AllDocs");
        waitForTextContains("//span[@class='xwiki-livetable-pagination-content']", "1 2 3");
        getSelenium().typeKeys("xpath=//input[@name='doc.author']", "SomeUnknownAuthor");
        waitForTextPresent("//span[@class='xwiki-livetable-pagination-content']", "");
        assertElementNotPresent("//td[contains(@class, 'doc_name')]/a[text()='Treeview']");

        // Validate Copy link action.
        open("Main", "AllDocs");
        waitForTextContains("//span[@class='xwiki-livetable-pagination-content']", "1 2 3");
        getSelenium().typeKeys("xpath=//input[@name='doc.name']", "treeview");
        waitForTextPresent("//span[@class='xwiki-livetable-pagination-content']", "1");
        assertElementPresent("//td[contains(@class, 'doc_name')]/a[text()='Treeview']");
        clickLinkWithText("copy");
        setFieldValue("targetdoc", "New.TreeviewNew");
        clickLinkWithLocator("//input[@value='Copy']");
        open("Main", "AllDocs");
        getSelenium().typeKeys("xpath=//input[@name='doc.space']", "New");
        getSelenium().typeKeys("xpath=//input[@name='doc.name']", "treeviewnew");
        waitForTextPresent("//span[@class='xwiki-livetable-pagination-content']", "1");
        assertElementPresent("//td[contains(@class, 'doc_name')]/a[text()='TreeviewNew']");

        // Validate Rename link action.
        open("Main", "AllDocs");
        waitForTextContains("//span[@class='xwiki-livetable-pagination-content']", "1 2 3");
        getSelenium().typeKeys("xpath=//input[@name='doc.name']", "TreeviewNew");
        waitForTextPresent("//span[@class='xwiki-livetable-pagination-content']", "1");
        clickLinkWithLocator("//tbody/tr/td/a[text()='rename']");
        setFieldValue("newPageName", "TreeviewNewRenamed");
        clickLinkWithLocator("//input[@value='Rename']");
        open("Main", "AllDocs");
        getSelenium().typeKeys("xpath=//input[@name='doc.name']", "TreeviewNewRenamed");
        waitForTextPresent("//span[@class='xwiki-livetable-pagination-content']", "1");
        assertElementPresent("//td[contains(@class, 'doc_name')]/a[text()='TreeviewNewRenamed']");

        // Validate Delete link action.
        open("Main", "AllDocs");
        waitForTextContains("//span[@class='xwiki-livetable-pagination-content']", "1 2 3");
        getSelenium().typeKeys("xpath=//input[@name='doc.name']", "Treeviewnewrenamed");
        waitForTextPresent("//span[@class='xwiki-livetable-pagination-content']", "1");
        clickLinkWithLocator("//tbody/tr/td/a[text()='delete']");
        clickLinkWithLocator("//input[@value='yes']");
        assertTextPresent("The document has been deleted.");
        open("Main", "AllDocs");
        getSelenium().typeKeys("xpath=//input[@name='doc.name']", "treeview");
        waitForTextPresent("//span[@class='xwiki-livetable-pagination-content']", "1");
        assertElementNotPresent("//td[contains(@class, 'doc_name')]/a[text()='TreeviewNewRenamed']");

        // Validate Rights link action.
        open("Main", "AllDocs");
        waitForTextContains("//span[@class='xwiki-livetable-pagination-content']", "1 2 3");
        getSelenium().typeKeys("xpath=//input[@name='doc.name']", "Treeview");
        waitForTextPresent("//span[@class='xwiki-livetable-pagination-content']", "1");
        clickLinkWithLocator("//tbody/tr/td/a[text()='rights']");
        Assert.assertEquals("Editing Rights for Tree", getTitle());
    }

    /**
     * Validate that space nodes are loaded by the Treeview widget.
     */
    public void testTreeViewInit()
    {
        open("Main", "AllDocs", "view", "view=tree");

        // Wait for the widget to load.
        waitForCondition("typeof selenium.browserbot.getCurrentWindow().Treeview != 'undefined'");

        // Wait for the data to arrive.
        waitForNodeToLoad("xwiki:Blog");
        waitForNodeToLoad("xwiki:Main");
        waitForNodeToLoad("xwiki:Panels");
        waitForNodeToLoad("xwiki:Sandbox");
        waitForNodeToLoad("xwiki:Scheduler");
        waitForNodeToLoad("xwiki:Stats");
        waitForNodeToLoad("xwiki:XWiki");

        // We can't use Selenium to generate events (clicks and keys) for Smartclient widgets.
        // See this thread for more details: http://forums.smartclient.com/showthread.php?t=2312
    }

    /**
     * Validate that the suggest allow to open a node further levels down the tree.
     */
    public void testTreeViewSuggest()
    {
        open("Main", "AllDocs", "view", "view=tree");

        // Wait for the widget to load.
        waitForCondition("typeof selenium.browserbot.getCurrentWindow().Treeview != 'undefined'");
        waitForNodeToLoad("xwiki:XWiki");

        setFieldValue("Treeview_Input", "Main.RecentChanges");
        waitForNodeToLoad("xwiki:Main.RecentChanges");
    }

    /**
     * Validate Treeview API.
     */
    public void testTreeViewAPI()
    {
        open("Main", "AllDocs", "view", "view=tree");

        // Wait for the widget to load.
        waitForCondition("typeof selenium.browserbot.getCurrentWindow().Treeview != 'undefined'");
        waitForNodeToLoad("xwiki:Main");

        setFieldValue("Treeview_Input", "Main.RecentChanges@lquo.gif");
        waitForNodeToLoad("xwiki:Main.RecentChanges@lquo.gif");

        waitForCondition("selenium.browserbot.getCurrentWindow().Treeview.getSelectedResourceProperty('wiki') " +
        		"== 'xwiki'");
        waitForCondition("selenium.browserbot.getCurrentWindow().Treeview.getSelectedResourceProperty('space') " +
        		"== 'Main'");
        waitForCondition("selenium.browserbot.getCurrentWindow().Treeview.getSelectedResourceProperty('name') " +
        		"== 'RecentChanges'");
        waitForCondition("selenium.browserbot.getCurrentWindow().Treeview.getSelectedResourceProperty('attachment') " +
        		"== 'lquo.gif'");
        waitForCondition("selenium.browserbot.getCurrentWindow().Treeview.getSelectedResourceProperty('anchor') == ''");
        waitForCondition("selenium.browserbot.getCurrentWindow().Treeview.getValue() == 'Main.RecentChanges@lquo.gif'");
    }

    /**
     * Click on a node in the Treeview widget.
     *
     * @param nodeId Id of the node to be clicked.
     */
    private void clickOnNode(String nodeId)
    {

        getSelenium().click("//img[@name='" + nodeId + "']");
    }

    /**
     * Wait for the node with the given ID to load.
     *
     * @param nodeId Id of the node to wait for.
     */
    private void waitForNodeToLoad(String nodeId)
    {
        waitForCondition("typeof selenium.browserbot.getCurrentWindow().Treeview.data.findById('" + nodeId
            + "') != 'undefined'");
    }
}
