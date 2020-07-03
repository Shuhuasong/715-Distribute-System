//package Server.StoryLogic;/*
//* Documentation. Please take a moment to read it.
//*
//* Although I commented as much as I could, It's worth reading the notes here. Hopefully it will help explain the implementation a bit better
//*
//*The story revolves around 5 main objects:
//*   1 Server.StoryLogic.Passenger, Server.StoryLogic.Counter and FlightAttendant are self-explanatory
//*   2.Server.StoryLogic.Plane:
//*       One of the tricky parts is to ensure all passengers leaving the airplane in ascending order of their seat number. I made use of minHeap.
//        I passed in custom comparator in the constructor of the plane class. Passengers will be notified to leave 1 by 1.
//*   3.Server.StoryLogic.GroupHandler:
//*       The tricky part in this project is to ensure passengers entering the plane group by group and zone by zone. Please read documentation in
//*       Server.StoryLogic.GroupHandler class if you have any difficulty understanding my code.
//*    4.Ticket generator will give a random ticket by calling getRandomTicket()
//*
//* Finally, I made the fly time a little bit long which looks like the program has deadlock. Just wait a little bit, the plane will land
//* and all passengers will leave one by one.
//* */
//
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Queue;
//
//public class Main {
//    static int total_passengers = 30;
//    static int maxLineSize = 3;
//    static int groupNum = 4;
//    final static int flightCapacity = 30;
//
//    public static void main(String[] args) {
//        if (args.length == 3) {
//            total_passengers = Integer.valueOf(args[0]);
//            groupNum = Integer.valueOf(args[1]);
//            maxLineSize = Integer.valueOf(args[2]);
//        }
//        //initializations
//        List<NotificationObject> passengersAtAirport = new ArrayList<>();
//        Queue<NotificationObject> waitingLine1 = new ArrayDeque<>(maxLineSize);
//        Queue<NotificationObject> waitingLine2 = new ArrayDeque<>(maxLineSize);
//        TicketGenerator ticketGenerator = new TicketGenerator();
//        NotificationObject[] threeZones = {
//                new NotificationObject("zone1", 1),
//                new NotificationObject("zone2", 2),
//                new NotificationObject("zone3", 3)
//        };
//        Plane plane = new Plane(threeZones);
//        Clock clock = new Clock(plane);
//        //story begins
//        System.out.println("The story begins...\n\n");
//
//        plane.start();
//        clock.start();
//
//        total_passengers = Math.min(flightCapacity, total_passengers); //guarantee the total of passengers cannot exceed the capacity
//        for (int i = 0; i < total_passengers; i++) {
//            new Passenger(i, passengersAtAirport, waitingLine1, waitingLine2, threeZones, plane, groupNum).start();
//        }
//        Counter counter1 = new Counter(1, passengersAtAirport, waitingLine1, ticketGenerator);
//        Counter counter2 = new Counter(2, passengersAtAirport, waitingLine2, ticketGenerator);
//        counter2.start();
//        counter1.start();
//    }
//}
