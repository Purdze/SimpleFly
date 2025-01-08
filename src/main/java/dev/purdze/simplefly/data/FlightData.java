package dev.purdze.simplefly.data;

public class FlightData {
    private boolean flying;
    private float speed;

    public FlightData(boolean flying, float speed) {
        this.flying = flying;
        this.speed = speed;
    }

    public boolean isFlying() {
        return flying;
    }

    public float getSpeed() {
        return speed;
    }
} 