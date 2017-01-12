import { Notification } from './notification';

/**
 * Represents an external application capable of sending notifications 
 * combined with the user settings defined for this application
 */
export class Application {
  
  public applicationId:string;
  public name:string;
  public icon:string;
  public lastNotificationText:string;
  public lastNotificationTimestamp:Date;
  public unreadNotificationsCount:number = 0;
  public silent:boolean = false;

  /**
  * Return a formatted date string
  */
  public getLastTimestampAsString(): string {
    let now:Date = new Date();
    //if it is the same day, return the time
    if(now.toISOString().substring(0, 10) == this.lastNotificationTimestamp.toISOString().substring(0, 10)){
       return this.lastNotificationTimestamp.getHours()+":"+this.lastNotificationTimestamp.getMinutes(); 
    }else{ //else, return the date
      return this.lastNotificationTimestamp.getDate()+"/"+this.lastNotificationTimestamp.getMonth();
    }
  }
  
}