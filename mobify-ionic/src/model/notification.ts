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
  
   /**
  * Return a formatted date string
  */
  public getTimestampSentAsString(): string {
    let now:Date = new Date();
    //if it is the same day, return the time
    if(now.toISOString().substring(0, 10) == this.timestampSent.toISOString().substring(0, 10)){
       return this.timestampSent.toISOString().substr(11,5); 
    }else{ //else, return the date
      return this.timestampSent.getDate()+"/"+this.timestampSent.getMonth()+1;
    }
  }
}