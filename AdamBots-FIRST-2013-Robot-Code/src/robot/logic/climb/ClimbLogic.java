/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.logic.climb;

import java.util.Vector;
import robot.RobotMain;
import robot.control.FancyJoystick;
import robot.logic.LogicPhase;
import robot.logic.LogicTask;
import robot.logic.tasks.TAwaitButton;
import robot.logic.tasks.TAwaitStatus;
import robot.logic.tasks.TMoveWinch;
import robot.logic.tasks.TStopWinch;

/**
 *
 * @author Ben
 */
public class ClimbLogic extends LogicPhase {
    //// CONSTANTS -------------------------------------------------------------
    
    public static final double WINCH_DISTANCE_1 = 0;
    public static final double WINCH_DISTANCE_2 = 0;
    public static final double WINCH_DISTANCE_3 = 0;
    
    //// TASK LIST -------------------------------------------------------------
    
    private Vector _tasks;
    private int _currentIndex = 0;
    private LogicTask _currentTask;
    
    //// CONSTRUCTOR -----------------------------------------------------------
    
    public ClimbLogic(){
	super();
    }

    //// INITIALIZATION --------------------------------------------------------
    
    public void initPhase() {
	println("ClimbLogic Initialized");
	// Populate Tasks Array
	_tasks = new Vector();
	_tasks.addElement(new TAwaitButton(0, FancyJoystick.BUTTON_START));
	//_tasks.addElement(new TMoveWinch(WINCH_DISTANCE_1));
	//_tasks.addElement(new TAwaitStatus(TAwaitStatus.WINCH_IN_POSITION));
	// TODO:  Manual Move Robot or Manual Adjust Winch
	//_tasks.addElement(new TStopWinch());
	
	// Begin First Task
	_currentIndex = 0;
	setCurrentTask((LogicTask)_tasks.elementAt(_currentIndex));
    }

    //// UPDATE ----------------------------------------------------------------
    
    public void updatePhase() {
	// Check for Emergency Stop (START and BACK on primary joystick)
	if(RobotMain.primaryJoystick.getRawButton(FancyJoystick.BUTTON_START)
	&& RobotMain.primaryJoystick.getRawButton(FancyJoystick.BUTTON_BACK)){
	    emergencyStop();
	}
	
	// Update Current Task
	if(_currentTask != null){
	    _currentTask.updateTask();
	
	    if(_currentTask.isDone()){
		println("Task Reported DONE, moving to next Task...");
		nextTask();
	    }
	}
    }

    //// FINISH ----------------------------------------------------------------
    
    /**
     * Stops the current Task and transitions to the TeleopLogic Phase.
     */
    public void finishPhase() {
	_currentTask.finishTask();
	_currentTask = null;
	RobotMain.getInstance().segueToLogicPhase(LogicPhase.TELEOP);
    }
    
    /**
     * Emergency stops Climbing.
     * @see #finishPhase()
     */
    public void emergencyStop(){
	println("Emergency Stopping ClimbLogic...");
	finishPhase();  // TODO:  Additional Logic Here?
    }
    
    //// TASK LOGIC ------------------------------------------------------------
    
    /**
     * Transitions to the next Task in the sequence.
     * @see #setCurrentTask(robot.logic.LogicTask) 
     */
    public void nextTask(){
	// Are there any tasks left in the sequence?
	if(_currentIndex < _tasks.size()-1){
	    setCurrentTask((LogicTask)_tasks.elementAt(++_currentIndex));
	} else {
	    println("Climbing Finished; No More Tasks Left!");
	}
    }
    
    /**
     * Transitions to the Task specified by first finishing the old Task and
     * then initializing the new Task.
     * @param newTask The Task to transition to.
     * @see LogicTask#finishTask() 
     * @see LogicTask#initializeTask() 
     */
    public void setCurrentTask(LogicTask newTask){
	// Finish Old Task
	if(_currentTask != null){
	    int status = _currentTask.finishTask();
	    if(status == LogicTask.SUCCESS){
		println("Task Finished Successfully.");
	    } else {
		println("Task Failed.");
	    }
	}
	
	// Begin New Task
	_currentTask = newTask;
	_currentTask.initializeTask();
	println("ClimbLogic :: Executing New Task (" + _currentTask.getClass());
    }
    
}
