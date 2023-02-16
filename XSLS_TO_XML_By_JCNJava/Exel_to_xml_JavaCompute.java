import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

//import com.ibm.broker.config.appdev.nodes.LoopbackRequestNode.Row;
import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;
import com.ibm.broker.plugin.MbXMLNSC;

public class Exel_to_xml_JavaCompute extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		// MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
			MbMessage outMessage = new MbMessage(inMessage);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			// Add user code below
			// fetch all data from file and convert into blob
			MbElement blob = inAssembly.getMessage().getRootElement().getLastChild();
			// converting into byte
			byte[] orgMSG = (byte[]) blob.getLastChild().getValue();

			// converting to stream
			InputStream istream = new ByteArrayInputStream(orgMSG);
			xlsxParser(istream, outMessage);

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
			// Example handling ensures all exceptions are re-thrown to be handled in the
			// flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(), null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}

	public static void xlsxParser(InputStream fis, MbMessage outMessage) {

		// MbMessage outMessage = null;
		MbElement outRoot = null;
		MbElement outBody = null;
		MbElement excelRoot = null;
		XSSFWorkbook myWorkBook = null;
		MbElement rowMsgElement = null;
		try {
			outRoot = outMessage.getRootElement();

			DataFormatter formatter = new DataFormatter();

			// create XMNLSC parser

			outBody = outRoot.createElementAsLastChild(MbXMLNSC.PARSER_NAME);
			// Create root element.
			excelRoot = outBody.createElementAsLastChild(MbElement.TYPE_NAME, "Result", null);

			// Finds the workbook instance for XLSX file
			myWorkBook = new XSSFWorkbook(fis);

			// Return first sheet from the XLSX workbook
			XSSFSheet mySheet = myWorkBook.getSheetAt(0);

			Iterator<Row> rowIterator = mySheet.iterator();

			while (rowIterator.hasNext()) {
				Row row =rowIterator.next();

				// create a row element for current excel row.

				rowMsgElement = excelRoot.createElementAsLastChild(MbElement.TYPE_NAME, "row", null);

				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();
					String text = formatter.formatCellValue(cell);

					switch (cell.getCellType()) {

					case 1:

						rowMsgElement.createElementAsLastChild(MbElement.TYPE_NAME, "lastName", text);

						break;
					case 2:

						rowMsgElement.createElementAsLastChild(MbElement.TYPE_NAME, "FirstName", text);

						break;
					case 3:

						rowMsgElement.createElementAsLastChild(MbElement.TYPE_NAME, "Status", text);

						break;
					case 4:

						rowMsgElement.createElementAsLastChild(MbElement.TYPE_NAME, "Salary", text);

						break;
					default:

						rowMsgElement.createElementAsLastChild(MbElement.TYPE_NAME, "cell", text);

					}
				}
			}
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (MbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// create an element called cell in output message with value as cell value
		// rowMsgElement.createElementAsLastChild(MbElement.TYPE_NAME,"cell",text);

	}

	/**
	 * onPreSetupValidation() is called during the construction of the node to allow
	 * the node configuration to be validated. Updating the node configuration or
	 * connecting to external resources should be avoided.
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
	 * Calling getPolicy() within this method to retrieve a policy links this node
	 * to the policy. If the policy is subsequently redeployed the message flow will
	 * be torn down and reinitialized to it's state prior to the policy redeploy.
	 *
	 * @throws MbException
	 */
	@Override
	public void onSetup() throws MbException {
	}

	/**
	 * onStart() is called as the message flow is started. The thread pool for the
	 * message flow is running when this method is invoked.
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
