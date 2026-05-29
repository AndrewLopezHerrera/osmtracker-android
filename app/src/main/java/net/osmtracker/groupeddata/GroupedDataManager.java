package net.osmtracker.groupeddata;

public class GroupedDataManager {

    private boolean RecordingGroupedData;
    private long CurrentTrackID;
    private String CurrentUUID;

    private static final GroupedDataManager Instance = new GroupedDataManager();

    private GroupedDataManager(){
        RecordingGroupedData = false;
        CurrentTrackID = 0;
        CurrentUUID = "";
    }

    public static GroupedDataManager getInstance(){
        return Instance;
    }

    public void activateRecordGroupedData(long trackID, String uuid){
        CurrentTrackID = trackID;
        CurrentUUID = uuid;
        RecordingGroupedData = true;
    }

    public void deactivateRecordGroupedData(){
        CurrentTrackID = 0;
        CurrentUUID = "";
        RecordingGroupedData = false;
    }

    public boolean isRecordingGroupedData() {
        return RecordingGroupedData;
    }

    public long getCurrentTrackID() {
        return CurrentTrackID;
    }

    public String getCurrentUUID() {
        return CurrentUUID;
    }
}
