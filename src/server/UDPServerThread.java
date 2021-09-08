package server;
import java.net.*;
import java.util.ArrayList;
import java.io.*;
import org.json.*;

public class UDPServerThread extends Thread {

    private Socket socket = null;

    UDPMultiServer servidor;
    ArrayList<JSONObject> data = new ArrayList<>();
    
    public UDPServerThread(Socket socket, UDPMultiServer servidor ) {
        super("TCPServerHilo");
        this.socket = socket;
        this.servidor = servidor;
    }

    public void run() {

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("Bienvenido!");
            String inputLine, outputLine = "";

            while ((inputLine = in.readLine()) != null) {
            	System.out.println(inputLine);
                JSONObject object = new JSONObject(inputLine);
                
                int temp = 0;
                switch (object.getInt("op")) {
                case 1:
                	data.add(object.getJSONObject("data"));
                	outputLine = "{\"result\": \"added\"}";
                	break;
                case 2:
                	boolean exist = false;
                	for (JSONObject datapoint : data) {
                		if(datapoint.getString("ciudad").compareTo(object.getJSONObject("data").getString("ciudad")) == 0) {
                			temp = datapoint.getInt("temperatura");
                			exist = true;
                		}
                	}
                	outputLine = exist ? "{\"result\": "+temp+"}" : "{\"error\": \"ciudad no existe\"}";
                	break;
                case 3:
                	int count = 0;
                	for (JSONObject datapoint : data) {
                		if(datapoint.getString("fecha").compareTo(object.getJSONObject("data").getString("fecha")) == 0) {
                			temp += datapoint.getInt("temperatura");
                			count++;
                		}
                	}
                	outputLine = "{\"result\": "+temp/count+"}";
                	break;
                default:
                	outputLine = "{\"error\": \"unknown op type\"}";
                }
                
                out.println(outputLine);
            }
            out.close();
            in.close();
            socket.close();
            System.out.println("Finalizando Hilo");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

