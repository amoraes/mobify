import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { Storage } from '@ionic/storage';

import { BasicService } from './basic.service';

import { NotificationService } from './notification.service';
import { ApplicationService } from './application.service';

import { Application } from '../model/application';
import { ApplicationSettings } from '../model/application-settings';
import { Notification } from '../model/notification';

/**
 * This class is a singleton to keep the content of the application synchronized
 */
@Injectable()
export class ContentService extends BasicService{

  //stores all applications and user settings. Key: applicationId
  private applications:Map<String,Application>;
  //stores all notifications received. Key: applicationId
  private notifications:Map<String,Notification[]>;
  
  constructor(private notificationService: NotificationService, 
              private applicationService: ApplicationService, 
              private storage: Storage) {
    super();
  }
  
  public init(): Promise<boolean> {
    console.log('initializing mobify data');
    this.applications = new Map<String,Application>();
    this.notifications = new Map<String,Notification[]>();

    //load all app settings online
    return this.applicationService.getAll()
    .then(allAppSettings => {
      for(let appSettings of allAppSettings){
        this.applications.set(appSettings.applicationId, appSettings);
        //get all the locally stored notifications
        this.storage.get('notifications_'+appSettings.applicationId).then(
          jsonArray => {
            let notificationsArray: Notification[];
            if(jsonArray != null){
                 notificationsArray = JSON.parse(jsonArray);
                 //it could be cleared by the user
                 if(notificationsArray.length > 0){
                    //set the last message
                    let app:Application = this.applications.get(appSettings.applicationId);
                    app.lastNotificationText = notificationsArray[notificationsArray.length-1].message;
                    //set the number of unread messages
                    let countUnread:number = 0;
                    for(let n of notificationsArray){
                      if(n.read != true){
                        countUnread++;
                      }
                    }
                    app.unreadNotificationsCount = countUnread;
                 }
                 
            }else{
                //if the app does not have any notifications, create a new array to store them
                notificationsArray = new Array();
            }
            this.notifications.set(appSettings.applicationId, notificationsArray);
          }
        );
      }
      console.log(this.notifications);
      //get all the unreceived messages
      let newMessagesReceived: boolean = false;
      return this.notificationService.getUnreceived()
      .then(notifications => {        
         for(let notification of notifications){
           this.receiveNotification(notification);
           newMessagesReceived = true;
         }   
         if(newMessagesReceived){
           this.flushNotifications();
         }
         return true;
      });
    })   
    .catch(this.handleError);
     
  }
  
  public getApplications(): IterableIterator<Application> {
    return this.applications.values();
  }

  /**
   * Receive a notification
   */
  public receiveNotification(notification: Notification): void{
    //put them in the array
    this.notifications.get(notification.applicationId).push(notification);
    let app:Application = this.applications.get(notification.applicationId);
    app.lastNotificationText = notification.message;
    app.unreadNotificationsCount++;
  }

  /**
   * Store the notifications on the local storage
   */
  public flushNotifications(): void{
    let iter = this.notifications.keys();
    let result = iter.next();
    while(result.done == false){
      let applicationId: String = result.value;
      let notificationsArray: Notification[] = this.notifications.get(applicationId);
      let json = JSON.stringify(notificationsArray);
      this.storage.set('notifications_'+applicationId, json);
      console.log('Stored locally '+notificationsArray.length+' notifications ('+applicationId+')');
      result = iter.next();
    }
  }

  /**
   * Clear all the notifications of an application
   */
  public clearNotifications(applicationId:string): void {
    //clear application settings (unread and last message)
    let app:Application = this.applications.get(applicationId);
    app.unreadNotificationsCount = 0;
    app.lastNotificationText = '';
    //clear the notifications
    let emptyArray:Notification[] = new Array();
    //clear the array
    this.notifications.set(applicationId, emptyArray);
    //clear the local storage
    this.storage.set('notifications_'+applicationId, JSON.stringify(emptyArray));
  }

  /**
   * Toogle mute an application
   */
  public muteApplication(applicationId:string): void {
    let app:Application = this.applications.get(applicationId);
    if(app.silent){ //unmute
      app.silent = false;
    }else{ //mute
      app.silent = true;
    }
    this.applicationService.updateApplicationSettings(applicationId, app.silent);
  }

}
