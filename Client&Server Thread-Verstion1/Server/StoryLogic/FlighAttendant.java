package Server.StoryLogic;

public class FlighAttendant {


    public static int countGroup1 = 0;

    public synchronized static void increaseCountGroup1() {
        countGroup1++;
    }

    public static int group1_boarded = 0;

    public synchronized static void increaseGroup1BoardedCount(NotificationObject lock) {
        group1_boarded++;
        if (group1_boarded == countGroup1) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    public static int countGroup2 = 0;

    public synchronized static void increaseCountGroup2() {
        countGroup2++;
    }

    public static int group2_boarded = 0;

    public synchronized static void increaseGroup2BoardedCount(NotificationObject lock) {
        group2_boarded++;
        if (group2_boarded == countGroup2) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    public static int countGroup3 = 0;

    public synchronized static void increaseCountGroup3() {
        countGroup3++;
    }

    public static int group3_boarded = 0;

    public synchronized static void increaseGroup3BoardedCount(NotificationObject lock) {
        group3_boarded++;
        if (group3_boarded == countGroup3) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }
}
