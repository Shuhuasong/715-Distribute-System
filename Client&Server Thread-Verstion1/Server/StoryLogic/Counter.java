package Server.StoryLogic;

import Client.Main;
import Server.ServerHelper;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

public class Counter extends Thread {

    private final List<NotificationObject> passengersAtAirport;
    private final Queue<NotificationObject> waitingLine;
    private int id;
    private final TicketGenerator ticketGenerator;
    public ServerHelper serverHelper;

    public Counter(int id, List<NotificationObject> passengersAtAirport, Queue<NotificationObject> waitingLine1, TicketGenerator ticketGenerator) {
        this.id = id;
        setName("Server.StoryLogic.Counter " + id);
        this.passengersAtAirport = passengersAtAirport;
        this.waitingLine = waitingLine1;
        this.ticketGenerator = ticketGenerator;
//        this.serverHelper = serverHelper;
    }


    @Override
    public void run() {
        while (Utility.numPassengersCheckin != Main.total_passengers) {
            if (this.waitingLine.size() > 0) {
                try {
                    takeCareOfLine(waitingLine, id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                notifyNextPassengerToEnterWaitingLine();
            }
        }
        msg("exit");
        try {
            if (serverHelper != null)
                serverHelper.closeAllConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyNextPassengerToEnterWaitingLine() {
        synchronized (passengersAtAirport) {
            if (passengersAtAirport.size() > 0) {
                synchronized (passengersAtAirport.get(0)) {
                    passengersAtAirport.remove(0).notify();
                    msg("Notifying the next passenger waiting at the airport to enter the waiting line to check in");
                }
            }
        }
    }

    private void takeCareOfLine(Queue<NotificationObject> waitingLine, int lineNumber) throws InterruptedException {
        msg("serving line number : " + lineNumber);
        NotificationObject waitingCustomer = waitingLine.poll();
        msg("Retrieving boarding pass for customer: " + waitingCustomer.threadName);
        Thread.sleep(1000);
        waitingCustomer.ticket = ticketGenerator.getRandomTicket();
        synchronized (waitingCustomer) {
            if (Utility.isGateOpen)
                msg("Giving boarding pass to passenger " + waitingCustomer.threadName + ".Ticket info: " + waitingCustomer.ticket);
            else
                msg("Sorry you have missed the flight...");
            waitingCustomer.notify();
        }
        Utility.increseNumCheckIn();
    }


    public static long time = System.currentTimeMillis();

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
        if (serverHelper == null) return;
        try {
            serverHelper.sendMessageToClient(m);
            serverHelper.readMessageFromClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
