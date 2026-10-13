
package com.sahib.uberApp.UberApplication;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class WebSocketStompTest {

    @Test
    void subscribeToDriverRideRequests() throws Exception {
        WebSocketStompClient client =
                new WebSocketStompClient(
                        new StandardWebSocketClient());

        StompSession session = client.connectAsync(
                "ws://localhost:8080/ws",
                new StompSessionHandlerAdapter() {}
        ).get(10, TimeUnit.SECONDS);

        System.out.println("STOMP connected: "
                + session.isConnected());

        Long driverId = 1L; // Replace with your actual driver ID

        session.subscribe(
                "/topic/driver/" + driverId + "/ride-requests",
                new StompSessionHandlerAdapter() {
                    @Override
                    public void handleFrame(
                            org.springframework.messaging.simp.stomp.StompHeaders headers,
                            Object payload) {
                        System.out.println(
                                "RIDE REQUEST RECEIVED: " + payload);
                    }
                });

        System.out.println(
                "Subscribed to driver " + driverId
                        + " ride requests.");

        // Keep the test alive so it can receive a message.
        Thread.sleep(30_000);

        session.disconnect();
        client.stop();
    }
}
