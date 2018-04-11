package org.mz.event;



import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CloudServlet
 */
@WebServlet("/CloudServlet")
public class CloudServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String GOOGLE_SERVER_KEY="AIzaSyAtgbZaVRW6lVBpJlVZmLVI4LDJXazA1n8";
	private Result result;



	/**
	 * Default constructor. 
	 */
	public CloudServlet() {
		// TODO Auto-generated constructor stub

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.print("hello");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String action=request.getParameter("action");
		System.out.print(action);
		if("internal".equalsIgnoreCase(action)){
			try{
				String userMessage1 = request.getParameter("message");
				System.out.println(userMessage1);
				String toName=request.getParameter("toName");

				Sender sender = new Sender(GOOGLE_SERVER_KEY);
		 
		
				Message message = new Message.Builder()
						.delayWhileIdle(true).addData("MESSAGE_KIES", userMessage1).build();
				Map<String, String> regIdMap = readFromFile();
				String regId = regIdMap.get(toName);
                   System.out.print(regId);
				result = sender.send(message,regId , 1);
				request.setAttribute("pushStatus", result.toString());
				System.out.println("byee");
				System.out.print(result);
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
				request.setAttribute("pushStatus",
						"RegId required: " + ioe.toString());
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("pushStatus", e.toString());
			}

		}

			if("shareRegID".equalsIgnoreCase(action)){
				writeToFile(request.getParameter("fromUserName"),request.getParameter("regId"));
				request.setAttribute("pushStatus","GCM Name and correspondind RegId Received");

			}
			if("SendMessage".equalsIgnoreCase(action)){
                   System.out.print("bababbab");
				try {
					
					String toName = request.getParameter("toUserName");
					String userMessage = request.getParameter("message");

					Sender sender = new Sender(GOOGLE_SERVER_KEY);
					Message message = new Message.Builder()
							.delayWhileIdle(true).addData("MESSAGE_KEY", userMessage).build();
					Map<String, String> regIdMap = readFromFile();
					String regId = regIdMap.get(toName);
					
					result = sender.send(message, regId, 1);
					request.setAttribute("pushStatus", result.toString());
					System.out.println(result.toString());
				} catch (IOException ioe) {
					ioe.printStackTrace();
					request.setAttribute("pushStatus",
							"RegId required: " + ioe.toString());
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("pushStatus", e.toString());
				}
				
		}
	}
	static String line;

	private void writeToFile(String parameter, String parameter2)throws IOException {
		// TODO Auto-generated method stub
		Map<String, String> regIdMap = readFromFile();
		regIdMap.put(parameter, parameter2);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("D:/cloud.txt", false)));
		for (Map.Entry<String, String> entry : regIdMap.entrySet()) 
		{
			System.out.println(entry.getKey()+","+entry.getValue());
			out.println(entry.getKey() + "," + entry.getValue());
		}

		out.close();
	}
	private Map<String, String> readFromFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("D:/cloud.txt"));
		String regIdLine = "";

		Map<String, String> regIdMap = new HashMap<String, String>();
		while ((regIdLine = br.readLine()) != null) {
			String[] regArr = regIdLine.split(",");

			regIdMap.put(regArr[0], regArr[1]);
		}
		br.close();
		return regIdMap;
	}

}

