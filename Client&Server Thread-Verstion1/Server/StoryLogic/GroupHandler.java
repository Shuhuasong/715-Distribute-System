package Server.StoryLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  + This implements singleton design pattern. This object gives permission for each passenger to board.
  + Server.StoryLogic.GroupHandler keeps track the number passengers who have formed the group and then notify all of them to enter the plane.
  + Server.StoryLogic.GroupHandler ensures passengers board group by group. Group1 board -> All passengers group 1 done -> Group2 board ....

This shows how all groups look like
[p1,p2...] is a group of passengers with a fixed size

                           [p1,p2...]       [p1, p2..]          [p1, p2..]
                           [p1, p2,.]       [p1, p2..]          [p1, p2..]
                           [p1, p2..]       [p1, p2..]          [p1, p2..]
Zones(keys in HashMap):     (zone1)          (zone2)             (zone3)
 * */
public class GroupHandler {
    private int groupSize;
    static GroupHandler instance;
    private Map<Integer, List<List<NotificationObject>>> allGroupsOfThreeZones;

    private GroupHandler(int groupSize) {
        this.groupSize = groupSize;
        allGroupsOfThreeZones = new HashMap<>();
        allGroupsOfThreeZones.computeIfAbsent(1, (k) -> new ArrayList<>()).add(new ArrayList<>()); //passengers zone 1
        allGroupsOfThreeZones.computeIfAbsent(2, (k) -> new ArrayList<>()).add(new ArrayList<>()); //passengers zone 2
        allGroupsOfThreeZones.computeIfAbsent(3, (k) -> new ArrayList<>()).add(new ArrayList<>()); // passengers zone 3
    }

    public static GroupHandler getInstance(int groupSize) {
        if (instance == null) {
            synchronized (GroupHandler.class) {
                if (instance == null) {
                    instance = new GroupHandler(groupSize);
                }
            }
        }
        return instance;
    }

    public synchronized void groupNewPassenger(NotificationObject p) {
        int zone = p.ticket.zoneNumber;
        List<List<NotificationObject>> allGroupsOfZone = allGroupsOfThreeZones.get(zone);
        List<NotificationObject> list = allGroupsOfZone.get(allGroupsOfZone.size() - 1);
        list.add(p);
        if (list.size() == groupSize) {//if the current group is full. Server.StoryLogic.GroupHandler gives all passengers in that group permission to board
            for (NotificationObject n : list) {
                synchronized (n) {
                    n.permissionToBoard = true;
                    n.notify(); //notify passenger to board, but Server.StoryLogic.GroupHandler does not remove the notification object.
                }
            }
            //Server.StoryLogic.GroupHandler will not remove the notification objects of passengers as Server.StoryLogic.GroupHandler needs
            //to keep track number of passengers who have grouped.
            allGroupsOfZone.add(new ArrayList<>()); //create a new group for that zone because the current group is full
        } else {//if the current group is not full
            //The remaining passengers of a zone is = Total passengers in that zone - number of passengers who have boarded so far
            int remainingPassengers = getTotal(zone) - numPassengersGroupedSoFar(zone);
            if (remainingPassengers < groupSize){ // the number of remaining passengers is not large enough to group
                //Let all the remaining passengers in that zone board
                for (NotificationObject n : list) {
                    synchronized (n) {
                        n.permissionToBoard = true;
                        n.notify();
                    }
                }
            }
        }
    }

    private int getTotal(int zone) {
        return zone == 1 ? FlighAttendant.countGroup1 : zone == 2 ? FlighAttendant.countGroup2 : FlighAttendant.countGroup3;
    }

    private synchronized int numPassengersGroupedSoFar(int zone) {
        int totalSize = 0;
        for (List<NotificationObject> group : allGroupsOfThreeZones.get(zone)) {
            totalSize += group.size();
        }
        return totalSize;
    }

}
