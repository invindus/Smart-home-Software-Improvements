package tartan.smarthome.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalTime;

import java.util.List;
import java.util.Objects;

/**
 * The mode for a Tartan Home to be serialized as JSON. This is managed by Jackson via Dropwizard.
 * See https://www.dropwizard.io/1.0.0/docs/getting-started.html#jackson-for-json
 */
public class TartanHome {

    // The name of the home
    @JsonProperty
    private String name;

    // The network address of the home
    @JsonProperty
    private String address;

    // The desired temperature
    @JsonProperty
    private String targetTemp;

    // the current temperature
    @JsonProperty
    private String temperature;

    // the current humidity
    @JsonProperty
    private String humidity;

    // the state of the door ("open"/"closed")
    @JsonProperty
    private String door;

    // the state of the door lock "locked" or "unlocked"
    @JsonProperty
    private String lock;

    // the state of the light (true if on, false if off)
    @JsonProperty
    private String light;

    // the humidifier state (true if on, false if off)
    @JsonProperty
    private String humidifier;

    // the state of the resident sensor (true of address occupied, false if vacant)
    @JsonProperty
    private String residentProximity;

    // state of the intruder sensor (true if detected)
    @JsonProperty
    private String intruderProximity;

    // the heater state (true if on, false if off)
    @JsonProperty
    private String hvacMode;

    // The state of the HVAC system
    @JsonProperty
    private String hvacState;

    // the alarm active state (true if alarm sounding, false if alarm not sounding)
    @JsonProperty
    private String alarmActive;

    // the alarm delay timeout
    @JsonProperty
    private String alarmDelay;

    // the alarm enabled state
    @JsonProperty
    private String alarmArmed;

    // Properties that are not part of the historical record
    @JsonProperty
    private List<String> eventLog;

    @JsonProperty
    private String authenticated;

    @JsonProperty
    private String alarmPasscode;

    // is the passcode enabled
    @JsonProperty
    private String lockPasscodeState;

    // the value of the passcode
    @JsonProperty
    private LocalTime nightModeStart;

    @JsonProperty
    private LocalTime nightModeEnd;

    @JsonProperty
    private String lockPasscode;

    @JsonProperty
    private String groupExperiment;

    @JsonProperty
    private Long minutesLightsOn;

    /**
     * Empty constructor needed by Jackson deserialization
     */
    public TartanHome() {  }


    /**
     * Get the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the address
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the target temperature
     * @return the target temperature
     */
    public String getTargetTemp() {
        return targetTemp;
    }

    /**
     * Set the target temperature
     * @param targetTemp the new target temperature
     */
    public void setTargetTemp(String targetTemp) { this.targetTemp = targetTemp; }

    /**
     * Get the current temperature
     * @return the temperature
     */
    public String getTemperature() {
        return this.temperature;
    }

    /**
     * Set the temperature
     * @param temperature the new temperature
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * Get the humidity
     * @return Current humidity
     */
    public String getHumidity() {
        return this.humidity;
    }

    /**
     * Set the humidity
     * @param humidity the new humidity
     */
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    /**
     * Get the door state
     * @return the door state
     */
    public String getDoor() {
        return this.door;
    }

    /**
     * Set the door state
     * @param door the new door state
     */
    public void setDoor(String door) {
        this.door = door;
    }

    /**
     * Get the lock state
     * @return the lock state
     */
    public String getLock() {
        return this.lock;
    }
    /**
     * Set the lock state
     * @param lock the new lock state
     */
    public void setLock(String lock) {
        this.lock = lock;
    }

    /**
     * Get the light state
     * @return the light state
     */
    public String getLight() {
        return this.light;
    }

    /**
     * Set the light state
     * @param light the new light state
     */
    public void setLight(String light) {
        this.light = light;
    }

    /**
     * Get the dehumidifier state
     * @return the dehumidifier state
     */
    public String getHumidifier() {
        return humidifier;
    }

    /**
     * Set the dehumidifier state
     * @param humidifier the new state
     */
    public void setHumidifier(String humidifier) {
        this.humidifier = humidifier;
    }

    /**
     * Get the motion sensor state
     * @return the motion sensor state
     */
    public String getResidentProximity() {
        return residentProximity;
    }

    /**
     * Set the motion sensor state
     * @param proximity the new state
     */
    public void setResidentProximity(String proximity) {
        this.residentProximity = proximity;
    }

    /**
     * Get the motion sensor state
     * @return the motion sensor state
     */
    public String getIntruderProximity() {
        return intruderProximity;
    }

    /**
     * Set the motion sensor state
     * @param proximity the new state
     */
    public void setIntruderProximity(String proximity) {
        this.intruderProximity = proximity;
    }

    /**
     * Get the alarm armed state
     * @return the status of the alarm
     */
    public String getAlarmArmed() {
        return alarmArmed;
    }

    /**
     * Arm/Disarm the alarm
     * @param alarmArmed the new state
     */
    public void setAlarmArmed(String alarmArmed) {
        this.alarmArmed = alarmArmed;
    }

    /**
     * Get the HVAC mode
     * @return the HVAC mode
     */
    public String getHvacMode() {
        return hvacMode;
    }

    /**
     * Set the HVAC mode
     * @param hvacMode the new mode
     */
    public void setHvacMode(String hvacMode) {
        this.hvacMode = hvacMode;
    }

    /**
     * Get the alarm active state
     * @return the current state
     */
    public String getAlarmActive() {
        return alarmActive;
    }

    /**
     * Set the alarm active state
     * @param alarmActive the new state
     */
    public void setAlarmActive(String alarmActive) {
        this.alarmActive = alarmActive;
    }

    /**
     * Get the alarm delay
     * @return the current delay
     */
    public String getAlarmDelay() {
        return alarmDelay;
    }

    /**
     * Set the alarm delay
     * @param alarmDelay the new delay
     */
    public void setAlarmDelay(String alarmDelay) {
        this.alarmDelay = alarmDelay;
    }

    /**
     * Get the HVAC state
     * @return the current state
     */
    public String getHvacState() {
        return hvacState;
    }

    /**
     * Set the HVAC state
     * @param hvacState the new state
     */
    public void setHvacState(String hvacState) {
        this.hvacState = hvacState;
    }

    /**
     * Get the house event log
     * @return the log
     */
    public List<String> getEventLog() { return eventLog;  }

    /**
     * Set the house event log
     * @param eventLog the log
     */
    public void setEventLog(List<String> eventLog) {
        this.eventLog = eventLog;
    }

    /**
     * Get the authenticated state
     * @return the state
     */
    public String getAuthenticated() { return authenticated; }

    /**
     * Set the authenticated state
     * @param authenticated the new state
     */
    public void setAuthenticated(String authenticated) { this.authenticated = authenticated;  }

    /**
     * Get the alarm passcode
     * @return the passcode
     */
    public String getAlarmPasscode() { return alarmPasscode; }

    /**
     * Set the alarm passcode
     * @param alarmPasscode the new passcode
     */
    public void setAlarmPasscode(String alarmPasscode) { this.alarmPasscode = alarmPasscode; }

    /**
     * Get the lock passcode
     * @return the passcode
     */
    public String getLockPasscode() { return lockPasscode; }

    /**
     * Set the lock passcode
     * @param lockPasscode the new passcode
     */
    public void setLockPasscode(String lockPasscode) {
        // Thread.dumpStack();
        this.lockPasscode = lockPasscode; }

    public String getLockPasscodeState() { return lockPasscodeState; }

    public void setLockPasscodeState(String lockPasscodeState) {
        this.lockPasscodeState = lockPasscodeState;
    }

    /**
     * Get the night mode start time
     * @return the start time
     */
    public LocalTime getNightModeStart() { return nightModeStart; }

    /**
     * Set the night mode start time
     * @param nightModeStart the new start time
     */
    public void setNightModeStart(String nightModeStart) {
        // TODO: might not be the right parse; see if we can pass in as a LocalTime
        this.nightModeStart = LocalTime.parse(nightModeStart);
    }

    /**
     * Get the night mode end time
     * @return the end time
     */
    public LocalTime getNightModeEnd() { return nightModeEnd; }

    /**
     * Set the night mode end time
     * @param nightModeEnd the new end time
     */
    public void setNightModeEnd(String nightModeEnd) {
        this.nightModeEnd = LocalTime.parse(nightModeEnd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TartanHome)) {
            return false;
        }
        final TartanHome that = (TartanHome) o;
        return Objects.equals(this.name, that.name);
    }

    public String getGroupExperiment() {
        return groupExperiment;
    }

    public void setGroupExperiment(String groupExperiment) {
        this.groupExperiment = groupExperiment;
    }

    public Long getMinutesLightsOn() {
        return minutesLightsOn;
    }

    public void setMinutesLightsOn(Long minutesLightsOn) {
        this.minutesLightsOn = minutesLightsOn;
    }
}
