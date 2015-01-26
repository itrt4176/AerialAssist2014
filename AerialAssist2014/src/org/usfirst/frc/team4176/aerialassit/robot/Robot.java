/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//Programmed By:
//Nick & Louis
//2014

package org.usfirst.frc.team4176.aerialassit.robot;


import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import java.lang.Math;

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
    Joystick xboxCntrlr2 = new Joystick(2);
    Talon frontLeft = new Talon(1); //Talon is a motor controller
    Talon frontRight = new Talon(2);
    Talon rearLeft = new Talon(3);
    Talon rearRight = new Talon(4);
    Talon pulleyLeft = new Talon(5);
    Talon pulleyRight = new Talon(6);
    Talon doorLeft = new Talon(7);
    Talon doorRight = new Talon(8);
    DigitalInput topLeft = new DigitalInput(13); //Sensors
    DigitalInput topRight = new DigitalInput(14); //
    DigitalInput botLeft = new DigitalInput(11); //
    DigitalInput botRight = new DigitalInput(12); //End sensors
    RobotDrive drive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight); //This tells the robot how to drive the motors
    
    public boolean leftInTransit = botLeft.get() && topLeft.get() || topLeft.get() == false; //leftInTransit is true if neither left sensor is triggered
    public boolean rightInTransit = botRight.get() && topRight.get() || topRight.get() == false; //rightInTransit is true in neither right sensor is triggered
    
    DriverStation ds = DriverStation.getInstance();
    
    

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        
    }   

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {

       while(isOperatorControl() && isEnabled()) {
           SmartDashboard.putString("Version", "Competiton2.0");
           SmartDashboard.putBoolean("Top Left Sensor", topLeft.get());
           SmartDashboard.putBoolean("Bottom Left Sensor", botLeft.get());
           SmartDashboard.putBoolean("Top Right Sensor", topRight.get());
           SmartDashboard.putBoolean("Bottom Right Sensor", botRight.get());
           double controlX = xboxCntrlr.getRawAxis(1);
           double controlY = xboxCntrlr.getRawAxis(2);
          //Xbox:
           double control2X = xboxCntrlr.getRawAxis(4);
           double control2Y = xboxCntrlr.getRawAxis(5);
           
           //Gamecube:
//           double control2X = xboxCntrlr.getRawAxis(4);
//           double control2Y = xboxCntrlr.getRawAxis(5);
           double control3Y = xboxCntrlr2.getRawAxis(2);
           double control4Y = xboxCntrlr2.getRawAxis(5);
           boolean rightTrue = false;
           boolean leftTrue = false;
           
           //setSenseLevel();
           
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

           
           SmartDashboard.putNumber("X Axis", controlX);
           SmartDashboard.putNumber("Y Axis", controlY);
           SmartDashboard.putNumber("X Axis 2", control2X);
           SmartDashboard.putNumber("Y Axis 2", control2Y);
           SmartDashboard.putNumber("X Out", motorX);
           SmartDashboard.putNumber("Y Out", motorY);
           SmartDashboard.putNumber("X Out 2", motorX);
           SmartDashboard.putNumber("Y Out 2", motor2Y);

           drive.mecanumDrive_Cartesian(motorX, motorY, motor2X, 0); //Mecanum wheels are controlled by joystick 1: X and Y, and joystick 2: Y
           
           doorControl(.8 * control3Y, .8 * control4Y);

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
   
    public void doorControl(double control3Y, double control4Y) {
               doorLeft.set(-control3Y);
        
               doorRight.set(-control4Y);
    }
}