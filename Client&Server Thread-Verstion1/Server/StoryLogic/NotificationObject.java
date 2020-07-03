package Server.StoryLogic;

public class NotificationObject {
    public String threadName;
    public int zoneNumber;
    public TicketGenerator.Ticket ticket;
    public boolean permissionToBoard = false;

    public NotificationObject(String threadName) {
        this.threadName = threadName;
    }

    public NotificationObject(String threadName, int ticketNumber) {
        this.threadName = threadName;
        this.zoneNumber = ticketNumber;
    }
}
