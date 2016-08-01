/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vopro7.vop.mobiliteitgent.eventsreceiver.rabbitmq;

import android.content.Context;
import android.util.Log;

import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.neovisionaries.ws.client.StatusLine;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import vopro7.vop.mobiliteitgent.certificates.CertificateInstaller;
import vopro7.vop.mobiliteitgent.eventsreceiver.Receiver;

/**
 * @author App Team
 */
public class WebSocketUse extends Receiver {

    private static final String PROTOCOL = "wss";
    private static final String BROKER_HOST = "vopro7.ugent.be";
    private static final int BROKER_PORT = 443;
    private static final String BROKER_PATH = "/rabbitmq/ws";

    private final String subId;
    private Context c;

    private volatile WebSocket ws;

    /**
     * Create a websocket use object
     *
     * @param userId  The Id of the user
     * @param context The context
     */
    public WebSocketUse(int userId, Context context) {
        super(userId);
        c = context;
        this.subId = "events-subscription-app-user-" + userId;
        WebSocketFactory factory = new WebSocketFactory();
        try {
            SSLSocketFactory socketFactory = CertificateInstaller.installCertificates(context, BROKER_HOST, BROKER_PORT);
            factory.setSSLSocketFactory(socketFactory);
            ws = factory.createSocket(PROTOCOL + "://" + BROKER_HOST + ":" + BROKER_PORT + BROKER_PATH, 5000);
            ws.addListener(new RabbitmqWebSocketListener());
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException | CertificateException ex) {
            Log.w(WebSocketUse.class.getName(), "Could not check if certificate is already trusted or could not create socket to connect with server! ", ex);
        }
    }

    @Override
    public void startReceiving() {
        if (ws != null) {
            try {
                ws.connect();
            } catch (OpeningHandshakeException e) {
                // A violation against the WebSocket protocol was detected during the opening handshake.
                Log.w(WebSocketUse.class.getName(), "Vialotion against WebSocket-protocol detected during opening handshake", e);

                // Status line.
                StatusLine sl = e.getStatusLine();

                // HTTP headers.
                Map<String, List<String>> headers = e.getHeaders();
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    // Header name.
                    String name = entry.getKey();

                    // Values of the header.
                    List<String> values = entry.getValue();

                    if (values == null || values.size() == 0) {
                        // Print the name only.
                        continue;
                    }
                }
            } catch (WebSocketException e) {
                Log.w(WebSocketUse.class.getName(), "Could not connect socket", e);
            }
        }
    }

    @Override
    public void stopReceiving() {
        if (ws != null && ws.isOpen()) {
            String begin = "User " + getUserId();
            ws.sendText("UNSUBSCRIBE\nid:" + subId + "\n\n\0");
            ws.sendText("DISCONNECT\nreceipt:" + ws.hashCode() + "\n\n\0");
        }
    }

    @Override
    public void waitTillClose() {
        while (ws.isOpen()) {
            //Keep open
        }
    }

    /**
     * Parse the body of a response
     *
     * @param msgFrame The frame you want to parse
     * @return The parsed frame
     */
    private String extractBody(String msgFrame) {
        //in a MESSAGE- or ERROR-frame (the only client frames with a body), the body
        //is separated from the header with a double line-feed and is the last part of the frame
        String[] parts = msgFrame.split("\n\n");
        return parts[parts.length - 1].replace("\0", "");
    }

    /**
     *
     */
    private class RabbitmqWebSocketListener extends WebSocketAdapter {

        private volatile boolean connected;

        /**
         * Create the listener for Rabbit MQ
         */
        public RabbitmqWebSocketListener() {
            connected = false;
        }

        @Override
        public synchronized void onConnected(WebSocket socket, Map<String, List<String>> headers) {

            connected = true;
            String vhost = "vopro";
            String username = "vopro_user_app";
            String password = "sassa7bien";
            String exchange = "events_vopro7_xchg";
            String rkey = "user" + getUserId();
            socket.sendText("CONNECT\n"
                    + "login:" + username + "\n"
                    + "passcode:" + password + "\n"
                    + "host:" + vhost + "\n"
                    + "client-id:Sample-client\n"
                    + "heart-beat:0,0\n\n\0"
            );

            socket.sendText("SUBSCRIBE\nid:" + subId + "\n"
                    + "destination:/exchange/" + exchange + "/" + rkey + "\n\n\0");
        }

        @Override
        public void onTextMessage(WebSocket websocket, String frame) {
            String begin = "User " + getUserId() + " received";
            if (frame.startsWith("MESSAGE")) {
                String body = extractBody(frame);
                processEvent(c, body);
            } else if (frame.startsWith("ERROR")) {
                Log.e(RabbitmqWebSocketListener.class.getName(), begin + " Error: " + frame);
            } else if (frame.startsWith("RECEIPT")) {
                Log.i(RabbitmqWebSocketListener.class.getName(), begin + " receipt: " + frame);
            } else {
                Log.i(RabbitmqWebSocketListener.class.getName(), begin + " frame: " + frame);
            }
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) {
            cause.printStackTrace(System.out);
        }

        @Override
        public synchronized void onCloseFrame(WebSocket websocket, WebSocketFrame frame) {
            String s = frame.getCloseReason();
            connected = false;
        }

        public synchronized boolean isConnected() {
            return connected;
        }

    }

}
