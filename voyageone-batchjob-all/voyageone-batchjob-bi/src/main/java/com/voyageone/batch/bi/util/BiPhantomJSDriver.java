package com.voyageone.batch.bi.util;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;

public class BiPhantomJSDriver extends PhantomJSDriver implements TakesScreenshot {

	public BiPhantomJSDriver() {
		super();
    }

    public BiPhantomJSDriver(Capabilities desiredCapabilities) {
    	super(desiredCapabilities);
    }

    public BiPhantomJSDriver(PhantomJSDriverService service, Capabilities desiredCapabilities) {
    	super(service, desiredCapabilities);
    }
    
	@Override
	public <X> X getScreenshotAs(OutputType<X> paramOutputType) throws WebDriverException {
		return super.getScreenshotAs(paramOutputType);
	}

	boolean isClose =false;
	@Override
	public void quit() {
		if (!isClose) {
			super.quit();
			isClose = true;
		}
	}
}
