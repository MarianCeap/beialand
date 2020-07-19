package com.beia.solomon.networkPackets;

/**
 *
 * @author beia
 */
public class KontaktBeacon extends Beacon
{
    public final String MANUFACTURER = "Kontakt";
    private String major;
    private String minor;

    public KontaktBeacon(String id, String label, int mallId, String companyId, String major, String minor, Coordinates coordinates, int layer, int floor)
    {
        this.id = id;
        this.label = label;
        this.mallId = mallId;
        this.companyId = companyId;
        this.major = major;
        this.minor = minor;
        this.coordinates = coordinates;
        this.layer = layer;
        this.floor = floor;
    }
    @Override
    public String getId()
    {
        return this.id;
    }

    @Override
    public String getLabel()
    {
        return this.label;
    }

    @Override
    public int getMallId()
    {
        return this.mallId;
    }

    @Override
    public String getCompanyId() { return this.companyId; }

    @Override
    public Coordinates getCoordinates()
    {
        return this.coordinates;
    }

    @Override
    public int getLayer()
    {
        return this.layer;
    }

    @Override
    public int getFloor()
    {
        return this.floor;
    }

    @Override
    public byte[] getImage() { return this.image; }

    @Override
    public void setImage(byte[] image) { this.image = image;}

    public String getMajor()
    {
        return this.major;
    }

    public String getMinor()
    {
        return this.minor;
    }
}