import { Notification } from './notification';

/**
 * Represents an external application capable of sending notifications
 */
export class Application {
  
  public applicationId:string;
  public name:string;
  public icon:string;
  public lastMessageText:string;
  public notifications: Notification[];
  
}