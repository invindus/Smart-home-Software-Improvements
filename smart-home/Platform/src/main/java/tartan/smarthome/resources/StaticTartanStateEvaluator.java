package tartan.smarthome.resources;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import tartan.smarthome.resources.iotcontroller.IoTValues;

public class StaticTartanStateEvaluator implements TartanStateEvaluator {

    private String formatLogEntry(String entry) {
        Long timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        return "[" + sdf.format(new Date(timeStamp)) + "]: " + entry + "\n";
    }

    /**
     * Ensure the requested state is permitted. This method checks each state
     * variable to ensure that the house remains in a consistent state.
     *
     * @param state The new state to evaluate
     * @param log The log of state evaluations
     * @return The evaluated state
     */
    @Override
    public Map<String, Object> evaluateState(Map<String, Object> inState, StringBuffer log) {

        // These are the state variables that reflect the current configuration of the house

        Integer tempReading = null; // the current temperature
        Integer targetTempSetting = null; // the user-desired temperature setting
        Integer humidityReading = null; // the current humidity
        Boolean doorState = null; // the state of the door (true if open, false if closed)
        Boolean lightState = null; // the state of the light (true if on, false if off)
        Boolean alarmState = null; // the alarm state (true if enabled, false if disabled)
        Boolean humidifierState = null; // the humidifier state (true if on, false if off)
        Boolean heaterOnState = null; // the heater state (true if on, false if off)
        Boolean chillerOnState = null; // the chiller state (true if on, false if off)
        Boolean alarmActiveState = null; // the alarm active state (true if alarm sounding, false if alarm not sounding)
        Boolean awayTimerState = false;  // assume that the away timer did not trigger this evaluation
        Boolean awayTimerAlreadySet = false;
        Boolean nightMode = false; // the night mode state (true if night mode is on, false if night mode is off)
        String alarmPassCode = null;
        String hvacSetting = null; // the HVAC mode setting, either Heater or Chiller
        String givenPassCode = "";
        LocalTime nightModeStart = null;
        LocalTime nightModeEnd = null;

        Boolean residentProximity = null; // the state of the resident sensor (true of house occupied, false if vacant)
        Boolean intruderProximity = null; // the state of the intruder sensor (true if intruder detected)

        Boolean lockState = null;
        Boolean requestedLockState = null;

        Boolean lockPasscodeState = null;
        String givenLockPasscode = "";
        String lockPasscode = "";

        System.out.println("Evaluating new state statically");

        Set<String> keys = inState.keySet();
        for (String key : keys) {

            if (key.equals(IoTValues.LOCK_STATE))
                lockState = (Boolean) inState.get(key);
            else if (key.equals(IoTValues.REQUESTED_LOCK_STATE))
                requestedLockState = (Boolean) inState.get(key);
            else if (key.equals(IoTValues.LOCK_PASSCODE_STATE)) {
                lockPasscodeState = (Boolean) inState.get(key);
            }
            else if (key.equals(IoTValues.LOCK_PASSCODE))
                lockPasscode = (String) inState.get(key);
            else if (key.equals(IoTValues.GIVEN_LOCK_PASSCODE))
                givenLockPasscode = (String) inState.get(key);

            if (key.equals(IoTValues.TEMP_READING)) {
                tempReading = (Integer) inState.get(key);
            } else if (key.equals(IoTValues.HUMIDITY_READING)) {
                humidityReading = (Integer) inState.get(key);
            } else if (key.equals(IoTValues.TARGET_TEMP)) {
                targetTempSetting = (Integer) inState.get(key);
            } else if (key.equals(IoTValues.HUMIDIFIER_STATE)) {
                humidifierState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.DOOR_STATE)) {
                doorState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.LIGHT_STATE)) {
                lightState = (Boolean) inState.get(key);

            } else if (key.equals(IoTValues.INTRUDER_PROXIMITY)) {
                intruderProximity = (Boolean) inState.get(key);

            } else if (key.equals(IoTValues.RESIDENT_PROXIMITY)) {
                residentProximity = (Boolean) inState.get(key);

            } else if (key.equals(IoTValues.ALARM_STATE)) {
                alarmState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.HEATER_STATE)) {
                heaterOnState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.CHILLER_STATE)) {
                chillerOnState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.HVAC_MODE)) {
                hvacSetting = (String) inState.get(key);
            } else if (key.equals(IoTValues.ALARM_PASSCODE)) {
                alarmPassCode = (String) inState.get(key);
            } else if (key.equals(IoTValues.GIVEN_PASSCODE)) {
                givenPassCode = (String) inState.get(key);
            } else if (key.equals(IoTValues.AWAY_TIMER)) {
                // This is a hack!
                awayTimerState = (Boolean) inState.getOrDefault(key, false);
            } else if (key.equals(IoTValues.ALARM_ACTIVE)) {
                alarmActiveState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.NIGHT_MODE)) {
                nightMode = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.NIGHT_MODE_START)) {
                nightModeStart = (LocalTime) inState.get(key);
            } else if (key.equals(IoTValues.NIGHT_MODE_END)) {
                nightModeEnd = (LocalTime) inState.get(key);
            } else if (key.equals(IoTValues.LOCK_STATE)) {
                lockState = (Boolean) inState.get(key);
            }
        }

        if (!residentProximity && !awayTimerAlreadySet) {
            awayTimerState = true;
            awayTimerAlreadySet = true;
        }

        if (lightState == true) {
            // The light was activated
            if (!residentProximity) {
                log.append(formatLogEntry("Cannot turn on light because user not home"));
                lightState = false;
            }
            else {
                log.append(formatLogEntry("Light on"));
            }
        } else if (lightState) {
            log.append(formatLogEntry("Light off"));
        }

        // -------------- ALARM EVALUATION --------------  //
        if(alarmState) {
            log.append(formatLogEntry("Alarm enabled"));
            if(intruderProximity || doorState) {
                alarmActiveState = true;
                log.append(formatLogEntry("Break in detected: Activating alarm"));  // R4: Alarm enabled, house suddenly occupied, sound alarm.
            }
        } else {
            alarmActiveState = false;
            log.append(formatLogEntry("Alarm disabled"));
            if(doorState && !residentProximity) {
                doorState = false;
                log.append(formatLogEntry("Closed door because house vacant"));     // R3: Vacant, close door.
            }

            if(!residentProximity) {
                alarmState = true;
                log.append(formatLogEntry("Cannot disable the alarm, house is empty"));
            }
        }

        if (alarmActiveState) {
            if(givenPassCode.compareTo(alarmPassCode) == 0) {
                log.append(formatLogEntry("Correct passcode entered, disabled alarm"));
                alarmActiveState = false;
                alarmState = false;
            } else {
                log.append(formatLogEntry("Cannot disable alarm, invalid passcode given"));
                alarmState = true;
            }
        }

        // -------------- LOCK EVALUATION ---------------- //
        // Priority of conditions if enforced by order of evaluations

        // Door automatically unlocks when resident is nearby
        if (residentProximity && lockState)
            lockState = false;

        // User can request to lock & unlock door, & may require passcode

        if (requestedLockState != null) {           // refactor: requestedLockState is optional & may be null
            if (requestedLockState != lockState) {
                // Check if passcode required
                if(lockPasscodeState) {
                    if (givenLockPasscode.compareTo(lockPasscode) == 0) {
                        log.append(formatLogEntry("Correct lock passcode given. Changing state of lock"));
                        lockState = requestedLockState;
                    } else {
                        log.append(formatLogEntry("Incorrect lock passcode given. Not changing state."));
                    }
                } else {
                    log.append(formatLogEntry("No lock passcode required. Changing lock state to requested state"));
                    lockState = requestedLockState;
                }
            }
        }

        // If an intruder is detected the door automatically locks
        if (intruderProximity)
            lockState = true;

        // the lock can not be locked while the door is open. close door?
        if((lockState == true) && (doorState == true)) {
            log.append(formatLogEntry("Door can not be open while deadbolt is locked. Closing door"));
            doorState = false;
        }

        if(doorState)
            log.append("Door open");
        else
            log.append("Door closed");

        // the user has arrived
        if (residentProximity) {
            log.append(formatLogEntry("House is occupied"));
            // if the alarm has been disabled, then turn on the light for the user

            if (!lightState && !alarmState) {
                lightState = true;
                log.append(formatLogEntry("Turning on light"));
            }

        }

        // Auto lock the house
        if (awayTimerState == true) {
            lightState = false;
            doorState = false;
            alarmState = true;
        }

        // Is the heater needed?
        if (tempReading < targetTempSetting) {
            log.append(formatLogEntry("Turning on heater, target temperature = " + targetTempSetting
                    + "F, current temperature = " + tempReading + "F"));
            heaterOnState = true;

            // Heater already on
        } else {
            // Heater not needed
            heaterOnState = false;
        }

        if (tempReading > targetTempSetting) {
            // Is the heater needed?
            if (chillerOnState != null) {
                if (!chillerOnState) {
                    log.append(formatLogEntry("Turning on air conditioner target temperature = " + targetTempSetting
                            + "F, current temperature = " + tempReading + "F"));
                    chillerOnState = true;
                } // AC already on
            }
        }
        // AC not needed
        else {
            chillerOnState = false;
        }


        if (chillerOnState) {
            hvacSetting = "Chiller";
        } else if (heaterOnState) {
            hvacSetting = "Heater";
        }
        // manage the HVAC control

        if (hvacSetting.equals("Heater")) {

            if (chillerOnState == true) {
                log.append(formatLogEntry("Turning off air conditioner"));
            }

            chillerOnState = false; // can't run AC
            humidifierState = false; // can't run dehumidifier with heater
        }

        if (hvacSetting.equals("Chiller")) {

            if (heaterOnState == true) {
                log.append(formatLogEntry("Turning off heater"));
            }

            heaterOnState = false; // can't run heater when the A/C is on
        }

        if (humidifierState && hvacSetting.equals("Chiller")) {
            log.append(formatLogEntry("Enabled Dehumidifier"));
        } else {
            log.append(formatLogEntry("Automatically disabled dehumidifier when running heater"));
            humidifierState = false;
        }

        if (nightModeStart != null && nightModeEnd != null) {
            LocalTime currentTime = LocalTime.now();
            if (currentTime.isAfter(nightModeStart) && currentTime.isBefore(nightModeEnd)) {
                nightMode = true;
                // if nightModeEnd is after midnight & currentTime is before midnight, then night mode is on
            } else nightMode = nightModeStart.isAfter(nightModeEnd) && (currentTime.isAfter(nightModeStart) || currentTime.isBefore(nightModeEnd));
        }

        if (nightMode && !lockState) {
            log.append(formatLogEntry("Locking doors because night mode is on"));
            lockState = true;
        }

        Map<String, Object> newState = new Hashtable<>();
        newState.put(IoTValues.DOOR_STATE, doorState);
        newState.put(IoTValues.AWAY_TIMER, awayTimerState);
        newState.put(IoTValues.LIGHT_STATE, lightState);
        newState.put(IoTValues.ALARM_STATE, alarmState);
        newState.put(IoTValues.HUMIDIFIER_STATE, humidifierState);
        newState.put(IoTValues.HEATER_STATE, heaterOnState);
        newState.put(IoTValues.CHILLER_STATE, chillerOnState);
        newState.put(IoTValues.ALARM_ACTIVE, alarmActiveState);
        newState.put(IoTValues.HVAC_MODE, hvacSetting);
        newState.put(IoTValues.ALARM_PASSCODE, alarmPassCode);
        newState.put(IoTValues.GIVEN_PASSCODE, givenPassCode);

        newState.put(IoTValues.RESIDENT_PROXIMITY, residentProximity);
        newState.put(IoTValues.INTRUDER_PROXIMITY, intruderProximity);

        newState.put(IoTValues.LOCK_STATE, lockState);

        newState.put(IoTValues.LOCK_PASSCODE_STATE, lockPasscodeState);
        newState.put(IoTValues.GIVEN_LOCK_PASSCODE, givenLockPasscode);
        newState.put(IoTValues.LOCK_PASSCODE, lockPasscode);

        newState.put(IoTValues.NIGHT_MODE, nightMode);

        return newState;
    }
}