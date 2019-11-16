package net.tierion.meraiot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BeaconDTO {
    private final int number;
    private final int rssi;
    private final int txPowerLevel;
    private final int x;
    private final int y;

    public BeaconDTO(@JsonProperty("number") int number,
                     @JsonProperty("rssi") int rssi,
                     @JsonProperty("txPowerLevel") int txPowerLevel,
                     @JsonProperty("x") int x,
                     @JsonProperty("y") int y) {
        this.number = number;
        this.rssi = rssi;
        this.txPowerLevel = txPowerLevel;
        this.x = x;
        this.y = y;
    }
}
