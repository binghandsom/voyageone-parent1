package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.*;
import com.voyageone.components.channeladvisor.webservice.OrderServiceSoap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * <p>
 * An example of how this class may be used:
 * 
 * <pre>
 * OrderService service = new OrderService();
 * OrderServiceSoap portType = service.getOrderServiceSoap();
 * portType.setOrdersExportStatus(...);
 * </pre>
 * 
 * </p>
 * 
 */
@WebServiceClient(name = "OrderService", targetNamespace = "http://api.channeladvisor.com/webservices/", wsdlLocation = "https://api.channeladvisor.com/ChannelAdvisorAPI/v7/OrderService.asmx?WSDL")
public class OrderService extends Service {

	private final static URL ORDERSERVICE_WSDL_LOCATION;
	private final static Logger logger = Logger.getLogger(OrderService.class
			.getName());

	static {
		URL url = null;
		try {
			URL baseUrl;
			baseUrl = OrderService.class.getResource(".");
			url = new URL(baseUrl, "https://api.channeladvisor.com/ChannelAdvisorAPI/v7/OrderService.asmx?WSDL");
		} catch (MalformedURLException e) {
			logger.warning("Failed to create URL for the wsdl Location: 'https://api.channeladvisor.com/ChannelAdvisorAPI/v7/OrderService.asmx?WSDL', retrying as a local file");
			logger.warning(e.getMessage());
		}
		ORDERSERVICE_WSDL_LOCATION = url;
	}

	public OrderService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public OrderService() {
		super(ORDERSERVICE_WSDL_LOCATION, new QName("http://api.channeladvisor.com/webservices/", "OrderService"));
	}

	/**
	 * 
	 * @return returns OrderServiceSoap
	 */
	@WebEndpoint(name = "OrderServiceSoap")
	public com.voyageone.components.channeladvisor.webservice.OrderServiceSoap getOrderServiceSoap() {
		return super.getPort(new QName("http://api.channeladvisor.com/webservices/", "OrderServiceSoap"),
				com.voyageone.components.channeladvisor.webservice.OrderServiceSoap.class);
	}

	/**
	 *
	 * @return returns OrderServiceSoap
	 */
	@WebEndpoint(name = "OrderServiceSoap12")
	public com.voyageone.components.channeladvisor.webservice.OrderServiceSoap getOrderServiceSoap12() {
		return super.getPort(new QName("http://api.channeladvisor.com/webservices/", "OrderServiceSoap12"),
				com.voyageone.components.channeladvisor.webservice.OrderServiceSoap.class);
	}

}