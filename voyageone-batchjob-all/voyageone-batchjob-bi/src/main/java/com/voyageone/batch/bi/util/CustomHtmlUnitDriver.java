package com.voyageone.batch.bi.util;

import com.gargoylesoftware.htmlunit.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class CustomHtmlUnitDriver extends HtmlUnitDriver {

	private boolean isPageChanged = false;
	public boolean isPageChanged() {
		return isPageChanged;
	}
	public void setPageChanged(boolean isPageChanged) {
		this.isPageChanged = isPageChanged;
	}



	public CustomHtmlUnitDriver(BrowserVersion version) {
		super(version);
	}
	
	// This is the magic. Keep a reference to the client instance
	protected WebClient modifyWebClient(WebClient client) {
		client.addWebWindowListener(new WebWindowListener() {
			public void webWindowOpened(WebWindowEvent webWindowEvent) {}

			public void webWindowContentChanged(WebWindowEvent event) {
				setPageChanged(true);
			}

			public void webWindowClosed(WebWindowEvent event) {}
		});
		
		return client;
	}

	public WebResponse getResponse() {
		Page page = lastPage();
		if (page == null) {
			return null;
		}
		return page.getWebResponse();
	}

}