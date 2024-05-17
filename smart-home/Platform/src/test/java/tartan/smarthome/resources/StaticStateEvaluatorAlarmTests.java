package tartan.smarthome.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Hashtable;
import java.util.Map;
import tartan.smarthome.resources.iotcontroller.IoTValues;
import tartan.smarthome.resources.StaticTartanStateEvaluator;

class StaticStateEvaluatorAlarmTests extends genericStaticStateEvaluator{

    @BeforeEach
    void makeState() {
        super.makeState();

        // Alarm is not yet active
        this.init_state.put(IoTValues.ALARM_ACTIVE, false);

        this.init_state.put(IoTValues.ALARM_PASSCODE, "1234");

        // Correct passcode is not given
        this.init_state.put(IoTValues.GIVEN_PASSCODE, "incorrect");
    }

    @Test
    @DisplayName("Test Frame #1")
    void alarmSoundsTestFrame_1() {
       // Alarm is armed
       this.init_state.put(IoTValues.ALARM_STATE, true);

       // Door is opened: sound alarm
       this.init_state.put(IoTValues.DOOR_STATE, true);

       // House is occupied: sound alarm
       this.init_state.put(IoTValues.INTRUDER_PROXIMITY, true);

       Map<String, Object> state_new = this.evaluator.evaluateState(this.init_state, this.log);

       // Alarm should be active now
       assertEquals(true, state_new.get(IoTValues.ALARM_ACTIVE), "Alarm should have been sounded");
    }

    @Test
    @DisplayName("Test Frame #2")
    void alarmSoundsTestFrame_2() {

        // Alarm is armed
        this.init_state.put(IoTValues.ALARM_STATE, true);

        // Door is opened: sound alarm
        this.init_state.put(IoTValues.DOOR_STATE, true);

        // House is not occupied
        this.init_state.put(IoTValues.INTRUDER_PROXIMITY, false);

        Map<String, Object> state_new = this.evaluator.evaluateState(this.init_state, this.log);

        // Alarm should be active now
        assertEquals(true, state_new.get(IoTValues.ALARM_ACTIVE), "Alarm should have been sounded");
    }

    @Test
    @DisplayName("Test Frame #3")
    void alarmSoundsTestFrame_3() {

        // Alarm is armed
        this.init_state.put(IoTValues.ALARM_STATE, true);

        // Door is closed
        this.init_state.put(IoTValues.DOOR_STATE, false);

        // House is occupied: sound alarm
        this.init_state.put(IoTValues.INTRUDER_PROXIMITY, true);

        Map<String, Object> state_new = this.evaluator.evaluateState(this.init_state, this.log);

        // Alarm should be active now
        assertEquals(true, state_new.get(IoTValues.ALARM_ACTIVE), "Alarm should have been sounded");
    }

    @Test
    @DisplayName("Test Frame #4")
    void alarmSoundsTestFrame_4() {

        // Alarm is armed
        this.init_state.put(IoTValues.ALARM_STATE, true);

        // Door is closed
        this.init_state.put(IoTValues.DOOR_STATE, false);

        // House is not occupied
        this.init_state.put(IoTValues.INTRUDER_PROXIMITY, false);

        Map<String, Object> state_new = this.evaluator.evaluateState(this.init_state, this.log);

        // Alarm should be inactive
        assertEquals(false, state_new.get(IoTValues.ALARM_ACTIVE), "Alarm was activated when it shouldn't have been");
    }

    @Test
    @DisplayName("Test Frame #5")
    void alarmSoundsTestFrame_5() {

        // Alarm is not armed
        this.init_state.put(IoTValues.ALARM_STATE, false);

        // Door is opened
        this.init_state.put(IoTValues.DOOR_STATE, true);

        // House is occupied
        this.init_state.put(IoTValues.INTRUDER_PROXIMITY, true);

        Map<String, Object> state_new = this.evaluator.evaluateState(this.init_state, this.log);

        // Alarm should be inactive
        assertEquals(false, state_new.get(IoTValues.ALARM_ACTIVE), "Alarm was activated when it shouldn't have been");
    }

    @Test
    @DisplayName("Test Frame #6")
    void alarmSoundsTestFrame_6() {

        // Alarm is not armed
        this.init_state.put(IoTValues.ALARM_STATE, false);

        // Door is opened
        this.init_state.put(IoTValues.DOOR_STATE, true);

        // House is not occupied
        this.init_state.put(IoTValues.INTRUDER_PROXIMITY, false);

        Map<String, Object> state_new = this.evaluator.evaluateState(this.init_state, this.log);

        // Alarm should be inactive
        assertEquals(false, state_new.get(IoTValues.ALARM_ACTIVE), "Alarm was activated when it shouldn't have been");
    }

    @Test
    @DisplayName("Test Frame #7")
    void alarmSoundsTestFrame_7() {

        // Alarm is not armed
        this.init_state.put(IoTValues.ALARM_STATE, false);

        // Door is closed
        this.init_state.put(IoTValues.DOOR_STATE, false);

        // House is occupied
        this.init_state.put(IoTValues.INTRUDER_PROXIMITY, true);

        Map<String, Object> state_new = this.evaluator.evaluateState(this.init_state, this.log);

        // Alarm should be inactive
        assertEquals(false, state_new.get(IoTValues.ALARM_ACTIVE), "Alarm was activated when it shouldn't have been");
    }

    @Test
    @DisplayName("Test Frame #8")
    void alarmSoundsTestFrame_8() {

        // Alarm is not armed
        this.init_state.put(IoTValues.ALARM_STATE, false);

        // Door is closed
        this.init_state.put(IoTValues.DOOR_STATE, false);

        // House is not occupied
        this.init_state.put(IoTValues.INTRUDER_PROXIMITY, false);

        Map<String, Object> state_new = this.evaluator.evaluateState(this.init_state, this.log);

        // Alarm should be inactive
        assertEquals(false, state_new.get(IoTValues.ALARM_ACTIVE), "Alarm was activated when it shouldn't have been");
    }
}
