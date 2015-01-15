/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//Programmed By:
//Nick & Louis
//2014


//TODO
/*



Create a crawl, walk, run modes such that the robot isn't as sensitive in drive
this can be done by setting a variable (say x, = 0.2(crawl), 0.6(walk), 1(run)) to a different 
value and then scaling the input from the controller (y) to that value in the drive code. (x * y) = motor output
you can set x by scrolling through it by pressing a button on the controller

COMPLETED:
Extender Arms
    make them operate separately
    only allow one to operate at a single time, we cant have more than one up at a time. program in the other buttons



    Not sure why but the dead zone was programmed as a square not a circle, while it
    makes sense on a joystick level it doesn't make sense on a drive basis. It would
    be better as a r = sqrt(x^2 + y^2) type deal

    java.lang.math wouldn't work in the code so sqrt wouldn't work. Check the old 
    code to see what might be wrong. I saw that they were using Java.Lang.Math in it.
    An email tree would be great to have  -- TO TEST


Fix the Front left wheel's wiring and recode it to run correctly, it was programmed backwards in the code. -- WIRING FIX

*/

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
        /*while (isAutonomous() && isEnabled()){
            /*frontLeft.set(-1); //Set all wheels to drive foward
            frontRight.set(-1);
            rearLeft.set(-1);
            rearLeft.set(-1);
            Timer.delay(2); //Wait 2 seconds
            frontLeft.set(0); //Set all wheels to stop
            frontRight.set(0);
            rearLeft.set(0);
            rearLeft.set(0);*/
            
                /*if (topLeft.get()) {
                leftExtend();
            }
            else if(topLeft.get() == false){
                leftStop();
            }
        }*/
        
        if(ds.getDigitalIn(1))
        {
            Timer timer1517 = new Timer();
            timer1517.start();
            while(timer1517.get() < 5.0)
            {
                drive.mecanumDrive_Cartesian(0, -0.45, 0, 0);
            }
            drive.tankDrive(0, 0);
            timer1517.stop();
        }
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
           
           //armControl();
           oneArmControl();
           
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
       /*if (speedScale){
           motorX = (controlX * 0.25);
           motorY = (controlX * 0.25);
           Timer.delay(250);
           motorX = (controlX * 0.5);
           motorY = (controlX * 0.5);
           Timer.delay(250);
           motorX = (controlX * 0.75);
           motorY = (controlX * 0.75);
           Timer.delay(250);
           speedScale = false;
       } else {
           motorX = controlX;
           motorY = controlY;
       }*/
       motorX = controlX;
       motorY = controlY;
       motor2X = control2X;
       motor2Y = control2Y;
   }

    /**
     * Old method of extending the arms
     */
    public void extendArmOld() {
       boolean leftTrue = false;
       boolean rightTrue = false;
       if (topLeft.get() == true) {
               leftTrue = true;
           }
           
           if (botLeft.get() == true) {
               leftTrue = false;
           }
           
           if (topRight.get() == true) {
               rightTrue = true;
           }
           
           if (botRight.get() == true) {
               rightTrue = false;
           }
            
            //If button LB is pressed, the right arm is down, and the arm isn't extended, then extend the left arm
           if (xboxCntrlr.getRawButton(5) && rightTrue == false && leftTrue == false) { 
               leftExtend(); //extend
               //Timer.delay(0.5);
               //leftTrue = true;
           }
           
           //If button LB is pressed, the right arm is down, and the left arm is extended, then retract the arm
           else if (xboxCntrlr.getRawButton(5) && rightTrue == false && leftTrue == true) { 
               leftRetract(); //retract
               //Timer.delay(0.5);
               //leftTrue = false;
           }

           else { //Otherwise do nothing
               leftStop();

           }
           //If button RB is pressed, the left arm is down, and the right arm isn't extended, then extend the arm
           if (xboxCntrlr.getRawButton(6) && leftTrue == false && rightTrue == false) { 
               rightExtend();
               //Timer.delay(0.5);
               //rightTrue = true;
           }
           //If button RB is pressed, the left arm is down, and the right arm is extended, then retract the arm
           else if (xboxCntrlr.getRawButton(6) && leftTrue == false && rightTrue == true) { 
               rightRetract();
               //Timer.delay(0.5);
               //rightTrue = false;
           }

           else { //Otherwise do nothing
               rightStop();

           }
   }
    
    public void armControl() {
                //press A, top L sensor not triggered, and bottom right is triggered, then extend L arm
	        if (xboxCntrlr2.getRawButton(1) && botRight.get() == false && topLeft.get()) {
	            leftExtend();
	        }
	        //press B, bottom L sensor not triggered, then retract L arm
	        else if (xboxCntrlr2.getRawButton(2) && botLeft.get()) {
	            leftRetract();
                }
	        else{
	            leftStop();
	        }
	        //press x, top sensor not triggered, and bottom left is triggered then extend R arm
	        if (xboxCntrlr2.getRawButton(3) && botLeft.get() == false && topRight.get()) {
	            rightExtend();
	        }
	        //press Y, bottom sensor not triggered, then retract R arm
	        else if (xboxCntrlr2.getRawButton(4) && botRight.get()) {
	            rightRetract();
	        }
	        else {
	            rightStop();
	        }
	    }
    
    public void doorControl(double control3Y, double control4Y) {
               doorLeft.set(-control3Y);
        
               doorRight.set(-control4Y);
           
    }

    public void armControlBroken() {
        
        //left arm
        if (xboxCntrlr.getRawButton(5)) {
               //if right arm is down and left is down, then extend arm.
               if (botRight.get() == false && topLeft.get()) {
                   leftExtend(); //extend left arm
               }
               //if right arm is down and left arm is up, then retract arm.
               else if (botRight.get() == false && topLeft.get() == false) {
                   leftRetract(); //retract left arm
               }
               
               //if right arm is down and left is moving then retract arm
               else if (botRight.get() == false && leftInTransit) {
                   leftRetract(); //retract left arm
               }
               
               
               else {
                   leftStop();
               }
        
            if (rightInTransit || topRight.get() == false) {
                rightRetract(); //retract right arm that's already moving.
                armUp = true;
            }
            
            else {
                rightStop();
            }
        }
 
        else {
            quickLeftExtend();
            armUp = false;
        }
           
           
           if (xboxCntrlr.getRawButton(6)) { 
               if (botLeft.get() == false && topRight.get()) {
                   rightExtend();
               }
               
               else if (botLeft.get() == false && topRight.get() == false) {
                   rightRetract();
               }
               
               else if (botLeft.get() == false && rightInTransit) {
                   rightRetract();
               }
               else {
                   rightStop();
                   
               }
               if (leftInTransit || topLeft.get()) {
                   leftRetract();
                   armUp = true;
               }
               
               else {
                   leftStop();
               }
           }
           else {
               quickRightExtend();
               armUp = false;
           }
        
    }
       
    public void quickRightExtend() {
        if (armUp && botLeft.get() == false && topRight.get()) {
                       rightExtend(); //extend left arm
                   }
                   else {
                       rightStop();
                   }
    }
    
    public void quickLeftExtend() {
        if (armUp && botRight.get() == false && topLeft.get()) {
           leftExtend(); //extend left arm
       }
       else {
           leftStop();
       }
    }
    
    public void leftExtend() {
        pulleyLeft.set(-1);
    }
    
    public void rightExtend() {
        pulleyRight.set(1);
    }
    
    public void leftRetract() {
        pulleyLeft.set(1);
    }
    
    public void rightRetract() {
        pulleyRight.set(-1);
    }
    
    public void leftStop() {
        pulleyLeft.set(0);
    }
    
    public void rightStop() {
        pulleyRight.set(0);
    }
    
    public void oneArmControl() {
        //press A, top L sensor not triggered, then extend L arm
        if (xboxCntrlr2.getRawButton(1) && topLeft.get()) {
            leftExtend();
        }
        //press B, bottom L sensor not triggered, then retract L arm
        else if (xboxCntrlr2.getRawButton(2) && botLeft.get()) {
            leftRetract();
        }
        else{
            leftStop();
        }
    }
    
}

 /*if(Math.sqrt(controlX*controlX + controlY*controlY) < dead){
               controlX = 0;
               controlY = 0;
           }
           
           if (Math.sqrt(control2X*control2X + control2Y*control2Y) < dead) {
               control2X = 0;
               control2Y = 0;
           }
           
           if(Math.sqrt(controlX*controlX + controlY*controlY) < dead){
               controlX = 0;
               controlY = 0;
           }
           
           if (Math.sqrt(control2X*control2X + control2Y*control2Y) < dead) {
               control2X = 0;
               control2Y = 0;
           }
           
           if (control3Y < .15 && control3Y > -.15) {
               control3Y = 0;
           }
           
           if (control4Y < .15 && control4Y > -.15) {
               control4Y = 0;
           }*/