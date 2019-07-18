package com.beia.solomon.networkPackets;

import java.io.Serializable;

public class LocationData implements Serializable
{
    private int userId;
    private int storeId;
    private String zoneName;
    private boolean zoneEntered;
    private String time;
    public LocationData(int userId, int storeId, String zoneName, boolean zoneEntered, String time)
    {
        this.userId = userId;
        this.storeId = storeId;
        this.zoneName = zoneName;
        this.zoneEntered = zoneEntered;
        this.time = time;
    }
    public int getUserId()
    {
        return this.userId;
    }
    public int getStoreId()
    {
        return this.storeId;
    }
    public String getZoneName()
    {
        return this.zoneName;
    }
    public boolean getZoneEntered()
    {
        return this.zoneEntered;
    }
    public String getTime()
    {
        return this.time;
    }
}
