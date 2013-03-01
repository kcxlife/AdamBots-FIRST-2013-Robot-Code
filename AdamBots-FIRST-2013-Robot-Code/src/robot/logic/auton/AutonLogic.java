/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.logic.auton;

import java.util.Vector;
import robot.RobotMain;
import robot.control.FancyJoystick;
import robot.logic.LogicPhase;
import robot.logic.LogicTask;

/**
 * Performs logic during the autonomous period of gameplay.
 * @author Ben
 */
public class AutonLogic extends LogicPhase {
    //// TASK LIST -------------------------------------------------------------
    
    private Vector _tasks;
    private int _currentIndex = 0;
    private LogicTask _currentTask;
    
    //// CONSTRUCTOR -----------------------------------------------------------
    
    public AutonLogic(){
		super();
    }
    
    //// INITIALIZATION --------------------------------------------------------
    
    public void initPhase() {
		// Populate Tasks Array
		_tasks = new Vector();

		// Begin First Task
		_currentIndex = 0;
		setCurrentTask((LogicTask)_tasks.elementAt(_currentIndex));
    }

    //// UPDATE ----------------------------------------------------------------
    
	/**
	 * Manages task flow.  Should be called periodically while AutonLogic is
	 * active.
	 */
    public void updatePhase() {
		// Update Current Task
		_currentTask.updateTask();

		if(_currentTask.isDone()){
			println("Task Reported DONE, moving to next Task...");
			nextTask();
		}
    }

    //// FINISH ----------------------------------------------------------------
    
    /**
     * Stops the current Task and any ongoing processes.  Not responsible for
	 * the segue to TeleopLogic.
     */
    public void finishPhase() {
		_currentTask.finishTask();
		_currentTask = null;
    }
    
    //// TASK LOGIC ------------------------------------------------------------
    
    /**
     * Transitions to the next Task in the sequence.
     * @see #setCurrentTask(robot.logic.LogicTask) 
     */
    public void nextTask(){
		if(_currentIndex < _tasks.size() - 1){
			setCurrentTask((LogicTask)_tasks.elementAt(++_currentIndex));
		} else {
			System.out.println("AutonLogic :: No Tasks Remain.  Finishing...");
			RobotMain.getInstance().endPhase();
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
    }
}