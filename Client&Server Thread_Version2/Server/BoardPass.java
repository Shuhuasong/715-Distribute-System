package Server;

import java.util.HashSet;
import java.util.Set;


import java.util.Random;

public class BoardPass {
    public static Set<Integer> chooosedTickets = new HashSet<>();
    int id;
    public static int totalPassengers = 30;

    static class Ticket {

        int seatNumber;
        int zoneNumber;


        Ticket() {
        }

        public  Ticket(int seatNumber, int zoneNumber) {
            this.seatNumber = seatNumber;
            this.zoneNumber = (seatNumber/10)+1;
//            if (seatNumber > 0 && seatNumber <= 10) {
//                zoneNumber = 1;
//            } else if (seatNumber > 10 && seatNumber <= 20) {
//                zoneNumber = 2;
//            } else if (seatNumber > 20 && seatNumber <= 30) {
//                zoneNumber = 3;
//            }
        }
        public synchronized Ticket getTicket(){
            Random rand = new Random();
            int newNumber = rand.nextInt(totalPassengers)+1; // get the number 1-30
            while(chooosedTickets.contains(newNumber)){
                newNumber = rand.nextInt(totalPassengers)+1;
            }
            chooosedTickets.add(newNumber);
//             System.out.println(newNumber);
            return new Ticket(newNumber, (newNumber/10)+1);
        }

    }


    public Ticket ticket;
    public  String threadName;

    BoardPass(){

    }
    BoardPass(String threadName){
        this.threadName = threadName;
    }
    BoardPass(int seatNumber, int zoneNumber, String threadName ){
        this.ticket = new Ticket(seatNumber, zoneNumber);
        this.threadName = threadName;
    }
}
