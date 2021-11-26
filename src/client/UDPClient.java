package client;

import java.io.*;
import java.net.*;
import org.json.*;

public class UDPClient {
	public static void main(String[] args) throws IOException {
		// Datos necesario
		String direccionServidor = "127.0.0.1";

		if (args.length > 0) {
			direccionServidor = args[0];
		}

		int puertoServidor = 9876;

		try {
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

			DatagramSocket clientSocket = new DatagramSocket();

			InetAddress IPAddress = InetAddress.getByName(direccionServidor);
			System.out.println("Intentando conectar a = " + IPAddress + ":" + puertoServidor +  " via UDP...");

			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
                
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
				sendData = jo.toString().getBytes();
				break;
			case 2:
				System.out.println("ciudad: ");
				data.put("ciudad", stdIn.readLine());
				jo.put("data", data);
				sendData = jo.toString().getBytes();
				break;
			case 3:
				System.out.println("fecha: ");
				data.put("fecha", stdIn.readLine());
				jo.put("data", data);
				sendData = jo.toString().getBytes();
				break;
			default:
				System.out.println("Unknown operation");
			}

			DatagramPacket sendPacket =
					new DatagramPacket(sendData, sendData.length, IPAddress, puertoServidor);

			clientSocket.send(sendPacket);

			DatagramPacket receivePacket =
					new DatagramPacket(receiveData, receiveData.length);

			//Vamos a hacer una llamada BLOQUEANTE entonces establecemos un timeout maximo de espera
			clientSocket.setSoTimeout(10000);

			try {
				// ESPERAMOS LA RESPUESTA, BLOQUENTE
				clientSocket.receive(receivePacket);

				String respuesta = new String(receivePacket.getData());
				
				InetAddress returnIPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();

				System.out.println("Respuesta desde =  " + returnIPAddress + ":" + port + ": " + respuesta);
			} catch (SocketTimeoutException ste) {
				System.out.println("TimeOut: El paquete udp se asume perdido.");
			}
			clientSocket.close();
		} catch (UnknownHostException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}
    }
}
