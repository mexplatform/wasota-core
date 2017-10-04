/**
 * 
 */
package wasota.core.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import wasota.rest.messages.WasotaRestModel;
import wasota.rest.messages.WasotaRestMsg;

/**
 * @author Ciro Baron Neto
 * 
 * Jul 4, 2016
 */

@RestController
public class RootController {
	
	/**
	 * Root page (can be used to test authentication)
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public WasotaRestModel  getAllContext() {
		return new WasotaRestModel(WasotaRestMsg.OK, "");
	}	
	

}
