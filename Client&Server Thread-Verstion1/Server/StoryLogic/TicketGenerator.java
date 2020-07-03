package Server.StoryLogic;

import Client.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketGenerator {
    private List<Ticket> tickets;

    public class Ticket {
        int seatNumber;
        int zoneNumber;

        public Ticket(int seatNumber) {
            this.seatNumber = seatNumber;
            this.zoneNumber = (seatNumber <= 10) ? 1 : (seatNumber <= 20) ? 2 : 3;
        }

        @Override
        public String toString() {
            return "[Seat Number: " + seatNumber + ",zone number: " + zoneNumber + "]";
        }

    }

    public TicketGenerator() {
        tickets = new ArrayList<>(Main.flightCapacity);
        for (int seatNumber = 1; seatNumber <= Main.flightCapacity; seatNumber++) {
            tickets.add(new Ticket(seatNumber));
        }
        Collections.shuffle(tickets); //shuffle all the tickets
    }

    public synchronized Ticket getRandomTicket() {
        //tickets are shuffled. It doesn't matter which position to remove. This method
        // always returns a random seat. However, removing the last element in the arraylist
        // is a O(1) operation compared to removing any element in the array
        return tickets.remove(tickets.size() - 1);
    }
}
