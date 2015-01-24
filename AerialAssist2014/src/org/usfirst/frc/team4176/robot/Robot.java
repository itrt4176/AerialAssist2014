/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//Programmed By:
//Nick & Louis
//2014

package org.usfirst.frc.team4176.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;

//A=button 1
//B=button 2
//X=button 3
//Y=button 4
//LB=button 5
//RB=button 6
//Back=button 7
//Start=Button 8
//Left axis press=button 9
//Right axis press=button 10
//xbox button isn't a thing.



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends SampleRobot {
    public double motorX;
    public double motorY;
    public double motor2X;
    public double motor2Y;
    public double dead = .15; //dead zone
    public int sensitivity = 2; //counter
    public boolean lbPressed = false;
    public boolean rbPressed = false;
    public boolean armUp = false;
    public boolean speedScale = false;
    

    Joystick xboxCntrlr = new Joystick(1); //Xbox Controller is a considered joystick. Right joystick on actual controller is axis 3 and 4
    Talon frontLeft = new Talon(2); //Talon is a motor controller
    Talon frontRight = new Talon(3);
    Talon rearLeft = new Talon(5);
    Talon rearRight = new Talon(1);
    Talon forkLift = new Talon(4);
    RobotDrive drive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight); //This tells the robot how to drive the motors
    
    DriverStation ds = DriverStation.getInstance();
    
    

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        
/*        if(ds.getDigitalIn(1))
        {
            Timer timer1517 = new Timer();
            timer1517.start();
            while(timer1517.get() < 5.0)
            {
                drive.mecanumDrive_Cartesian(0, -0.45, 0, 0);
            }
            drive.tankDrive(0, 0);
            timer1517.stop();
        }*/
    }   

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {

       while(isOperatorControl() && isEnabled()) {
           SmartDashboard.putString("Version", "2015_1.0_alpha1");
           double controlX = xboxCntrlr.getRawAxis(1);
           double controlY = xboxCntrlr.getRawAxis(2);
           double control2X = xboxCntrlr.getRawAxis(4);
           double control2Y = xboxCntrlr.getRawAxis(5);
           double controlTriggers = xboxCntrlr.getRawAxis(3);
           
           //Dead zone
           if (controlX < .18 && controlX > -.18) {
               controlX = 0;
               speedScale = true;
           }
           
           if (controlY < .18 && controlY > -.18) {
               controlY = 0;
           }
           
           if (control2X < .18 && control2X > -.18) {
               control2X = 0;
           }
           
           if (control2Y < .18 && control2Y > -.18) {
               control2Y = 0;
           }
           setSensitivity(controlX, controlY, control2X, control2Y, sensitivity);
           
           forkliftControl(controlTriggers);

           
           SmartDashboard.putNumber("X Axis", controlX);
           SmartDashboard.putNumber("Y Axis", controlY);
           SmartDashboard.putNumber("X Axis 2", control2X);
           SmartDashboard.putNumber("Y Axis 2", control2Y);

           drive.mecanumDrive_Cartesian(motorX, motorY, motor2X, 0); //Mecanum wheels are controlled by joystick 1: X and Y, and joystick 2: Y
          

           //Timer.delay(0.005);
       }

    }
    
    

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {

    }
    
    /**
     * This function is called to set the sensitivity level of the controller
     */
    public void setSenseLevel() {
       if (xboxCntrlr.getRawButton(8) && sensitivity < 3) { //Increase controller sensitivity level when start is pressed
               sensitivity = sensitivity + 1;
       }

       if (xboxCntrlr.getRawButton(7) && sensitivity > 1) { //Decrease when maximum level is reached
           sensitivity = sensitivity - 1;
       }
    }
    
    /**
     * This function is called to apply the sensitivity level to the controller
     */
    public void setSensitivity(double controlX, double controlY, double control2X, double control2Y, int sensitivity) { //Applies a level of sensitivty to joystick output
       motorX = controlX;
       motorY = controlY;
       motor2X = control2X;
       motor2Y = control2Y;
   }
    
    public void forkliftControl(double controlTriggers){
    	forkLift.set(0.1 * controlTriggers);
    }
}