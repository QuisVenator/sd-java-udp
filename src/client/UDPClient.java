package client;

import java.io.*;
import java.net.*;
import org.json.*;

public class UDPClient {

    public static void main(String[] args) throws IOException {

        Socket unSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            unSocket = new Socket("localhost", 4444);
            
            out = new PrintWriter(unSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(unSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de I/O en la conexion al host");
            System.exit(1);
        }

        
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;

        while ((fromServer = in.readLine()) != null) {
            System.out.println("Servidor: " + fromServer);
            
        	JSONObject data = new JSONObject();
        	JSONObject jo = new JSONObject();
        	System.out.println("Operación:");
        	int op = Integer.valueOf(stdIn.readLine());
        	jo.put("op", op);
        	
        	switch (op) {
        	case 1:
            	System.out.println("id_estacion: ");
            	data.put("id_estacion", stdIn.readLine());
            	System.out.println("ciudad: ");
            	data.put("ciudad", stdIn.readLine());
            	System.out.println("porcentaje_humedad: ");
            	data.put("porcentaje_humedad", stdIn.readLine());
            	System.out.println("temperatura (int): ");
            	data.put("temperatura", Integer.valueOf(stdIn.readLine()));
            	System.out.println("velocidad_viento: ");
            	data.put("velocidad_viento", stdIn.readLine());
            	System.out.println("fecha: ");
            	data.put("fecha", stdIn.readLine());
            	System.out.println("hora: ");
            	data.put("hora", stdIn.readLine());
                jo.put("data", data);
            	out.println(jo.toString());
            	break;
        	case 2:
            	System.out.println("ciudad: ");
        		data.put("ciudad", stdIn.readLine());
                jo.put("data", data);
            	out.println(jo.toString());
        		break;
        	case 3:
            	System.out.println("fecha: ");
        		data.put("fecha", stdIn.readLine());
                jo.put("data", data);
            	out.println(jo.toString());
        		break;
        	default:
        		System.out.println("Unknown operation - exiting client");
        	}
        	
        }

        out.close();
        in.close();
        stdIn.close();
        unSocket.close();
    }
}
