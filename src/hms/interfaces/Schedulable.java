package hms.interfaces;

import java.time.LocalDateTime;
import java.util.List;

public interface Schedulable {
    boolean isAvailable(LocalDateTime dateTime);
    List<LocalDateTime> getSchedule();
    void addSchedule(LocalDateTime dateTime);
    void removeSchedule(LocalDateTime dateTime);
}