package Server;

public class Clock extends Thread{

    public FlightAttendant flightAttendant;
    public BoardPass lock;

    public Clock(FlightAttendant flightAttendant){
        setName("Clock Start");
        this.setPriority(MAX_PRIORITY);
        this.flightAttendant = flightAttendant;
        this.lock = new BoardPass(getName());
    }

    public static long time = System.currentTimeMillis();

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
    }

    @Override
    public void run(){
        try {
            sleep(35000);

            msg("The airplane is starting fly......");
            //notify the flight attendandt to start the process of checking Board Pass

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
