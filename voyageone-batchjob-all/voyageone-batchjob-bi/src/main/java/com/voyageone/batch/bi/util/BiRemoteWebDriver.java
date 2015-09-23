package com.voyageone.batch.bi.util;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;

public class BiRemoteWebDriver extends
		org.openqa.selenium.remote.RemoteWebDriver implements WebDriver,
		JavascriptExecutor, FindsById, FindsByClassName, FindsByLinkText,
		FindsByName, FindsByCssSelector, FindsByTagName, FindsByXPath,
		HasInputDevices, HasCapabilities, TakesScreenshot {

	public BiRemoteWebDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities, null);
	}

	@Override
	protected void startSession(Capabilities desiredCapabilities) {
		String sid = desiredCapabilities.getCapability("bi_sessionId").toString();
		if (sid != null) {
			setSessionId(sid);
			try {
				getCurrentUrl();
			} catch (WebDriverException e) {
			}
		}
		if (sid == null) {
			super.startSession(desiredCapabilities);
		}
	}

	@Override
	protected void startSession(Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
		String sid = desiredCapabilities.getCapability("bi_sessionId").toString();
		if (sid != null) {
			setSessionId(sid);
			try {
				getCurrentUrl();
			} catch (WebDriverException e) {
			}
		}
	}
	//
	// public void setSessionOpaqueKey(SessionId sessionId) {
	// super.setSessionId(sessionId.toString());
	// //this.sessionId = new SessionId(opaqueKey);
	// }
}
