/**
 * Represents a notification received
 */
export class Notification {
  
  public notificationId:string ;
  public applicationId:string;
  public type:string;
  public message:string;
  public timestampSent:Date;
  public read:boolean;
  
}