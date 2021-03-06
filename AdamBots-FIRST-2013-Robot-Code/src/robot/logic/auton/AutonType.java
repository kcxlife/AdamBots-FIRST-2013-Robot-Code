/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.logic.auton;

//import edu.wpi.first.wpilibj.networktables2.util.List;
import robot.control.MagicBox;
import robot.logic.tasks.TAwaitStatus;
import robot.logic.tasks.TDelay;
import robot.logic.tasks.TFeedDisc;
import robot.logic.tasks.TSetShooterAngle;
import robot.logic.tasks.TSetShooterSpeed;
import robot.logic.tasks.TStopShooter;
import robot.sensors.RobotSensors;
import utils.Listy;

/**
 *
 * @author Steven
 */
public class AutonType {
	//// STATIC AUTONOMOUS TYPES -----------------------------------------------
	
	public static final class Simple {
		//// INITIALIZATION ----------------------------------------------------
		
		// Perform any necessary initialization here.
		static {
			
		}
		
		//// SHOOT 3 DISCS -------------------------------------------------
		
		/**
		 * A simple autonomous program that shoots three discs.
		 * 
		 * <ol>
		 *	<li>Set shooter speed.</li>
		 *  <li>Shoot (x3).
		 *		<ul>
		 *			<li>Enable shooter feeder solenoid.
		 *			<li>Give the shooter feeder solenoid time to expand.
		 *			<li>Disable the shooter feeder solenoid.
		 *			<li>Wait for the shooter wheel to recover.
		 *		</ul>
		 *	<li>Shoot
		 *  <li>Shoot
		 * </ol>
		 * 
		 * @see Fancy#shootDiscs(int) 
		 */
		public static final Listy SIMPLE_THREE_SHOTS = AutonType.Fancy.shootDiscs(3, AutonType.Fancy.DEFAULT_INITIAL_DELAY_MILLIS);
		
		//// SHOOT 4 DISCS -------------------------------------------------
		
		/**
		 * A simple autonomous program that sets the shooter's target angle
		 * and shoots three discs.
		 * 
		 * <ol>
		 *	<li>Set shooter speed.</li>
		 *  <li>Set shooter angle.</li>
		 *  <li>Shoot (x4).
		 *		<ul>
		 *			<li>Enable shooter feeder solenoid.
		 *			<li>Give the shooter feeder solenoid time to expand.
		 *			<li>Disable the shooter feeder solenoid.
		 *			<li>Wait for the shooter wheel to recover.
		 *		</ul>
		 *	<li>Shoot
		 *  <li>Shoot
		 * </ol>
		 * 
		 * @see Fancy#shootDiscs(int) 
		 */
		public static final Listy SIMPLE_FOUR_SHOTS = AutonType.Fancy.shootDiscs(4, AutonType.Fancy.DEFAULT_INITIAL_DELAY_MILLIS);
		
		//// SHOOT 3 DISCS -------------------------------------------------
		
		/**
		 * A simple autonomous program that sets the shooter's target angle
		 * and shoots three discs.
		 * 
		 * <ol>
		 *	<li>Set shooter speed.</li>
		 *  <li>Set shooter angle.</li>
		 *  <li>Shoot (x3).
		 *		<ul>
		 *			<li>Enable shooter feeder solenoid.
		 *			<li>Give the shooter feeder solenoid time to expand.
		 *			<li>Disable the shooter feeder solenoid.
		 *			<li>Wait for the shooter wheel to recover.
		 *		</ul>
		 *	<li>Shoot
		 *  <li>Shoot
		 * </ol>
		 * 
		 * @see Fancy#shootDiscs(int) 
		 */
		public static final Listy SIMPLE_ANGLED_THREE_SHOTS = AutonType.Fancy.angledShootDiscs(3, AutonType.Fancy.DEFAULT_INITIAL_DELAY_MILLIS);
		
		//// SHOOT 4 DISCS -------------------------------------------------
		
		/**
		 * A simple autonomous program that shoots three discs.
		 * 
		 * <ol>
		 *	<li>Set shooter speed.</li>
		 *  <li>Shoot (x4).
		 *		<ul>
		 *			<li>Enable shooter feeder solenoid.
		 *			<li>Give the shooter feeder solenoid time to expand.
		 *			<li>Disable the shooter feeder solenoid.
		 *			<li>Wait for the shooter wheel to recover.
		 *		</ul>
		 *	<li>Shoot
		 *  <li>Shoot
		 * </ol>
		 * 
		 * @see Fancy#shootDiscs(int) 
		 */
		public static final Listy SIMPLE_ANGLED_FOUR_SHOTS = AutonType.Fancy.angledShootDiscs(4, AutonType.Fancy.DEFAULT_INITIAL_DELAY_MILLIS);
	}
	
	//// DYNAMIC AUTONOMOUS TYPES ----------------------------------------------
	
	public static final class Fancy {
		//// CONSTANTS ---------------------------------------------------------
		
		/** Default initial delay for Autonomous (to wait for the compressor). */
		public static final int DEFAULT_INITIAL_DELAY_MILLIS = 4000;
		/** Default feed arm delay.  Determines for how long the arm is extended. */ 
		public static final int DEFAULT_FEED_DELAY_MILLIS = 800;
		/** Default shot delay. */
		public static final int DEFAULT_SHOT_DELAY_MILLIS = 800;
		
		//// INITIALIZATION ----------------------------------------------------
		
		// Perform any necessary initialization here.
		static {
			
		}
		
		//// FANCY SHOOT DISCS -------------------------------------------------
		
		public static Listy shootDiscs(int discs){
			return shootDiscs(discs, DEFAULT_FEED_DELAY_MILLIS, DEFAULT_SHOT_DELAY_MILLIS, 0);
		}
		
		public static Listy shootDiscs(int discs, int initialDelayMillis){
			return shootDiscs(discs, DEFAULT_FEED_DELAY_MILLIS, DEFAULT_SHOT_DELAY_MILLIS, initialDelayMillis);
		}
		
		public static Listy shootDiscs(int discs, int feedDelayMillis, int shotDelayMillis){
			return shootDiscs(discs, feedDelayMillis, shotDelayMillis, 0);
		}
		
		/**
		 * Generates the task list for an Autonomous Phase that shoots a
		 * specified number of discs.  Remember that we can only hold four at 
		 * once, so it is silly to shoot more than four times in this mode,
		 * except, perhaps, to protect against flawed magazine design.
		 * 
		 * <ol>
		 *	<li>Set shooter speed.</li>
		 *  <li>Shoot X Times.
		 *		<ul>
		 *			<li>Enable shooter feeder solenoid.
		 *			<li>Give the shooter feeder solenoid time to expand.
		 *			<li>Disable the shooter feeder solenoid.
		 *			<li>Wait for the shooter wheel to recover.
		 *		</ul>
		 *	<li>Shoot
		 *  <li>Shoot
		 * </ol>
		 * 
		 * @param discs The number of discs to shoot.
		 * @param feedDelayMillis Delay between expanding and contracting the feeder arm.
		 * @param shotDelayMillis The number of milliseconds to wait after each shot.
		 * @param initialDelayMillis Initial Delay.
		 * @return A list of LogicTasks.
		 * @see robot.logic.LogicTask
		 * @see Fancy#shootDiscs(int) 
		 */
		public static Listy shootDiscs(int discs, int feedDelayMillis, int shotDelayMillis, int initialDelayMillis){
			Listy tasks = new Listy();
			
			// Set Shooter Speed
			tasks.add(new TSetShooterSpeed(MagicBox.PYRAMID_SHOT_SPEED));
			
			// Wait
			tasks.add(new TDelay(initialDelayMillis));
		
			// Shoot X Discs
			for(int i = 0; i < discs; i++){
				tasks.add(new TFeedDisc(feedDelayMillis));
				tasks.add(new TDelay(shotDelayMillis));
			}
			
			// Stop the Shooter
			tasks.add(new TStopShooter());
			
			return tasks;
		}
		
		//// ANGLED SHOOT DISCS ------------------------------------------------
		
		public static Listy angledShootDiscs(int discs){
			return angledShootDiscs(discs, DEFAULT_FEED_DELAY_MILLIS, DEFAULT_SHOT_DELAY_MILLIS, 0);
		}
		
		public static Listy angledShootDiscs(int discs, int initialDelayMillis){
			return angledShootDiscs(discs, DEFAULT_FEED_DELAY_MILLIS, DEFAULT_SHOT_DELAY_MILLIS, initialDelayMillis);
		}
		
		public static Listy angledShootDiscs(int discs, int feedDelayMillis, int shotDelayMillis){
			return angledShootDiscs(discs, feedDelayMillis, shotDelayMillis, 0);
		}
		
		/**
		 * Generates the task list for an Autonomous Phase that shoots a
		 * specified number of discs.  Remember that we can only hold four at 
		 * once, so it is silly to shoot more than four times in this mode,
		 * except, perhaps, to protect against flawed magazine design.
		 * 
		 * <ol>
		 *	<li>Set shooter speed target.</li>
		 *	<li>Set shooter angle target.</li>
		 *  <li>Wait for the correct shooter angle.</li>
		 *  <li>Shoot X Times.
		 *		<ul>
		 *			<li>Enable shooter feeder solenoid.</li>
		 *			<li>Give the shooter feeder solenoid time to expand.</li>
		 *			<li>Disable the shooter feeder solenoid.</li>
		 *			<li>Wait.</li>
		 *		</ul>
		 *	<li>Shoot
		 *  <li>Shoot
		 * </ol>
		 * 
		 * @param discs The number of discs to shoot.
		 * @param feedDelayMillis Delay between expanding and contracting the feeder arm.
		 * @param shotDelayMillis The number of milliseconds to wait after each shot.
		 * @param initialDelayMillis Initial Delay.
		 * @return A list of LogicTasks.
		 * @see robot.logic.LogicTask
		 * @see Fancy#angledShootDiscs(int) 
		 */
		public static Listy angledShootDiscs(int discs, int feedDelayMillis, int shotDelayMillis, int initialDelayMillis){
			Listy tasks = new Listy();
			
			// Set Speed & Angle
			tasks.add(new TSetShooterSpeed(MagicBox.PYRAMID_SHOT_SPEED));
			tasks.add(new TSetShooterAngle(RobotSensors.configA.get()?MagicBox.PYRAMID_SIDE_SHOT_ANGLE:MagicBox.PYRAMID_MIDDLE_SHOT_ANGLE));
			
			// Wait
			tasks.add(new TDelay(initialDelayMillis)); 
			
			// Wait for Shooter with a 2s timeout
			tasks.add(new TAwaitStatus(TAwaitStatus.SHOOTER_IN_POSITION, 2000));
			
			// Shoot X Times
			for(int i = 0; i < discs; i++){
				tasks.add(new TFeedDisc(feedDelayMillis));
				tasks.add(new TDelay(shotDelayMillis));
			}
			
			// Stop the Shooter
			tasks.add(new TStopShooter());
			return tasks;
		}
		
		//// DRIVE STRAIGHT ----------------------------------------------------
		
		public static Listy driveStraight(double distanceInches){
			Listy tasks = new Listy();
			
			//tasks.add(new TDriveDistance2(distanceInches, true));
			
			return tasks;
		}
	}	
}
