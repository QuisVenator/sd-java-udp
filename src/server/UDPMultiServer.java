package server;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class UDPMultiServer {
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

                System.out.println("Esperando a algun cliente... ");

                // 4) Receive LLAMADA BLOQUEANTE
                serverSocket.receive(receivePacket);
				
				System.out.println("________________________________________________");
                System.out.println("Aceptamos un paquete");

                // Datos recibidos e Identificamos quien nos envio
                String datoRecibido = new String(receivePacket.getData());
                System.out.println("DatoRecibido: " + datoRecibido );

                InetAddress IPAddress = receivePacket.getAddress();

                int port = receivePacket.getPort();
                
                //TODO insertar dato
                
                // Respondemos agregando a la persona una asignatura
                p.getAsignaturas().add("Algoritmos y Estructuras de datos 2");
                p.getAsignaturas().add("Redes de Computadoras 2");

                // Enviamos la respuesta inmediatamente a ese mismo cliente
                // Es no bloqueante
                sendData = PersonaJSON.objetoString(p).getBytes();
                DatagramPacket sendPacket =
                        new DatagramPacket(sendData, sendData.length, IPAddress,port);

                serverSocket.send(sendPacket);

            }

        } catch (Exception ex) {
        	ex.printStackTrace();
            System.exit(1);
        }

    }
}