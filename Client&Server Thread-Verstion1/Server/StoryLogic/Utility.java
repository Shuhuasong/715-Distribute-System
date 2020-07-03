package Server.StoryLogic;

public class Utility {
    public volatile static boolean isGateOpen = true;
    public volatile static int numPassengersCheckin = 0;


    public synchronized static void increseNumCheckIn() {
        numPassengersCheckin++;
    }
}
