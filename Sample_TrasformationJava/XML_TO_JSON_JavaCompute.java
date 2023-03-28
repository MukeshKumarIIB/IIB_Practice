 import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbBLOB;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;

public class XML_TO_JSON_JavaCompute extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		//MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		
		try {
			// create new message as a copy of the input
			MbMessage outMessage = new MbMessage();
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			
			// Add user code below
			
			MbElement inRoot = inMessage.getRootElement();
			MbElement outputRootElement = outMessage.getRootElement();
			byte[] inRootBytes = inRoot.getLastChild().toBitstream(null, null, null, 0, 1208, 0);
			String inRootString = new String(inRootBytes);
			
			String json=convertToJson(inRootString);
			
			
			
	       //outAssembly.getMessage().getRootElement().createElementAsLastChild("JSON").createElementAsLastChild(MbElement.TYPE_NAME,"Data",null).createElementAsLastChild(MbElement.TYPE_NAME,"Response",json);
	          
			/*
			 * MbElement outRoot=outMessage.getRootElement(); MbElement
			 * outJsonRoot=outRoot.createElementAsLastChild(MbJSON.PARSER_NAME); MbElement
			 * outJsonData=outJsonRoot.createElementAsLastChild(MbElement.TYPE_NAME,MbJSON.
			 * DATA_ELEMENT_NAME,null); MbElement
			 * outJsonTest=outJsonData.createElementAsLastChild(MbElement.TYPE_NAME,
			 * "Response",json);
			 */
			
			// Set the string data as the output message element value
			/*
			 * MbElement outputBodyElement =
			 * outputRootElement.createElementAsLastChild(MbElement.TYPE_VALUE,MbBLOB.
			 * PARSER_NAME, null);
			 * outputBodyElement.createElementAsLastChild(MbElement.TYPE_VALUE,MbBLOB.
			 * ROOT_ELEMENT_NAME, json);
			 */
	        
            
            
            outputRootElement.createElementAsLastChild(MbBLOB.PARSER_NAME).createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, MbBLOB.ROOT_ELEMENT_NAME, json.getBytes());

            
            
            
			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(), null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}
      private String convertToJson(String inRootString) throws JSONException {
		
    	JSONObject json = XML.toJSONObject(inRootString);   
        String jsonString = json.toString(); 
		
		return jsonString;
	}

	/**
	 * onPreSetupValidation() is called during the construction of the node
	 * to allow the node configuration to be validated.  Updating the node
	 * configuration or connecting to external resources should be avoided.
	 *
	 * @throws MbException
	 */
	@Override
	public void onPreSetupValidation() throws MbException {
	}

	/**
	 * onSetup() is called during the start of the message flow allowing
	 * configuration to be read/cached, and endpoints to be registered.
	 *
	 * Calling getPolicy() within this method to retrieve a policy links this
	 * node to the policy. If the policy is subsequently redeployed the message
	 * flow will be torn down and reinitialized to it's state prior to the policy
	 * redeploy.
	 *
	 * @throws MbException
	 */
	@Override
	public void onSetup() throws MbException {
	}

	/**
	 * onStart() is called as the message flow is started. The thread pool for
	 * the message flow is running when this method is invoked.
	 *
	 * @throws MbException
	 */
	@Override
	public void onStart() throws MbException {
	}

	/**
	 * onStop() is called as the message flow is stopped. 
	 *
	 * The onStop method is called twice as a message flow is stopped. Initially
	 * with a 'wait' value of false and subsequently with a 'wait' value of true.
	 * Blocking operations should be avoided during the initial call. All thread
	 * pools and external connections should be stopped by the completion of the
	 * second call.
	 *
	 * @throws MbException
	 */
	@Override
	public void onStop(boolean wait) throws MbException {
	}

	/**
	 * onTearDown() is called to allow any cached data to be released and any
	 * endpoints to be deregistered.
	 *
	 * @throws MbException
	 */
	@Override
	public void onTearDown() throws MbException {
	}

}
