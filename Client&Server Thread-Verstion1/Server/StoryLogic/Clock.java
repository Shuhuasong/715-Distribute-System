package Server.StoryLogic;

public class Clock extends Thread {
    private static int durationBeforeBoarding = 12000;
    private Plane plane;
    public NotificationObject lock;

    public Clock(Plane plane) {
        setName("Story clock");
        this.setPriority(MAX_PRIORITY);
        this.plane = plane;
        this.lock = new NotificationObject(getName());
    }

    @Override
    public void run() {
        try {
            sleep(durationBeforeBoarding);
            synchronized (plane) {
                while (!plane.isReadyToBoardPassengers) ;
                msg("It's time to fly. Notifying flight attendant");
                plane.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static long time = System.currentTimeMillis();

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
    }
}
