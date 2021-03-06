/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.logic.tasks;

import robot.behavior.RobotShoot;
import robot.logic.LogicTask;
import robot.logic.targeting.TargetShooterSpeedLogic;

/**
 * Sets the shooter's target speed.  Note:  This does not set the speed 
 * directly, but rather informs RobotShoot of the desired value, allowing it to
 * perform logic to regulate the speed with a PID loop.
 * @author Ben
 */
public class TSetShooterSpeed extends LogicTask {
	//// CONSTANTS -------------------------------------------------------------
	
	//// PRIVATE VARIABLES -----------------------------------------------------
	
	private double _targetSpeedRPM;
	
	//// CONSTRUCTOR -----------------------------------------------------------
	
	/**
	 * Sets the shooter's target speed.  Note:  This does not set the speed
	 * directly, but rather informs RobotShoot of the desired value, allowing it
	 * to perform logic to regulate the speed with a PID loop.
	 * @param speedRPM The desired shooter wheel speed. (-1.0 to 1.0)  
	 */
	public TSetShooterSpeed(double speedRPM){
		_targetSpeedRPM = speedRPM;
	}

	//// INITIALIZATION --------------------------------------------------------
	
	protected void initialize() {
		TargetShooterSpeedLogic.setRestSpeedRPM(_targetSpeedRPM);
	}

	//// UPDATE ----------------------------------------------------------------
	
	protected void update() {
		_done = true;
	}

	//// FINISH ----------------------------------------------------------------
	protected int finish() {
		return _done ? SUCCESS : FAILURE;
	}
}
