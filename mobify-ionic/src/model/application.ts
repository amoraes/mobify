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
  public unreadNotificationsCount:number = 0;
  public silent:boolean = false;
  
}