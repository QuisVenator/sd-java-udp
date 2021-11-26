package server;

import java.net.*;
import org.json.*;
import java.io.*;
import java.util.*;

public class UDPServer {
    public static ArrayList<JSONObject> data = new ArrayList<>();

	public static void main(String[] a){
        // Variables
        int puertoServidor = 9876;
        
        try {
            //1) Creamos el socket Servidor de Datagramas (UDP)
            DatagramSocket serverSocket = new DatagramSocket(puertoServidor);
			System.out.println("Servidor Sistemas Distribuidos - UDP ");
			
            //2) buffer de datos a enviar y recibir
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
			
            //3) Servidor siempre esperando
            while (true) {
                receiveData = new byte[1024];

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // 4) Receive LLAMADA BLOQUEANTE
                serverSocket.receive(receivePacket);
                // Datos recibidos
                String datoRecibido = new String(receivePacket.getData());
                System.out.println("DatoRecibido: " + datoRecibido );

                InetAddress IPAddress = receivePacket.getAddress();

                int port = receivePacket.getPort();

                JSONObject object = new JSONObject(datoRecibido);
                
                int temp = 0;
                switch (object.getInt("op")) {
                case 1:
                    data.add(object.getJSONObject("data"));
                    sendData = "{\"result\": \"added\"}".getBytes();
                    break;
                case 2:
                    boolean exist = false;
                    for (JSONObject datapoint : data) {
                        if(datapoint.getString("ciudad").compareTo(object.getJSONObject("data").getString("ciudad")) == 0) {
                            temp = datapoint.getInt("temperatura");
                            exist = true;
                        }
                    }
                    sendData = exist ? ("{\"result\": "+temp+"}").getBytes() : "{\"error\": \"ciudad no existe\"}".getBytes();
                    break;
                case 3:
                    int count = 0;
                    for (JSONObject datapoint : data) {
                        if(datapoint.getString("fecha").compareTo(object.getJSONObject("data").getString("fecha")) == 0) {
                            temp += datapoint.getInt("temperatura");
                            count++;
                        }
                    }
                    sendData = ("{\"result\": "+temp/count+"}").getBytes();
                    break;
                default:
                    sendData = "{\"error\": \"unknown op type\"}".getBytes();
                }

                // Enviamos la respuesta inmediatamente a ese mismo cliente
                // Es no bloqueante
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress,port);

                serverSocket.send(sendPacket);
            }
            //serverSocket.close();
        } catch (Exception ex) {
        	ex.printStackTrace();
            System.exit(1);
        }
    }
}