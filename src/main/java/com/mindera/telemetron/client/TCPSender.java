package com.mindera.telemetron.client;

/**
 * Created by hugocosta on 22/11/15.
 */
public class TCPSender extends AbstractSender implements Sender {

    private String host;
    private int port;

    public TCPSender(String host, String port) {
        //TODO validate params
        this.host = host;
        this.port = Integer.parseInt(port);
    }

    @Override
    public void send(byte[] message) {
        //TODO add implementation
    }

}
